package pers.fancy.cache;

import java.util.Random;


public class Utils {

    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int nextRadom() {
        return new Random().nextInt();
    }
}
