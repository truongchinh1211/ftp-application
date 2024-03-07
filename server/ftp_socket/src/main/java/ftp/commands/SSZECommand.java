///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package ftp.commands;
//
//import bus.UserBus;
//import ftp.FtpServerSession;
//import ftp.SocketUtils;
//import ftp.StatusCode;
//import ftp.commands.Command;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import payload.UserFileLimits;
//
///**
// *
// * @author User
// */
//public class SSZECommand implements Command {
//
//    private final UserBus userBus = new UserBus();
//    
//    public Integer readStringAsInteger(String string) {
//        try {
//            return Integer.valueOf(string);
//        } catch(NumberFormatException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
//        Integer quotaKb = Integer.valueOf(arguments[0]);
//        Integer maxUploadFileSizeKb = Integer.valueOf(arguments[1]);
//        Integer maxDownloadFileSizeKb = Integer.valueOf(arguments[2]);
//        String setFileLimitsResponse = userBus.setFileLimits(
//                session.getUsername(),
//                new UserFileLimits(quotaKb, maxDownloadFileSizeKb, maxUploadFileSizeKb)
//        );
//        
//        if (setFileLimitsResponse.equals(UserBus.SET_FILE_LIMITS_SUCCESSFULLY)) {
//            try {
//                session.getSocketUtils().respondCommandSocket(
//                        StatusCode.FILE_ACTION_OK,
//                        UserBus.SET_FILE_LIMITS_SUCCESSFULLY,
//                        commandSocketWriter
//                );
//            } catch (IOException ex) {
//                Logger.getLogger(SSZECommand.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            try {
//                session.getSocketUtils().respondCommandSocket(
//                        StatusCode.FILE_ACTION_NOT_TAKEN,
//                        setFileLimitsResponse,
//                        commandSocketWriter
//                );
//            } catch (IOException ex) {
//                Logger.getLogger(SSZECommand.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//}
