package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkClient implements Runnable, Observer {
    private final String SERVER_IP = "localhost";
    private final int MSG_SIZE = 512;
    private final int SLEEP_MS = 100;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    boolean isRunning = true;

    public NetworkClient(){
        try {
            serverAddress = InetAddress.getByName(SERVER_IP);
            socket = new DatagramSocket();
            socket.setSoTimeout(SLEEP_MS);
            NetworkServer.myObservers.add(this);
        } catch(Exception e){ System.out.println("I konstruktorn: " + e.getMessage()); }
    }
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    public void sendMsgToServer(String msg) {
        byte[] buffer = msg.getBytes();
        DatagramPacket request = new DatagramPacket(buffer, buffer.length, this.serverAddress, NetworkServer.PORT);
        try {
            socket.send(request);
        } catch (Exception e) {
            System.out.println("sendMsgToServer: " +  e.getMessage());
        }
    }
    private void receiveMessageFromServer() {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(response);
            String serverMsg = new String(buffer, 0, response.getLength());
            System.out.println("In client " + serverMsg);
        } catch (Exception ex) {
            try { Thread.sleep(SLEEP_MS); }
            catch (Exception e) {
                System.out.println("Couldn't sleep");
            }
        }
    }
    @Override
    public void run() {
        while (isRunning) {
            receiveMessageFromServer();
        }
        socket.close();
    }
    @Override
    public void update(String message) {
        System.out.println("(In client) From server: " + message);
    }
}

