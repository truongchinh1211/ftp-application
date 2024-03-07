/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import ftp.SessionSocketUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.io.IOUtils;
import utils.SocketUtils;

/**
 *
 * @author User
 */
public class TestLSUR {

    public static void main(String[] args) throws IOException {
        Socket commandSocket = new Socket("localhost", 21);
        BufferedWriter commandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
        BufferedReader commandReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));

        // Read welcome message
        commandReader.readLine();

        // Login
        SocketUtils.writeLineAndFlush("USER testuser", commandWriter);
        commandReader.readLine();
        SocketUtils.writeLineAndFlush("PASS test", commandWriter);
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
        Socket dataSocket = new Socket("localhost", dataPort);
        BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

        // Call PROF command to get profile data
        commandWriter.write("LSUR /users/testuser");
        commandWriter.newLine();
        commandWriter.flush();
        System.out.println(commandReader.readLine());
        System.out.println("LSUR data: " + IOUtils.toString(dataReader));
        System.out.println(commandReader.readLine());
    }
}
