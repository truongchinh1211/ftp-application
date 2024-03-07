/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socket;

/**
 *
 * @author User
 */
public class StatusCode {

    public static final int ABOUT_TO_OPEN_DATA_CONNECTION = 150;
    public static final int COMMAND_OK = 200;
    public static final int CLOSING_DATA_CONNECTION = 226;
    public static final int FILE_ACTION_OK = 250;
    public static final int USERNAME_OK = 331;
    public static final int FILE_ACTION_REQUIRES_INFO = 350;
    public static final int COMMAND_UNRECOGNIZED = 500;
    public static final int SYNTAX_ERROR = 500;
    public static final int SECURITY_METHOD_UNAVAILABLE = 431;
    public static final int NOT_LOGGED_IN = 530;
    public static final int LOGGED_IN = 230;
    public static final int DIRECTORY_CREATED = 257;
    public static final int CURRENT_WORKING_DIRECTORY = 257;
    public static final int ENTERED_EXTENDED_PASSIVE_MODE = 229;
    public static final int SYSTEM_HELP_REPLY = 211;
    public static final int FILE_ACTION_NOT_TAKEN = 450;
    public static final int ACTION_FAILED = 450;
    public static final int OTP_NEEDED = 336;
}
