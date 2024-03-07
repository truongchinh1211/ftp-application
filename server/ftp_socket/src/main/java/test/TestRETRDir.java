/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import cipher.AESCipher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import utils.SocketUtils;

/**
 *
 * @author User
 */
public class TestRETRDir {

    public static void main(String[] args) throws Exception {
        Socket commandSocket = new Socket("localhost", 21);
        BufferedWriter commandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
        BufferedReader commandReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        String AESKey = "32mY0f2Mx8yU3m83";

        // Read welcome message
        commandReader.readLine();

        // Login
        SocketUtils.writeLineAndFlush("KEY 5AvUqDNEYLoh3oYBifCSZz7aGWeaDHS2kpnBZXycNBDBRtx9u4YXx3HJfrNctccRs6Bgno4zTyHIv9VWJYucu2h7piyM0eDlmC/sBvmFxPuxbpXLVdvslibf2n5twSj23+xIgtNEbrqtOQQ29JaJvEYBIeJwrBVN0pIVPT7Vy2Q=", commandWriter);
        commandReader.readLine();
        SocketUtils.writeLineAndFlush(AESCipher.encrypt(AESKey.getBytes(), "USER testuser"), commandWriter);
        commandReader.readLine();
        SocketUtils.writeLineAndFlush(AESCipher.encrypt(AESKey.getBytes(), "PASS test"), commandWriter);
        commandReader.readLine();

        // Open new data port
        commandWriter.write(AESCipher.encrypt(AESKey.getBytes(), "EPSV"));
        commandWriter.newLine();
        commandWriter.flush();
        String epsvResponse = AESCipher.decrypt(AESKey.getBytes(), commandReader.readLine());
        System.out.println("EPSV response: " + epsvResponse);
        int dataPort = Integer.parseInt(epsvResponse
                .replace("229 Entering Extended Passive Mode (|||", "")
                .replace("|)", ""));
        Socket dataSocket = new Socket("localhost", dataPort);
        BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream(), StandardCharsets.UTF_8));

        // Call RETR command on directory path
        commandWriter.write(AESCipher.encrypt(AESKey.getBytes(), "RETR /users/testuser"));
        commandWriter.newLine();
        commandWriter.flush();
        System.out.println(AESCipher.decrypt(AESKey.getBytes(), commandReader.readLine()));
        String data = IOUtils.toString(dataReader);

        // Remove the final new line at the end of the string
        data = data.replaceFirst("[\n\r]+$", "");

        System.out.println("RETR data: " + AESCipher.decrypt(AESKey.getBytes(), data));
        System.out.println(AESCipher.decrypt(AESKey.getBytes(), commandReader.readLine()));
    }
}
