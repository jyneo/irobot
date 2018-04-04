package com.arcsoft.irobot.SensorDataPackage;

import java.nio.ByteBuffer;

/**
 *
 * Created by yj2595 on 2018/4/3.
 */

public class SendData {

    public SendData() {

    }

    public byte[] head = new byte[2];
    public byte query_id;
    public int length;
    public ByteBuffer data;
}
