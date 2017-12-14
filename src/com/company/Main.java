package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Main {

    static List<String> usernames;

    public static void main(String[] args) {

        Elevator lift = new Lift(9 );
        try {
            usernames = Files.readAllLines(Paths.get("usernames.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Random random = new Random(100);
        for (int i = 0; i < 10 ; i++) {
            User user = new User(lift, usernames.get(random.nextInt(usernames.size()) - 1), i);
        }

    }
}
