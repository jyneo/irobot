package com.arcsoft.irobot.SensorDataPackage;

/**
 *
 * Created by yj2595 on 2018/3/6.
 */

public class SensorPacket {

    public static final int kBumpsAndWheelDrops = 7;
    public static final int kWall = 8;

    public static final int kCliffLeft = 9;
    public static final int kCliffFrontLeft = 10;
    public static final int kCliffFrontRight = 11;
    public static final int kCliffRight = 12;

    public static final int kVirtualWall = 13;
    public static final int kWheelOvercurrents = 14;

    public static final int kDirtDetect = 15;
    public static final int kUnused1 = 16; // always 0
    public static final int kUnused2 = 32; // always 0
    public static final int kUnused3 = 33; // always 0

    public static final int kInfraredCharacterOmni = 17;
    public static final int kInfraredCharacterLeft = 52;
    public static final int kInfraredCharacterRight = 53;

    public static final int kButtons = 18;
    public static final int kDistance = 19;
    public static final int kAngle = 20;

    public static final int kChargingState = 21;
    public static final int kVoltage = 22;
    public static final int kCurrent = 23;
    public static final int kTemperature = 24;
    public static final int kBatteryCharge = 25;
    public static final int kBatteryCapacity = 26;

    public static final int kWallSignal = 27;
    public static final int kCliffLeftSignal = 28;
    public static final int kCliffFrontLeftSignal = 29;
    public static final int kCliffFrontRightSignal = 30;
    public static final int kCliffRightSignal = 31;

    public static final int kChargingAvailable = 34;

    public static final int kOIMode = 35;

    public static final int kSongNumber = 36;
    public static final int kSongPlaying = 37;

    public static final int kNumberOfStreamPackets = 38;
    public static final int kRequestedVelocity = 39;
    public static final int kRequestedRadius = 40;
    public static final int kRequestedRightVelocity = 41;
    public static final int kRequestedLeftVelocity = 42;
    public static final int kLeftEncoderCounts = 43;
    public static final int kRightEncoderCounts = 44;

    public static final int kLightBumper = 45;
    public static final int kLightBumperLeftSignal = 46;
    public static final int kLightBumperFrontLeftSignal = 47;
    public static final int kLightBumperCenterLeftSignal = 48;
    public static final int kLightBumperCenterRightSignal = 49;
    public static final int kLightBumperFrontRightSignal = 50;
    public static final int kLightBumperRightSignal = 51;

    public static final int kLeftMoterCurrent = 54;
    public static final int kRightMoterCurrent = 55;
    public static final int kMainBrushMoterCurrent = 56;
    public static final int kSideBrushMoterCurrent = 57;
    public static final int kStasis = 58; // 1 when making forward progress

    public static final int kMin = 7;
    public static final int kMax = 58;
    public static final int kNum = kMax - kMin + 1;

    public static class Info {
        public int id;
        public String name;
        public int bytes;
        public int min;
        public int max;

        public Info(int id, String name, int bytes, int min, int max) {
            this.id = id;
            this.name = name;
            this.bytes = bytes;
            this.min = min;
            this.max = max;
        }
    }

