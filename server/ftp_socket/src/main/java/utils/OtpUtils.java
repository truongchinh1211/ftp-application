/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.SplittableRandom;

/**
 *
 * @author lamanhhai
 */
public class OtpUtils {

    public String generateOtp() {
        SplittableRandom random = new SplittableRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(0, 9));
        }

        return sb.toString();
    }
}
