package client;

import constants.ClientConstants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientThread extends Thread {
    private SocketChannel channel;
    private ByteBuffer buffer;
    private Scanner scanner;


    ClientThread(SocketChannel socketChannel) {
        channel = socketChannel;
        buffer = ByteBuffer.allocate(ClientConstants.BUFFER_SIZE);
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        String input;
        while (channel.isOpen()) {
            try {
                input = scanner.nextLine();
                if (input.equals(ClientConstants.QUIT_COMMAND)) {
                    channel.close();
                    System.out.println(ClientConstants.CLOSING_MESSAGE);
                    return;
                }
                buffer.clear();
                buffer.put(input.getBytes());
                buffer.flip();
                channel.write(buffer);
            } catch (IOException e) {
                System.out.println(ClientConstants.ERROR_OCCURED_MESSAGE +
                        System.lineSeparator() +
                        ClientConstants.CLOSING_MESSAGE);
                return;
            }
        }
    }
}
