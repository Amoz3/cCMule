import socket.Packet;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestPing {
    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 9091;

    // ping the bot to make it log in
    public static void main(String[] args) throws IOException {

        Packet testPacket = new Packet("weedman bob", 165);
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(testPacket);
        outputStream.flush();

        socket.close();
        System.exit(0);
    }
}
