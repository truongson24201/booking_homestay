package website.booking_homestay.utils;

import java.util.Random;

public class Characters {
    public static String getStringRandom() {
        char[] pass = new char[10];
        Random rd = new Random();
        for (int i = 0; i < 10; i++) {
            pass[i] = (char) ((char) rd.nextInt(75)+48);
            while ((pass[i] > 57 && pass[i] < 65) || (pass[i] > 90 && pass[i] < 97)) {
                pass[i] = (char) ((char) rd.nextInt(75)+48);
            }
        }
        return String.copyValueOf(pass);
    }
}
