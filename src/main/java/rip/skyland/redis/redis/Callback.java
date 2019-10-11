package rip.skyland.chat.util;

import redis.clients.jedis.Jedis;

public interface Callback {

    void execute(Jedis jedis);
}