/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author lamanhhai
 */
public class SendMessage implements Runnable {
    
    private final BufferedWriter out;
    private final Socket socket;
    public SendMessage(Socket s, BufferedWriter o) {
        this.socket = s;
        this.out = o;
    }
    public void run() {
        try {
            while(true) {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                String data = stdIn.readLine();
                out.write(data+'\n');
                out.flush();
                if(data.equals("bye"))
                    break;
            }
            System.out.println("[CLIENT " + Client.myName + "]: đóng kết nối.");
            out.close();
            socket.close();
            Client.executor.shutdown();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
    
}
