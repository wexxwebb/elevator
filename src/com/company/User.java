package com.company;

public class User implements Runnable {

    private Elevator lift;
    private Thread thread;

    public User(Elevator lift) {
        this.lift = lift;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lift.getMonitor()) {
                lift.callLift(15, Thread.currentThread().getName());
                try {
                    lift.getMonitor().wait();
                    lift.sendToTarget(1, Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
