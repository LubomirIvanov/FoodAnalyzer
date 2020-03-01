package client;

import constants.ClientConstants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client extends Thread {

    public static void main(String[] args) {
        new Client().start();
    }

    @Override
    public void run() {
        try (SocketChannel channel = SocketChannel.open()) {
            channel.connect(new InetSocketAddress(
                    ClientConstants.HOST,
                    ClientConstants.SERVER_PORT
            ));
            channel.configureBlocking(false);

            Thread clientThread = new ClientThread(channel);
            clientThread.start();
            clientThread.join();
        } catch (IOException | InterruptedException e) {
            System.out.println(ClientConstants.COULD_NOT_CONNECT_MESSAGE);
            System.out.println(ClientConstants.STOPPING_THREAD_MESSAGE);
        }
    }
}