package rip.skyland.redis.redis;

import redis.clients.jedis.Jedis;

public interface Callback {

    void execute(Jedis jedis);
}
