package com.company;

import java.util.concurrent.BlockingQueue;

public interface Elevator {

    void callLift(int callLevel, String username);
    void sendToTarget(int targetLevel, String username);
    Object getMonitor();
    int getLevelsAmount();

}
