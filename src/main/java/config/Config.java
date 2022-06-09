package config;

import socket.Packet;
import sun.misc.Queue;

import java.util.LinkedList;

public class Config {
    private static Config config = new Config();
    private Config() {}
    public static Config getConfig() {
        return config;
    }
    private LinkedList<Packet> muleQueue;

    public LinkedList<Packet> getMuleQueue() {
        return muleQueue;
    }
    public void muleQueuePop() {
        muleQueue.remove(0);
    }
    public void muleQueueAdd(Packet packet) {
        muleQueue.add(packet);
    }
}
