package com.company;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Lift implements Runnable, Elevator {

    @Override
    public int getLevelsAmount() {
        return LEVELS_AMOUNT;
    }

    @Override
    public void callLift(int callLevel, String username) {
        try {
            getQueue().put(new Task(callLevel, username));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendToTarget(int targetLevel, String username) {
        try {
            getQueue().put(new Task(targetLevel, username));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getMonitor() {
        return queue;
    }

    private class Task {
        private boolean done;
        private int target;
        private String username;

        public void setDone(boolean done) {
            this.done = done;
        }

        private boolean isDone() {
            return done;
        }

        private Task(int target, String username) {
            this.done = false;
            this.target = target;
            this.username = username;
        }

        private Task() {
            this.target = 1;
            this.done = true;
        }
    }

    private Task task;
    private BlockingQueue queue;
    private volatile int level;
    private final int LEVELS_AMOUNT;

    public Lift(int levelsAmount) {
        this.task = new Task();
        this.queue = new ArrayBlockingQueue(100);
        this.level = 1;
        this.LEVELS_AMOUNT = levelsAmount;
        checkTarget();
        Thread thread = new Thread(this);
        thread.start();
    }

    public BlockingQueue<Task> getQueue() {
        return queue;
    }

    private void getTask() {
        if (task.isDone() && queue.peek() != null) {
            task = (Task) queue.poll();
        }
    }

    private boolean checkTarget() {
        if (level == task.target) {
            System.out.println("Lift STOP on level " + level + " (" + task.username + ")");
            task.setDone(true);
            return true;
        } else {
            System.out.println("Lift on level " + level + " (" + task.username + ")");
            return false;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (queue) {
                if (!task.isDone()) {
                    if (level < task.target) {
                        level++;
                    }
                    if (level > task.target) {
                        level--;
                    }
                    if (checkTarget()) queue.notifyAll();
                } else {
                    getTask();
                }
            }
        }
    }
}
