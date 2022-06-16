package org.dreambot.util;

import java.io.Serializable;

public class Packet implements Serializable {
    private String username;
    private int worldNum;

    public Packet(String username, int worldNum) {
        this.username = username;
        this.worldNum = worldNum;
    }

    public String getUsername() {
        return username;
    }

    public int getWorldNum() {
        return worldNum;
    }
}
