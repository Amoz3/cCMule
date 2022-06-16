import org.dreambot.util.Packet;

import java.io.*;
import java.net.Socket;

public class TestPing {
    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 9091;

    // ping the bot to make it log in
    public static void main(String[] args) throws IOException {
        System.out.println(callMule());
    }

    private static String callMule() {
        Packet testPacket = new Packet("weedman bob", 165);
        try (Socket socket = new Socket(SERVER_IP,SERVER_PORT)) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(testPacket);
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Packet responsePacket = (Packet) inputStream.readObject();
            System.out.println(responsePacket.getUsername());
            socket.close();
            return responsePacket.getUsername();
        } catch(IOException e){
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
