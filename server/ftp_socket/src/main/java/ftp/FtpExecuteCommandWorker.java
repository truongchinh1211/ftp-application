/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;

import ftp.commands.Command;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public class FtpExecuteCommandWorker extends Thread {

    private final BufferedWriter commandSocketWriter;
    private final FtpServerSession session;
    private final Command command;
    private final String[] arguments;

    public FtpExecuteCommandWorker(
            Command command,
            String[] arguments,
            BufferedWriter commandSocketWriter,
            FtpServerSession session
    ) {
        this.command = command;
        this.arguments = arguments;
        this.commandSocketWriter = commandSocketWriter;
        this.session = session;
    }

    @Override
    public void run() {
        command.execute(arguments, session, commandSocketWriter);
    }

}
