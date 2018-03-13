package com.arcsoft.irobot.SensorDataPackage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by yj2595 on 2018/3/6.
 */

public class SensorResult implements Parcelable {
    public int id;
    public short value;

    protected SensorResult(Parcel in) {
        id = in.readInt();
        value = (short) in.readLong();
    }

    //     add Parcelable interface
    public static final Creator<SensorResult> CREATOR = new Creator<SensorResult>() {
        @Override
        public SensorResult createFromParcel(Parcel in) {
            return new SensorResult(in);
        }

        @Override
        public SensorResult[] newArray(int size) {
            return new SensorResult[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(value);
    }

    public SensorResult(int id, byte x) {
        this.id = id;
        this.value = (short) (x & 0xFF);
    }

    public SensorResult(int id, byte x, byte y) {
        this.id = id;
        this.value = (short) (((x & 0xFF) << 8) | (y & 0xFF));
    }

    public int getFinalValue() {
        SensorPacket.Info info = SensorPacket.getInfo(this.id);
        if (info.bytes == 1)
            return info.min >= 0 ? this.value & 0xFF : (byte) (this.value & 0xFF);
        else
            return info.min >= 0 ? this.value & 0xFFFF : (short) (this.value & 0xFFFF);
    }
}
