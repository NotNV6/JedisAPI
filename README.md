# JedisAPI
A simplistic yet clean Jedis API

# Examples
Setup the RedisHandler:
```
RedisHandler handler = new RedisHandler("localhost" /*ip*/, "" /*password*/, 27017 /*port*/);
handler.load();
```

Send a packet:
```
handler.sendPacket(packet);
```
