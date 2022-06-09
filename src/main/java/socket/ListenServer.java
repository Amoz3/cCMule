package socket;

import config.Config;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.ScriptManager;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenServer implements Runnable{
    Config config = Config.getConfig();
    private static final int PORT = 9091;
    @Override
    public void run() {
        while (ScriptManager.getScriptManager().isRunning()) {
            try (ServerSocket listner = new ServerSocket(PORT)) {
                MethodProvider.log(Color.CYAN, "[SERVER] Waiting for a mule packet...");
                Socket bot = listner.accept();

                ObjectOutputStream outStream = new ObjectOutputStream(bot.getOutputStream());
                ObjectInputStream inStream = new ObjectInputStream(bot.getInputStream());

                Packet recPacket = (Packet) inStream.readObject();
                if (recPacket != null) {
                    MethodProvider.log(Color.CYAN, "[SERVER] received - " + recPacket.getUsername() + " - " + recPacket.getWorldNum());
                    config.muleQueueAdd(recPacket);
                }
            } catch (IOException e) {
                MethodProvider.logError(e);
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