    private static final Info[] m_Infos = new Info[] {
            new Info(kBumpsAndWheelDrops, "Bumps and Wheel Drops", 1, 0, 15),
            new Info(kWall, "Wall", 1, 0, 1),
            new Info(kCliffLeft, "Cliff Left", 1, 0, 1),
            new Info(kCliffFrontLeft, "Cliff Front Left", 1, 0, 1),
            new Info(kCliffFrontRight, "Cliff Front Right", 1, 0, 1),
            new Info(kCliffRight, "Cliff Right", 1, 0, 1),
            new Info(kVirtualWall, "Virtual Wall", 1, 0, 1),
            new Info(kWheelOvercurrents, "Wheel Overcurrents", 1, 0, 29),
            new Info(kDirtDetect, "Dirt Detect", 1, 0, 255),
            new Info(kUnused1, "Unused 1", 1, 0, 255),
            new Info(kInfraredCharacterOmni, "Infrared Character Omni", 1, 0, 255),
            new Info(kButtons, "Buttons", 1, 0, 255),
            new Info(kDistance, "Distance (mm)", 2, -32768, 32767),
            new Info(kAngle, "Angle (degree)", 2, -32768, 32767),
            new Info(kChargingState, "Charging State", 1, 0, 6),
            new Info(kVoltage, "Voltage (mV)", 2, 0, 65535),
            new Info(kCurrent, "Current (mA)", 2, -32768, 32767),
            new Info(kTemperature, "Temperature (deg C)", 1, -128, 127),
            new Info(kBatteryCharge, "Battery Charge (mAh)", 2, 0, 65535),
            new Info(kBatteryCapacity, "Battery Capacity (mAh)", 2, 0, 65535),
            new Info(kWallSignal, "Wall Signal", 2, 0, 1023),
            new Info(kCliffLeftSignal, "Cliff Left Signal", 2, 0, 4095),
            new Info(kCliffFrontLeftSignal, "Cliff Front Left Signal", 2, 0, 4095),
            new Info(kCliffFrontRightSignal, "Cliff Front Right Signal", 2, 0, 4095),
            new Info(kCliffRightSignal, "Cliff Right Signal", 2, 0, 4095),
            new Info(kUnused2, "Unused 2", 1, 0, 255),
            new Info(kUnused3, "Unused 3", 2, 0, 65535),
            new Info(kChargingAvailable, "Charging Available", 1, 0, 3),
            new Info(kOIMode, "Open Interface Mode", 1, 0, 3),
            new Info(kSongNumber, "Song Number", 1, 0, 4),
            new Info(kSongPlaying, "Song Playing", 1, 0, 1),
            new Info(kNumberOfStreamPackets, "Number of Stream Packages", 1, 0, 108),
            new Info(kRequestedVelocity, "Requested Velocity (mm/s)", 2, -500, 500),
            new Info(kRequestedRadius, "Requested Radius (mm)", 2, -32768, 32767),
            new Info(kRequestedRightVelocity, "Requested Right Velocity (mm/s)", 2, -500, 500),
            new Info(kRequestedLeftVelocity, "Requested Left Velocity (mm/s)", 2, -500, 500),
            new Info(kLeftEncoderCounts, "Left Encoder Counts", 2, 0, 65535),
            new Info(kRightEncoderCounts, "Right Encoder Counts", 2, 0, 65535),
            new Info(kLightBumper, "Light Bumper", 1, 0, 127),
            new Info(kLightBumperLeftSignal, "Light Bumper Left Signal", 2, 0, 4095),
            new Info(kLightBumperFrontLeftSignal, "Light Bumper Front Left Signal", 2, 0, 4095),
            new Info(kLightBumperCenterLeftSignal, "Light Bumper Center Left Signal", 2, 0, 4095),
            new Info(kLightBumperCenterRightSignal, "Light Bumper Center Right Signal", 2, 0, 4095),
            new Info(kLightBumperFrontRightSignal, "Light Bumper Front Right Signal", 2, 0, 4095),
            new Info(kLightBumperRightSignal, "Light Bumper Right Signal", 2, 0, 4095),
            new Info(kInfraredCharacterLeft, "Infrared Character Left", 1, 0, 255),
            new Info(kInfraredCharacterRight, "Infrared Character Right", 1, 0, 255),
            new Info(kLeftMoterCurrent, "Left Motor Current (mA)", 2, -32768, 32767),
            new Info(kRightMoterCurrent, "Right Motor Current (mA)", 2, -32768, 32767),
            new Info(kMainBrushMoterCurrent, "Main Brush Motor Current (mA)", 2, -32768, 32767),
            new Info(kSideBrushMoterCurrent, "Side Brush Motor Current (mA)", 2, -32768, 32767),
            new Info(kStasis, "Stasis", 1, 0, 1), // 0~3?
    };

    public static Info getInfo(int id) {
        if (id >= kMin && id <= kMax)
            return m_Infos[id - kMin];
        else
            return null;
    }

    public static Info[] getInfos() {
        return m_Infos;
    }
}
