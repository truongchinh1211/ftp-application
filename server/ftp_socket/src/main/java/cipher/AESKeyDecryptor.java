/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cipher;

/**
 *
 * @author lamanhhai
 */
public class AESKeyDecryptor {
    public static String decryptKey(String encrypt) throws Exception {
        byte[] decrypt = RSACipher.decrypt(Config.SERVER_PRIVATE_KEY, encrypt.getBytes());
        return new String(decrypt);
    }
}
