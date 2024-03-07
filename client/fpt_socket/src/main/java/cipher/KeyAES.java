/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cipher;


/**
 *
 * @author lamanhhai
 */
public class KeyAES {
    private static KeyAES instance = null;
    private byte[] key = null;
    
    private KeyAES() {
        try {
            key = SecureRandomUtil.getRandom(16).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static KeyAES getInstance() {
        if (instance == null) {
            instance = new KeyAES();
        }
        return instance;
    }
    //ceaser subttition ceaser
    //RSA
    public void clearKey() {
        key = null;
    }

    public byte[] getKey() {
        return key;
    }
    
}
