package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Scanner;

public class Input {
    NetworkClient client;
    private String msg;

    public Input(NetworkClient client){
        this.client = client;
    }
}
