package com.company;

import java.util.Random;

public class User implements Runnable {

    private Elevator lift;
    private Thread thread;
    private Random random;

    public User(Elevator lift) {
        this.lift = lift;
        thread = new Thread(this);
        random = new Random(42);
        thread.start();
    }

    @Override
    public void run() {
        int userLevel = random.nextInt(lift.getLevelsAmount() + 1);
        while (true) {
            synchronized (lift.getMonitor()) {
                lift.callLift(userLevel, Thread.currentThread().getName());
                try {
                    int targetLevel = random.nextInt(lift.getLevelsAmount() + 1);
                    lift.getMonitor().wait();
                    lift.sendToTarget(targetLevel, Thread.currentThread().getName());
                    lift.getMonitor().wait();
                    userLevel = targetLevel;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
