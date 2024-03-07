/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import java.security.SecureRandom;

/**
 *
 * @author lamanhhai
 */
public class Test {
    public static void main(String[] args) {
        // Bước 1: Tạo mã OTP
        String otp = generateOTP();

        // Bước 2: Gửi mã OTP qua email
        sendEmail("nguoidung@example.com", otp);

        // Bước 3: Người dùng nhập và kiểm tra mã OTP

        // Bước 4: Kiểm tra tính hợp lệ của mã OTP

        // Bước 5: Kích hoạt tài khoản
        
        
    }

    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otpValue = 100000 + random.nextInt(900000); // Sinh mã OTP 6 chữ số
        return String.valueOf(otpValue);
    }

    public static void sendEmail(String email, String otp) {
        // Gửi email chứa mã OTP đến địa chỉ email
        // Sử dụng JavaMail API hoặc thư viện email tương tự
    }
}
