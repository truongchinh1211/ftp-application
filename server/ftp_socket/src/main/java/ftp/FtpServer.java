package ftp;

import config.IPConfig;
import ftp.commands.TYPECommand;
import ftp.commands.USERCommand;
import ftp.commands.RNTOCommand;
import ftp.commands.PWDCommand;
import ftp.commands.RETRCommand;
import ftp.commands.STORCommand;
import ftp.commands.MLSDCommand;
import ftp.commands.PASSCommand;
import ftp.commands.RNFRCommand;
import ftp.commands.MKDCommand;
import ftp.commands.FEATCommand;
import ftp.commands.CWDCommand;
import ftp.commands.Command;
import ftp.commands.DELECommand;
import ftp.commands.EPSVCommand;
import ftp.commands.AUTHCommand;
import ftp.commands.CHDTCommand;
import ftp.commands.CHKSCommand;
import ftp.commands.GOTPCommand;
import ftp.commands.KEYCommand;
import ftp.commands.LSHRCommand;
import ftp.commands.LSURCommand;
import ftp.commands.PCHGCommand;
import ftp.commands.PROFCommand;
import ftp.commands.REGCommand;
import ftp.commands.RMDCommand;
import ftp.commands.SHRECommand;
import ftp.commands.SOTPCommand;
import ftp.commands.USHRCommand;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import threading.ThreadManager;

public class FtpServer {

    private Map<String, Command> commands = new HashMap<>();
    private List<Command> onSocketConnectCommands = new ArrayList<>();
    private List<Command> onSocketDisconnectCommands = new ArrayList<>();
    private ServerSocket server;

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }

    public List<Command> getOnSocketConnectCommands() {
        return onSocketConnectCommands;
    }

    public void setOnSocketConnectCommands(List<Command> onSocketConnectCommands) {
        this.onSocketConnectCommands = onSocketConnectCommands;
    }

    public List<Command> getOnSocketDisconnectCommands() {
        return onSocketDisconnectCommands;
    }

    public void setOnSocketDisconnectCommands(List<Command> onSocketDisconnectCommands) {
        this.onSocketDisconnectCommands = onSocketDisconnectCommands;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public void start() {
        try {
            IPConfig ipConfig = new IPConfig();
            ipConfig.createServerIP();
            server = new ServerSocket(21);
            System.out.println("Server started on port 21");
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                Socket socket = server.accept();

                // Create new thread for each connected user
                ThreadManager.getInstance().getExecutorService().execute(
                        new FtpSessionWorker(
                                socket,
                                onSocketConnectCommands,
                                onSocketDisconnectCommands,
                                commands
                        )
                );
            } catch (IOException ex) {
                Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        FtpServerBuilder ftpServerBuilder = new FtpServerBuilder();

        ftpServerBuilder.addOnConnectCommand(new Command() {
            @Override
            public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
                try {
                    session.getSessionSocketUtils().writeLineAndFlush("220 Service ready for new user.", commandSocketWriter);

                } catch (IOException ex) {
                    Logger.getLogger(FtpServer.class
                            .getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        ftpServerBuilder.addCommand("AUTH", new AUTHCommand());
        ftpServerBuilder.addCommand("USER", new USERCommand());
        ftpServerBuilder.addCommand("PASS", new PASSCommand());
        ftpServerBuilder.addCommand("PWD", new PWDCommand());
        ftpServerBuilder.addCommand("MLSD", new MLSDCommand());
        ftpServerBuilder.addCommand("FEAT", new FEATCommand());
        ftpServerBuilder.addCommand("TYPE", new TYPECommand());
        ftpServerBuilder.addCommand("EPSV", new EPSVCommand());
        ftpServerBuilder.addCommand("CWD", new CWDCommand());
        ftpServerBuilder.addCommand("RETR", new RETRCommand());
        ftpServerBuilder.addCommand("STOR", new STORCommand());
        ftpServerBuilder.addCommand("MKD", new MKDCommand());
        ftpServerBuilder.addCommand("DELE", new DELECommand());
        ftpServerBuilder.addCommand("RNFR", new RNFRCommand());
        ftpServerBuilder.addCommand("RNTO", new RNTOCommand());
        ftpServerBuilder.addCommand("RMD", new RMDCommand());
        ftpServerBuilder.addCommand("SHRE", new SHRECommand());
        ftpServerBuilder.addCommand("REG", new REGCommand());
        ftpServerBuilder.addCommand("GOTP", new GOTPCommand());
        ftpServerBuilder.addCommand("SOTP", new SOTPCommand());
        ftpServerBuilder.addCommand("LSHR", new LSHRCommand());
        ftpServerBuilder.addCommand("USHR", new USHRCommand());
        ftpServerBuilder.addCommand("PROF", new PROFCommand());
        ftpServerBuilder.addCommand("LSUR", new LSURCommand());
        ftpServerBuilder.addCommand("KEY", new KEYCommand());
        ftpServerBuilder.addCommand("PCHG", new PCHGCommand());
        ftpServerBuilder.addCommand("CHDT", new CHDTCommand());
        ftpServerBuilder.addCommand("CHKS", new CHKSCommand());

        ftpServerBuilder.addOnDisconnectCommand(new Command() {
            @Override
            public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
                System.out.println("Client disconnected");
            }
        });

        FtpServer ftpServer = ftpServerBuilder.build();
        ftpServer.start();
    }
}
