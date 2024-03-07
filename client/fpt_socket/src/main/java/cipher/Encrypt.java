/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cipher;

/**
 *
 * @author lamanhhai
 */
public class Encrypt {

    public static String encriptKey() throws Exception {
        byte[] keyAES = KeyAES.getInstance().getKey();
        System.out.println("Key: " + new String(keyAES));

        byte[] encrypt = RSACipher.encrypt(Config.SERVER_PUBLIC_KEY, keyAES);

        return new String(encrypt);
    }

    public static void main(String[] args) {
        try {
            String encrypt = Encrypt.encriptKey();
            System.out.println("Encrypt: " + encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
