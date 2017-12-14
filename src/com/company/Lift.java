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
            getQueue().put(new Task(callLevel, username, TaskType.CALL));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendToTarget(int targetLevel, String username) {
        try {
            getQueue().put(new Task(targetLevel, username, TaskType.TARGET));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getMonitor() {
        return queue;
    }

    private enum TaskType {
        CALL, TARGET
    }

    private class Task {
        private int target;
        private String username;
        private TaskType type;

        private Task(int target, String username, TaskType type) {
            this.target = target;
            this.username = username;
            this.type = type;
        }

        private Task() {
            this.target = 1;
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
        if (task.target == level && queue.peek() != null) {
            task = (Task) queue.poll();
        }
        if (task.target == level) queue.notify();
    }

    private boolean checkTarget() {
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";
        if (level == task.target) {
            System.out.println(ANSI_RED + "Lift STOP on level " + level + " (" + task.username + ", " + task.type + ")" + ANSI_RESET);
            return true;
        } else {
            System.out.println("Lift on level " + level + " (" + task.username + ", " + task.type + ")");
            return false;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (queue) {
                if (task.target != level) {
                    if (level < task.target) {
                        level++;
                    }
                    if (level > task.target) {
                        level--;
                    }
                    if (checkTarget()) queue.notify();
                } else {
                    getTask();
                }
            }
        }
    }
}
