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
public class TestGenerateOTP {

    public static void main(String[] args) throws IOException {
        Socket commandSocket = new Socket("localhost", 21);
        BufferedWriter commandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
        BufferedReader commandReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));

        // Read welcome message
        commandReader.readLine();
        
        // Generate OTP
        commandWriter.write("GOTP lahai7744@gmail.com 123");
        commandWriter.newLine();
        commandWriter.flush();

        String gotpResponse = commandReader.readLine();
        System.out.println("GOTP response: " + gotpResponse);
    }
}
