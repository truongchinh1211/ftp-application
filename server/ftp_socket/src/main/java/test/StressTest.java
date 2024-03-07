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
public class StressTest {

    public static void main(String[] args) throws IOException {
        Socket commandSocket = new Socket("localhost", 21);
        BufferedWriter commandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
        BufferedReader commandReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));

        // Read welcome message
        commandReader.readLine();

        for (int i = 0; i < 10000; i++) {
            // Login
            SocketUtils.writeLineAndFlush("USER testuser2", commandWriter);
            commandReader.readLine();
            SocketUtils.writeLineAndFlush("PASS test2", commandWriter);
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

            // Get shared files
            SocketUtils.writeLineAndFlush("LSHR", commandWriter);
            commandReader.readLine();

            // Read data
            System.out.println(IOUtils.toString(dataReader));

            dataWriter.close();
            dataReader.close();
            dataSocket.close();

            // Read closing data socket message
            System.out.println(commandReader.readLine());

            // Open new data port for RETR
            commandWriter.write("EPSV");
            commandWriter.newLine();
            commandWriter.flush();
            epsvResponse = commandReader.readLine();
            System.out.println("EPSV response: " + epsvResponse);
            dataPort = Integer.parseInt(epsvResponse
                    .replace("229 Entering Extended Passive Mode (|||", "")
                    .replace("|)", ""));
            dataSocket = new Socket("localhost", dataPort);
            dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
            dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

            // Sending file type
            // A: text
            // I: binary (media files, such as image, video...)
            SocketUtils.writeLineAndFlush("TYPE A", commandWriter);
            commandReader.readLine();

            // Retrieve file
            SocketUtils.writeLineAndFlush("RETR /testuser/aaaa/test.txt", commandWriter);

            // Start of transfer message
            System.out.println("RETR: " + commandReader.readLine());

            // File content
            System.out.println("RETR result: " + IOUtils.toString(dataReader));

            // End of transfer message
            System.out.println(commandReader.readLine());
        }
    }
}
