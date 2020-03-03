package com.company;

import java.util.Scanner;

public class Program implements Observer {

    private String message;
    private NetworkClient client;
    NetworkServer server = new NetworkServer();

    public Program() {
        server.addObserver(this);
        Thread thread1 = new Thread(server, "server");
        thread1.start();
        thread1.setPriority(10);

        client = new NetworkClient();
        Thread thread2 = new Thread(client, "klient");
        thread2.start();
        inputMsg();
    }
    public void inputMsg() {
        boolean isLooping = true;
        while (isLooping) {
            System.out.println("Enter message: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextLine()) {
                message = scan.nextLine();
            }
            else {
                isLooping = false;
            }
            if (message.equals("STOPP")) {
                client.setIsRunning(false);
                isLooping = false;
            } else {
                client.sendMsgToServer(message);
            }
        }
    }

    @Override
    public void update(String message) {
        System.out.println("In (Program) From server " + message);
    }
}
