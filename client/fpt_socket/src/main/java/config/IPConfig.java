/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author lamanhhai
 */
public class IPConfig {

    public String getIPServer() {
        String ipServer = "";
        try {
            String api = "https://retoolapi.dev/ThkBFo/data/1";
            Document doc = Jsoup.connect(api)
                    .ignoreContentType(true).ignoreHttpErrors(true)
                    .header("Content-Type", "application/json")
                    .method(Connection.Method.GET).execute().parse();
            JSONObject jsonObject = new JSONObject(doc.text());
            ipServer = (String) jsonObject.get("ip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ipServer;
    }
    
    public static void main(String[] args) {
        IPConfig config = new IPConfig();
        String ip = config.getIPServer();
        System.out.println("Kiem tra: " + ip);
    }
}
