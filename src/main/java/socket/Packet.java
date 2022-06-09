package socket;

public class Packet {
    private String username;
    private int worldNum;

    Packet(String username, int worldNum) {
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
