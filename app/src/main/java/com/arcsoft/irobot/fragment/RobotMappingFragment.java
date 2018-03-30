package com.arcsoft.irobot.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

/**
 *
 * Created by yj2595 on 2018/3/22.
 */

public class RobotMappingFragment extends Fragment {

    private static final String TAG = "RobotMappingFragment";
    private Create2 create2;
    private View rootView;
    private PhotoView photoView;
    private PhotoViewAttacher attacher;

    public RobotMappingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_robot_mapping, container, false);
        photoView = (PhotoView) rootView.findViewById(R.id.photo_view);
        attacher = new PhotoViewAttacher(photoView);
        create2 = new Create2();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        create2.mappingStream(mMappingStreamCallback);
        Log.d(TAG, "onActivityCreated: " + mMappingStreamCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update status when navigating back
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public final Create2.MappingStreamCallback mMappingStreamCallback = new Create2.MappingStreamCallback() {
        @Override
        public void onMappingStream(byte[] results) {

//            int width = 300;
//            int height = 300;
//            Random random = new Random();
//            byte[] data = new byte[90000];
//            int[] colors = new int[90000];
//            for (int i = 0; i < 90000; i++) {
//                data[i] = (byte) random.nextInt(4);
//            }
//            int count = 0;
//            for (int i = 0; i < height; i++) {
//                for (int j = 0; j < width; j++) {
//                    if (data[count] == 0) {
//                        colors[count] = (128 << 16) | (128 << 8) | 128 | 0xFF000000;
//                    } else if (data[count] == 1) {
//                        colors[count] = (255 << 16) | (127 << 8) | 80 | 0xFF000000;
//                    } else if (data[count] == 2) {
//                        colors[count] = (128 << 8) | 0xFF000000;
//                    } else {
//                        colors[count] = (255 << 16) | (255 << 8) | 255 | 0xFF000000;
//                    }
//                    count++;
//                }
//            }

            int width = (results[2] << 24 & 0xFF) + (results[3] << 16 & 0xFF) + (results[4] << 8 & 0xFF) + (results[5] & 0xFF);
            int height = (results[6] << 24 & 0xFF) + (results[7] << 16 & 0xFF) + (results[8] << 8 & 0xFF) + (results[9] & 0xFF);
            Log.d(TAG, "onMappingStream: " + width + " " + height);

            int[] colors = new int[width * height];
            int count = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (results[count + 10] == 0) {
                        colors[count] = (128 << 16) | (128 << 8) | 128 | 0xFF000000;
                    } else if (results[count + 10] == 1) {
                        colors[count] = (255 << 16) | (127 << 8) | 80 | 0xFF000000;
                    } else if (results[count + 10] == 2) {
                        colors[count] = (128 << 8) | 0xFF000000;
                    } else {
                        colors[count] = (255 << 16) | (255 << 8) | 255 | 0xFF000000;
                    }
                    count++;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(colors, 0, width, width, height, Bitmap.Config.ARGB_8888);
            photoView.setImageBitmap(bitmap);
        }
    };

    // 将纯RGB数据数组转化成int像素数组
    public int[] convertByteToColor(byte[] data){
        int size = data.length;
        if (size == 0){
            return null;
        }

        int arg = 0;
        if (size % 3 != 0){
            arg = 1;
        }

        // 一般情况下data数组的长度应该是3的倍数，这里做个兼容，多余的RGB数据用黑色0XFF000000填充
        int[] color = new int[size / 3 + arg];
        int red, green, blue;

        if (arg == 0) {
            for(int i = 0; i < color.length; ++i){
                red = convertByteToInt(data[i * 3]);
                green = convertByteToInt(data[i * 3 + 1]);
                blue = convertByteToInt(data[i * 3 + 2]);

                // 获取RGB分量值通过按位或生成int的像素值
                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }
        } else{
            for(int i = 0; i < color.length - 1; ++i){
                red = convertByteToInt(data[i * 3]);
                green = convertByteToInt(data[i * 3 + 1]);
                blue = convertByteToInt(data[i * 3 + 2]);
                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }

            color[color.length - 1] = 0xFF000000;
        }

        return color;
    }

    // 将一个byte数转成int, 实现这个函数的目的是为了将byte数当成无符号的变量去转化成int
    public int convertByteToInt(byte data){
        int heightBit = (data >> 4) & 0x0F;
        int lowBit = 0x0F & data;
        return heightBit * 16 + lowBit;
    }

    public Bitmap getTranslateImage(Bitmap bitmap , int alpha) { // alpha: 0 - 255
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bitmap_new = Bitmap.createBitmap(w , h , Bitmap.Config.ARGB_8888);
        for(int i = 0 ; i < h ; i++) {
            for(int j = 0 ; j < w; j ++) {
                int argb = bitmap.getPixel(j , i );
                int r = (argb>>16) & 0xff;
                int g = (argb>>8) & 0xff;
                int b = argb & 0xff;
                int a = (argb>>24) & 0xff;
                int rgb =((a * 256 + r) * 256 + g) * 256 + b;
                bitmap_new.setPixel(j , i , rgb);
            }
        }
        return  bitmap_new;
    }
}
