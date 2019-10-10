# JedisAPI
A Jedis API mainly made for Bukkit plugins (can also be used for other things)

# Examples
Setup the RedisHandler:
```java
RedisHandler handler = new RedisHandler("localhost" /*ip*/, "" /*password*/, 27017 /*port*/);
handler.load();
```

Send a packet:
```java
handler.sendPacket(packet);
```

Make a packet (example with lombok in code):
```java
public class ExamplePacket extends Packet {

public String string;

public ExamplePacket(String string) {
    this.string = string;
}

@Override
public void onReceive() {
  System.out.println("Received ExamplePacket with data: " + string);
}

@Override
public void onSend() {

}
```


A more in-depth example can be found in rip/skyland/redis/example
