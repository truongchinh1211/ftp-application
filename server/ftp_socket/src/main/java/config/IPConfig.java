/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.net.Socket;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author lamanhhai
 */
public class IPConfig {
    public void createServerIP() {
        try {
            // CODE PHÍA SERVER: lấy local IP bằng cách tạo socket đến 1 website tạm
        Socket socket = new Socket("google.com", 80);
        String localIP = socket.getLocalAddress().toString().substring(1);
        // SV tự generate API tại https://retool.com/api-generator/
        String api = "https://retoolapi.dev/ThkBFo/data/1"; // Ghi vào dòng 1 trong DB
        String jsonData = "{\"ip\":\"" + localIP + "\"}";
        Jsoup.connect(api)
                .ignoreContentType(true).ignoreHttpErrors(true)
                .header("Content-Type", "application/json")
                .requestBody(jsonData)
                .method(Connection.Method.PUT).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
    
    public static void main(String[] args) {
        IPConfig config = new IPConfig();
        config.createServerIP();
    }
}
