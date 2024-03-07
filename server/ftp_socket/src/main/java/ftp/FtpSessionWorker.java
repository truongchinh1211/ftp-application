/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;
import ftp.commands.Command;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import threading.ThreadManager;

/**
 *
 * @author User
 */
public class FtpSessionWorker extends Thread {

    private final FtpServerSession sessionData;
    private final Socket commandSocket;
    private final List<Command> onSocketConnectCommands;
    private final List<Command> onSocketDisconnectCommands;
    private final Map<String, Command> commands;

    public FtpSessionWorker(
            Socket commandSocket,
            List<Command> onSocketConnectCommands,
            List<Command> onSocketDisconnectCommands,
            Map<String, Command> commands
    ) {
        this.commandSocket = commandSocket;
        this.sessionData = new FtpServerSession(commandSocket);
        this.onSocketConnectCommands = onSocketConnectCommands;
        this.onSocketDisconnectCommands = onSocketDisconnectCommands;
        this.commands = commands;
    }

    private InputParseResult parseInput(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }

        return new InputParseResult(
                tokens.get(0),
                tokens.subList(1, tokens.size()).toArray(String[]::new)
        );
    }

    public void matchCommand(String input, BufferedWriter commandSocketWriter, FtpServerSession session) {
        System.out.println("Input: " + input);
        InputParseResult parsedInput = parseInput(input);
        Command command = commands.get(parsedInput.commandName());
        if (command != null) {
//            command.execute(parsedInput.args(), session, writer);
            ThreadManager.getInstance().getExecutorService().execute(
                    new FtpExecuteCommandWorker(
                            command,
                            parsedInput.args(),
                            commandSocketWriter,
                            session
                    )
            );
        } else {
            try {
                session.getSessionSocketUtils().writeLineAndFlush("500 Command is not recognised.", commandSocketWriter);
            } catch (IOException ex) {
                
            }
        }
    }

    @Override
    public void run() {
        try {

            String input = "";
            BufferedWriter writer = sessionData.getWriter();
            BufferedReader reader = sessionData.getReader();

            // On socket connection listeners
            onSocketConnectCommands.forEach((command) -> {
                command.execute(null, sessionData, writer);
            });

            // Read and execute commands sent from client
            while ((input = sessionData.getSessionSocketUtils().readLine(reader)) != null) {
                matchCommand(input, writer, sessionData);
            }

            // Call socket disconnection listeners
            onSocketDisconnectCommands.forEach((command) -> {
                command.execute(null, sessionData, writer);
            });
        } catch (Exception ex) {
            

        }
    }
}
