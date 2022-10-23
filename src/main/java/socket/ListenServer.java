package socket;

import config.Config;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.util.Packet;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenServer implements Runnable {
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
                    if (recPacket.getUsername().equals("weedman bob")) {
                        MethodProvider.log(Color.CYAN, "killing server, cya lata mon");
                        break;
                    }
                    config.muleQueueAdd(recPacket);
                }
                Packet sendPacket = new Packet(Players.localPlayer().getName(), 0);
                outStream.writeObject(sendPacket);
                outStream.flush();
                MethodProvider.log(Color.CYAN, "[SERVER] sending our username to bot...");
            } catch (IOException e) {
                MethodProvider.logError(e);
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                MethodProvider.log(e);
                throw new RuntimeException(e);
            }
        }
    }
}
