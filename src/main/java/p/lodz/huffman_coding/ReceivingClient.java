package p.lodz.huffman_coding;

import java.io.IOException;
import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;


public class ReceivingClient implements AutoCloseable {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ObjectInputStream objectInputStream;

    private ObjectOutputStream objectOutputStream;


    private Object receivedObject;

    private byte[] receivedBytes;


    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            receivedBytes = (byte[]) receiveObject();
            receivedObject = receiveObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Object receiveObject() throws RuntimeException {
        Object object = null;
        try {
            object = objectInputStream.readObject();

            objectOutputStream.writeObject("ok");

        } catch (IOException | ClassNotFoundException e) {
            try {
                objectOutputStream.writeObject("error");
            } catch (IOException ex) {
                return null;
            }
        }
        return object;
    }

    public Object getReceivedObject() {
        return receivedObject;
    }

    public byte[] getReceivedBytes() {
        return receivedBytes;
    }

    @Override
    public void close() {
        try {
            objectInputStream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
