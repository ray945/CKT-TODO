package com.ckt.ckttodo.database;

/**
 * Created by ckt on 3/19/17.
 */

public class ActTime {

    private int seconds;
    private float radians;

    public ActTime(int seconds, float radians) {
        this.seconds = seconds;
        this.radians = radians;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public float getRadians() {
        return radians;
    }

    public void setRadians(float radians) {
        this.radians = radians;
    }
}
