package p.lodz.huffman_coding;

import java.io.*;
import java.net.Socket;

public class SendingClient implements AutoCloseable{


    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    public void start(int port, String ip){
        try {
            clientSocket = new Socket(ip,port);
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    public String sendObject(Object object){
        try {
            objectOutputStream.writeObject(object);
            return (String)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            objectOutputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
