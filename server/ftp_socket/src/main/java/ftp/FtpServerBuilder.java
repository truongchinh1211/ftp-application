package ftp;

import ftp.commands.Command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtpServerBuilder {
    private Map<String, Command> commands = new HashMap<>();
    private List<Command> onSocketConnectCommands = new ArrayList<>();
    private List<Command> onSocketDisconnectCommands = new ArrayList<>();
    
    
    public void addOnConnectCommand(Command command) {
        onSocketConnectCommands.add(command);
    }
    
    public void addOnDisconnectCommand(Command command) {
        onSocketDisconnectCommands.add(command);
    }
    
    public void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }
    
    public FtpServer build() {
        FtpServer ftpServer = new FtpServer();
        ftpServer.setCommands(commands);
        ftpServer.setOnSocketConnectCommands(onSocketConnectCommands);
        ftpServer.setOnSocketDisconnectCommands(onSocketDisconnectCommands);
        return ftpServer;
    }
}
