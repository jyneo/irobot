package com.arcsoft.irobot.SensorDataPackage;

/**
 *
 * Created by yj2595 on 2018/3/6.
 */

public class SensorGroupPackage {
    private static final int kGroup_0 = 0;
    private static final int kGroup_1 = 1;
    private static final int kGroup_2 = 2;
    private static final int kGroup_3 = 3;
    private static final int kGroup_4 = 4;
    private static final int kGroup_5 = 5;
    private static final int kGroup_6 = 6;
    private static final int kGroup_100 = 100;
    private static final int kGroup_101 = 101;
    private static final int kGroup_106 = 106;
    private static final int kGroup_107 = 107;

    public static class Info {
        private int group_id;
        private int bytes;
        private int start_id;
        private int end_id;

        private Info(int gid, int bytes, int start_id, int end_id) {
            this.group_id = gid;
            this.bytes = bytes;
            this.start_id = start_id;
            this.end_id = end_id;
        }

        public int length() {
            return end_id - start_id + 1;
        }
    }

    private static final Info[] m_Infos = new Info[]{
            new Info(kGroup_0, 26, 7, 26),
            new Info(kGroup_1, 10, 7, 16),
            new Info(kGroup_2, 6, 17, 20),
            new Info(kGroup_3, 10, 21, 26),
            new Info(kGroup_4, 14, 27, 34),
            new Info(kGroup_5, 12, 35, 42),
            new Info(kGroup_6, 52, 7, 42),
            new Info(kGroup_100, 80, 7, 58),
            new Info(kGroup_101, 28, 43, 58),
            new Info(kGroup_106, 12, 46, 51),
            new Info(kGroup_107, 9, 54, 58),
    };

    public static Info getInfo(int group_id) {
        for (Info m_Info : m_Infos) {
            if (m_Info.group_id == group_id)
                return m_Info;
        }
        return null;
    }

    public static Info[] getInfos() {
        return m_Infos;
    }
}
