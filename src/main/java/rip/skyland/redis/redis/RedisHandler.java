package rip.skyland.redis.redis;

import com.google.gson.Gson;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import rip.skyland.redis.redis.packet.Packet;

public class RedisHandler {

    private String host;
    private String password;
    private int port;

    private JedisPool jedisPool;

    private String channel;

    @Getter
    private Gson gson;

    /**
     * create a new RedisHandler
     *
     * @param host the ip address of the redis server.
     * @param password the password address of the redis server.
     * @param port the port of the redis server.
     */
    public RedisHandler(String host, String password, int port) {
        this.host = host;
        this.password = password;
        this.port = port;

        this.gson = new Gson();
        this.channel = "example";
    }

    public void load() {
        this.jedisPool = new JedisPool(host, port);
        jedisPool.getResource().auth(password.isEmpty() ? null : password);

        new Thread(() ->
            runCommand(redis -> {
                redis.auth(password.isEmpty() ? null : password);
                redis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        try {
                            int index = message.indexOf("||");
                            ((Packet) new Gson().fromJson(message.substring(index + 2), Class.forName(message.substring(0, index)))).onReceive();
                        } catch (ClassNotFoundException | NoClassDefFoundError exception) {
                            exception.printStackTrace();
                        }
                    }
                }, "core");
            })).start();
    }

    /**
     * sends a packet through redis
     *
     * @param packet the packet to get sent
     */

    public void sendPacket(Packet packet) {
        packet.onSend();

        Thread thread = new Thread(() ->
                runCommand(redis -> {
                    redis.auth(password.isEmpty() ? null : password);
                    redis.publish(channel, packet.getClass().getName() + "||" + gson.toJson(packet));
                }));

        thread.start();
        Timer timer = new Timer(2000, arg0 -> thread.stop());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * sends a packet through redis
     *
     * @param callback the callback to be executed
     */
    private void runCommand(Callback callback) {
        Jedis jedis = jedisPool.getResource();
        if (jedis != null) {
            callback.execute(jedis);
            jedisPool.returnResource(jedis);
        }
    }


}
