package rip.skyland.redis.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import rip.skyland.redis.redis.packet.Packet;

import javax.swing.*;

public class RedisHandler {

private String host;
    private String password;
    private int port;

    private JedisPool jedisPool;

    private String channel;

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

    /**
     * Attempts to make a connection to the
     * redis database with the specified credentials and
     * starts a thread for receiving messages
     */
    public void connect() {
        this.jedisPool = new JedisPool(host, port);
        this.jedisPool.getResource().auth(password.isEmpty() ? null : password);

        // Setup thread

        new Thread(() -> {

            this.runCommand(redis -> {

                // authenticate with the specified credentials
                redis.auth(this.password.isEmpty() ? null : this.password);

                redis.subscribe(new JedisPubSub() {

                    @Override
                    public void onMessage(String channel, String message) {
                        try {
                            // Create the packet
                            int index = message.indexOf("||");
                            Object jsonObject = gson.fromJson(message.substring(index + 2), Class.forName(message.substring(0, index)));
                            Packet packet = (Packet) jsonObject;

                            packet.onReceive();

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }, "core");
            });
        }).start();

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
