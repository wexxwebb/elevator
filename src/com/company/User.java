package com.company;

import java.util.Random;

public class User implements Runnable {

    private Elevator lift;
    private Thread thread;
    private Random random;
    private String username;

    public User(Elevator lift, String username, int seed) {
        this.lift = lift;
        this.username = username;
        thread = new Thread(this);
        random = new Random();
        thread.start();
    }

    @Override
    public void run() {
        int userLevel = random.nextInt(lift.getLevelsAmount() + 1);
        while (true) {
            synchronized (lift.getMonitor()) {
                lift.callLift(userLevel, username);
                try {
                    int targetLevel = random.nextInt(lift.getLevelsAmount() + 1);
                    lift.sendToTarget(targetLevel, username);
                    lift.getMonitor().wait();
                    userLevel = targetLevel;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
