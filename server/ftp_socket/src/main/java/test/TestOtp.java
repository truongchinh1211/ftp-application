/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.time.Duration;
import java.time.LocalDateTime;


/**
 *
 * @author lamanhhai
 */
public class TestOtp {
    public static void main(String[] args) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // Thêm 10 phút vào currentDateTime
        LocalDateTime newDateTime = currentDateTime.plusMinutes(12);

        System.out.println("Thời gian hiện tại: " + currentDateTime);
        System.out.println("Thời gian sau khi thêm 10 phút: " + newDateTime);

        // Tính thời gian chênh lệch
        Duration duration = Duration.between(currentDateTime, newDateTime);
        long minutesDiff = duration.toMinutes(); // Chênh lệch thời gian theo phút
        long secondsDiff = duration.getSeconds(); // Chênh lệch thời gian theo giây

        System.out.println("Chênh lệch thời gian (phút): " + minutesDiff);
        System.out.println("Chênh lệch thời gian (giây): " + secondsDiff);
    }
}
