/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author User
 */
public class TestRegister {

    public static void main(String[] args) throws IOException {
        Socket commandSocket = new Socket("localhost", 21);
        BufferedWriter commandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
        BufferedReader commandReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));

        // Read welcome message
        commandReader.readLine();
        
        
        // Open new data port
        commandWriter.write("EPSV");
        commandWriter.newLine();
        commandWriter.flush();
        String epsvResponse = commandReader.readLine();
        System.out.println("EPSV response: " + epsvResponse);
        int dataPort = Integer.parseInt(epsvResponse
                .replace("229 Entering Extended Passive Mode (|||", "")
                .replace("|)", ""));
        System.out.println(dataPort);
        Socket dataSocket = new Socket("localhost", dataPort);
        BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        
        // Send register info
        commandWriter.write("REG");
        commandWriter.newLine();
        commandWriter.flush();
        String regAck = commandReader.readLine();
        System.out.println("Reg ack: " + regAck);
        dataWriter.write("{\n"
                + "            \"username\": \"saudaiphat@gmail.com\",\n"
                + "            \"password\": \"123\";\n"
                + "            \"firstName\": \"Nguyễn Văn\",\n"
                + "            \"lastName\": \"C\",\n"
                + "            \"gender\": \"Nam\",\n"
                + "            \"birthday\": \"12/12/1999\"\n"
                + "        }");
        dataWriter.newLine();
        dataWriter.flush();
        dataWriter.close();

        String regResponse = commandReader.readLine();
        System.out.println("Reg response: " + regResponse);
    }
}
