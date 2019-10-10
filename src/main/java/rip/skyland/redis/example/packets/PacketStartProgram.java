package rip.skyland.redis.example.packets;

import lombok.AllArgsConstructor;
import rip.skyland.redis.redis.packet.Packet;

@AllArgsConstructor
public class PacketStartProgram extends Packet {

    public String program;

    public void onReceive() {
        System.out.println("Started " + program);
    }

    public void onSend() { }
}
