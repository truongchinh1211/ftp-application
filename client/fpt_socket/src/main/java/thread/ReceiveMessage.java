/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author lamanhhai
 */
public class ReceiveMessage implements Runnable {
    private final BufferedReader in;
    private Socket socket;
    public ReceiveMessage(Socket s, BufferedReader i) {
        this.socket = s;
        this.in = i;
    }
    public void run() {
        try {
            while(true) {
                String data = in.readLine();
                if(data==null || data.equals("bye"))
                    break;
                if(data.startsWith("#")) {
                    System.out.println("[CLIENT]: đã kết nối");
                    continue;
                }
                System.out.println(data);
            }
            Client.executor.shutdown();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
