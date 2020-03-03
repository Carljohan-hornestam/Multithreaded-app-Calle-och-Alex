package com.company;

import java.net.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkServer implements Runnable {
    public static final int PORT = 80;
    private final int SLEEP_MS = 100;
    private final int MSG_SIZE = 512;
    private DatagramSocket socket;
    static List<Observer> myObservers = new ArrayList<>();
    List<DatagramPacket> clients = new ArrayList<>();

    public NetworkServer(){
        try {
            socket = new DatagramSocket(PORT);
            socket.setSoTimeout(SLEEP_MS);
        } catch(SocketException e){ System.out.println(e.getMessage()); }
    }
    public void notifyObservers(String message){
        for (Observer observer : myObservers){
            observer.update(message);
        }
    }
    public void addObserver(Observer o){
        myObservers.add(o);
    }

    public void sendMsgToClient(String msg, SocketAddress clientSocketAddress) {
        byte[] buffer = msg.getBytes();

        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientSocketAddress);

        try { socket.send(response); }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void run() {
        AtomicBoolean isRunning = new AtomicBoolean(true);
        while (isRunning.get()) {
            DatagramPacket clientRequest = new DatagramPacket(new byte[MSG_SIZE], MSG_SIZE);

            if (!receiveMsgFromAnyClient(clientRequest)) {
                continue;
            }
            else {
                String clientMsg = new String(clientRequest.getData(), 0, clientRequest.getLength());
                clients.add(clientRequest);
                notifyObservers(clientMsg);
                System.out.println("In server, message from client is: " + clientMsg);
                for (DatagramPacket client : clients) {
                    sendMsgToClient("(Server): Message received", new InetSocketAddress(client.getAddress()
                            , client.getPort()));
                }
            }
        }
    }

    private boolean receiveMsgFromAnyClient(DatagramPacket clientRequest){
        try { socket.receive(clientRequest);
        }
        catch (Exception ex) { return false; }
        return true;
    }

}
