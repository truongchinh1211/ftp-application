/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author lamanhhai
 */
public class MP5Utils {
    public String getMD5Hash(String input) {
        try {
            // Khởi tạo đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Cập nhật đối tượng MessageDigest với dữ liệu từ chuỗi
            md.update(input.getBytes());

            // Tính toán giá trị băm (hash)
            byte[] hashBytes = md.digest();

            // Chuyển giá trị băm thành chuỗi hex
            BigInteger bigInt = new BigInteger(1, hashBytes);
            String md5Hash = bigInt.toString(16);

            // Đảm bảo chuỗi MD5 có đủ 32 ký tự bằng cách thêm số 0 ở đầu nếu cần
            while (md5Hash.length() < 32) {
                md5Hash = "0" + md5Hash;
            }

            return md5Hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
