# JedisAPI
A Jedis API mainly made for Bukkit plugins (can also be used for other things)

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
