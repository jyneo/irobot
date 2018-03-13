package com.arcsoft.irobot.interfaces;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public interface IRobot {
//    Create2 getCreate2();
    boolean setWheelSpeed(final int speedL, final int speedR);
    boolean setWheelSpeed(final int speedL, final int speedR, boolean force);
    void setRequiredVelocity(final int velocity);
    int getRequiredVelocity();
}
