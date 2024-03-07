/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cipher;

import java.security.SecureRandom;

/**
 *
 * @author lamanhhai
 */
public class SecureRandomUtil {
    public static SecureRandom random = new SecureRandom();

	public static String getRandom(int length) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			boolean isChar = (random.nextInt(2) % 2 == 0);
			if (isChar) { 
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				ret.append((char) (choice + random.nextInt(26)));
			} else { 
				ret.append(random.nextInt(10));
			}
		}
		return ret.toString();
	}
}
