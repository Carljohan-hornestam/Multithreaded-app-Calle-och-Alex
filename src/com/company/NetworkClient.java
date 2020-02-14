package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkClient implements Runnable {
    private final String SERVER_IP = "10.152.189.247";
    private final int MSG_SIZE = 512;
    private final int SLEEP_MS = 100;
    ServerInput input;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    boolean isRunning = true;

    public NetworkClient(){
        try {
            serverAddress = InetAddress.getByName(SERVER_IP);
            socket = new DatagramSocket();
            socket.setSoTimeout(SLEEP_MS);
            run();
        } catch(Exception e){ System.out.println("I konstruktorn: " + e.getMessage()); }
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void sendMsgToServer(String msg) {
        byte[] buffer = msg.getBytes();
        DatagramPacket request = new DatagramPacket(buffer, buffer.length, this.serverAddress, NetworkServer.PORT);
        try {
            socket.send(request);
            System.out.println("Meddelande skickat!");
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
            System.out.println(serverMsg); // debugging purpose only!
            // TODO: Save the msg to a queue instead
        } catch (Exception ex) {
            try { Thread.sleep(SLEEP_MS); }
            catch (Exception e) {}
        }
    }
    @Override
    public void run() {
        input = new ServerInput(this);
        while (isRunning) {
            receiveMessageFromServer();
        }
        socket.close();
    }
}

