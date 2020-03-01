package server;

import command.CommandProcessor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class FoodAnalyzerServer {
    private final static int PORT = 100;
    private final static String HOST = "localhost";
    private final static int BUFFER_SIZE = 1024;
    private final static int LOWER_BOUND = 0;
    private final static String CHANNEL_CLOSE_MESSAGE = "Channel already closed";
    private ByteBuffer buffer;
    private CommandProcessor commandProcessor;

    private FoodAnalyzerServer() {
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        commandProcessor = new CommandProcessor();
    }

    public static void main(String[] args) {
        new FoodAnalyzerServer().startServer();
    }


    private void startServer() {
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            channel.bind(new InetSocketAddress(HOST, PORT));
            channel.configureBlocking(false);

            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
            while (channel.isOpen()) {
                int keys = selector.select();
                if (keys <= 0) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        handleCommand(key);
                    } else if (key.isAcceptable()) {
                        connectClient(key, selector);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void connectClient(SelectionKey key, Selector selector) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = null;
        try {
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            System.out.println(CHANNEL_CLOSE_MESSAGE);
        }

    }


    private void handleCommand(SelectionKey key) throws URISyntaxException {
        SocketChannel readChannel = (SocketChannel) key.channel();
        buffer.clear();

        try {
            int readBytes = readChannel.read(buffer);
            if (readBytes <= LOWER_BOUND) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer.flip();
        String command = new String(buffer.array(), LOWER_BOUND, buffer.limit());
        String response = commandProcessor.processCommand(command);
        System.out.println(response);
    }
}