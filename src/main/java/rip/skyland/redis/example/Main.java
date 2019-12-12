package rip.skyland.redis.example;

import rip.skyland.redis.redis.RedisHandler;
import rip.skyland.redis.example.packets.PacketStartProgram;
import rip.skyland.redis.redis.packet.Packet;

import java.util.Scanner;

/**
 * Example class
 */
public class Main {

    private static String[] credentials = new String[] {
            "127.0.0.1",
            "",
            "6379"
    };

    public static void main(String[] args) {
        System.out.println("Please fill in your name");
        String programName = new Scanner(System.in).next();

        System.out.println();

        RedisHandler handler = new RedisHandler(credentials[0], credentials[1], "Redis", Integer.parseInt(credentials[2]));
        handler.connect();
        handler.sendPacket(new PacketStartProgram(programName));
    }
}
