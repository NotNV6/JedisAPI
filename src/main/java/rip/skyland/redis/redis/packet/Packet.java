package rip.skyland.redis.redis.packet;

public abstract class Packet {

    public abstract void onReceive();
    public abstract void onSend();

}
