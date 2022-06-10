import config.Config;
import org.dreambot.api.Client;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.trade.Trade;
import org.dreambot.api.methods.trade.TradeUser;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.Player;
import socket.ListenServer;
import socket.Packet;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@ScriptManifest(category = Category.MISC, name = "cCMule", author = "camalCase", version = 1.01)
public class Main extends AbstractScript {
    Config config = Config.getConfig();
    @Override
    public void onStart() {
        ListenServer listenServer = new ListenServer();
        Thread t1 = new Thread(listenServer);
        t1.start();

        getRandomManager().disableSolver(RandomEvent.LOGIN);
    }

    @Override
    public int onLoop() {
        if (!config.getMuleQueue().isEmpty() && config.getMuleQueue().get(0) != null) {
            if (!Client.isLoggedIn()) {
                getRandomManager().enableSolver(RandomEvent.LOGIN);
                MethodProvider.sleepUntil(Client::isLoggedIn, 45000);
                return 1000;
            }
            Packet currentPacket = config.getMuleQueue().get(0);
            if (Worlds.getCurrentWorld() != currentPacket.getWorldNum()) {
                WorldHopper.hopWorld(currentPacket.getWorldNum());
                MethodProvider.sleepUntil(() -> Worlds.getCurrentWorld() == currentPacket.getWorldNum(), 30000);
                return 1000;
            }
            Player bot = Players.closest(currentPacket.getUsername());
            if (Client.isLoggedIn() && bot != null && !Trade.isOpen()) {
                Trade.tradeWithPlayer(currentPacket.getUsername());
                MethodProvider.sleepUntil(Trade::isOpen, 25000);
                return 1000;
            }
            if (Trade.isOpen() && Trade.hasAcceptedTrade(TradeUser.THEM)) {
                Trade.acceptTrade();
                log(Color.YELLOW, "TRADE ACCEPTED :D, popping - " + currentPacket.getUsername() + " from queue");
                config.muleQueuePop();
                return 1000;
            }
        }
        return 1000;
    }

    @Override
    public void onExit() {
        // this is to make sure the server closes down
        callMule();
    }

    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 9091;
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
