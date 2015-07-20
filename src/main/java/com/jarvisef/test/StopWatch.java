package com.jarvisef.test;

/**
 * Created by youngsunkr on 2015-07-21.
 */
public class StopWatch {
    private long startTime;
    private long elapsedTime = 0;
    private StringBuffer currentName;
    private boolean threadFlag = false;


    public StopWatch() {
        currentName = new StringBuffer();
        startTime = System.nanoTime();
    }

    public StopWatch(boolean threadFlag) {
        changeMessage("", threadFlag, true);
    }

    public StopWatch(String message) {
        changeMessage(message, false, true);
    }

    public StopWatch(String message, boolean threadFlag) {
        changeMessage(message, threadFlag, true);
    }

    public void start() {
        startTime = System.nanoTime();
        elapsedTime = 0;
    }

    public void stop() {
        elapsedTime = System.nanoTime() - startTime;
    }

    public void changeMessage(String message, boolean threadFlag, boolean resetFlag) {
        StringBuffer threadName = new StringBuffer();
        this.threadFlag = threadFlag;

        if(threadFlag) {
            threadName.append("ThreadName = ").append(Thread.currentThread().getName());
        }

        this.currentName.append("[").append(message).append(threadName).append("] ");

        if(resetFlag) {
            start();
        }
    }

    public double getElapsedMS() {
        if (elapsedTime == 0 )
            stop();
        return elapsedTime/1000000.0;
    }

    public double getElapsedNano() {
        if (elapsedTime == 0 )
            stop();
        return elapsedTime;
    }

    public String toString() {
        if (elapsedTime == 0)
            stop();
        currentName.append("elapsed Time : ").append(elapsedTime/1000000.0).append("ms");
        return currentName.toString();
    }
}