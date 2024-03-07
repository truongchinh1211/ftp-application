/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import cipher.AESCipher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.io.FileUtils;
import utils.SocketUtils;

/**
 *
 * @author User
 */
public class TestSTOR {

    public static void main(String[] args) throws IOException, Exception {
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

        // --- STOR text file ---
        String fileContent = "hello world";

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

        // TYPE A
        commandWriter.write(AESCipher.encrypt(AESKey.getBytes(), "TYPE A"));
        commandWriter.newLine();
        commandWriter.flush();
        commandReader.readLine();

        // STOR
        commandWriter.write(AESCipher.encrypt(AESKey.getBytes(), "STOR /users/testuser/some/directory/with/name/1234.txt"));
        commandWriter.newLine();
        commandWriter.flush();
        dataWriter.append(AESCipher.encrypt(AESKey.getBytes(), fileContent));
        dataWriter.newLine();
        dataWriter.close();
        System.out.println(AESCipher.decrypt(AESKey.getBytes(), commandReader.readLine()));

        // --- STOR media file (image, video) ---
        File imageFile = new File("bai3_3119410300_saudaiphat.png");

        // Open new data port
        commandWriter.write(AESCipher.encrypt(AESKey.getBytes(), "EPSV"));
        commandWriter.newLine();
        commandWriter.flush();
        epsvResponse = AESCipher.decrypt(AESKey.getBytes(), commandReader.readLine());
        System.out.println("EPSV response: " + epsvResponse);
        dataPort = Integer.parseInt(epsvResponse
                .replace("229 Entering Extended Passive Mode (|||", "")
                .replace("|)", ""));
        dataSocket = new Socket("localhost", dataPort);
        dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream(), StandardCharsets.UTF_8));

        commandWriter.write(AESCipher.encrypt(AESKey.getBytes(), "TYPE I"));
        commandWriter.newLine();
        commandWriter.flush();
        commandReader.readLine();

        commandWriter.write(AESCipher.encrypt(AESKey.getBytes(), "STOR /users/testuser/image2.png"));
        commandWriter.newLine();
        commandWriter.flush();
        
        byte[] readFileToByteArray = FileUtils.readFileToByteArray(imageFile);
        
        String enc = AESCipher.encrypt(AESKey.getBytes(), Base64.getEncoder().encodeToString(readFileToByteArray));
//        String dec = AESCipher.decrypt(AESKey.getBytes(), AESCipher.encrypt(AESKey.getBytes(), Base64.getEncoder().encodeToString(readFileToByteArray)));
//        System.out.println("DEC:" + dec);
//        FileUtils.writeByteArrayToFile(new File("image.png"), Base64.getDecoder().decode(dec));
        
        dataWriter.append(enc);
        dataWriter.newLine();
        dataWriter.close();
        System.out.println(AESCipher.decrypt(AESKey.getBytes(), commandReader.readLine()));

    }
}
