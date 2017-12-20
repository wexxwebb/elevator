package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Main {

    static List<String> usernames;
    static String names = "usernames.txt";

    public static void main(String[] args) {

        Elevator lift = new Lift(9 );
        for (int i = 1; i <= 3; i++) {
            try {
                usernames = Files.readAllLines(Paths.get(names));
            } catch (IOException e) {
                System.out.println("Can't read/open file " + names + ". Retry " + i);
                if (i == 3) {
                    System.out.println("Can't read/open file " + names + ". Exit.");
                }
            }
        }
        Random random = new Random(100);
        for (int i = 0; i < 10 ; i++) {
            User user = new User(lift, usernames.get(random.nextInt(usernames.size()) - 1), i);
        }

    }
}
