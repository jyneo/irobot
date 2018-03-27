package com.arcsoft.irobot.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcsoft.irobot.R;
import com.arcsoft.irobot.SensorDataPackage.Create2;
import com.github.chrisbanes.photoview.PhotoView;

/**
 *
 * Created by yj2595 on 2018/3/22.
 */

public class RobotMappingFragment extends Fragment {

    private static final String TAG = "RobotMappingFragment";
    Create2 create2;
    View rootView;

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
//        PhotoView photoView = rootView.findViewById(R.id.photo_view);
//        Drawable bitmap = getResources().getDrawable(R.drawable.img_spalsh);
//        photoView.setImageDrawable(bitmap);
        create2 = new Create2();
//        create2.mappingStream(mMappingStreamCallback);

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

    public final Create2.MappingStreamCallback mMappingStreamCallback = new Create2.MappingStreamCallback() {
        @Override
        public void onMappingStream(byte[] results) {
//            int width = 300;
//            int height = 300;
//            int count = 0;
//            for (int i = 0; i < 90000; i++) {
//                results[i] = (byte) 127;
//            }

            int width = (results[2] << 24 & 0xFF) + (results[3] << 16 & 0xFF) + (results[4] << 8 & 0xFF) + (results[5] & 0xFF);
            int height = (results[6] << 24 & 0xFF) + (results[7] << 16 & 0xFF) + (results[8] << 8 & 0xFF) + (results[9] & 0xFF);
            Log.d(TAG, "onMappingStream: " + width + " " + height);
//            for (int i = 10; i < 90010; i++) {
//                results[i] = (byte) 127;
//            }

            Bitmap bitmap = Bitmap.createBitmap(width, height , Bitmap.Config.ARGB_8888);

            int count = 10;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    // 0、1、2、3 转换成灰度值
                    if (results[count] == 0) {
                        bitmap.setPixel(j, i, 255);
                    } else if (results[count] == 1) {
                        bitmap.setPixel(j, i, 0);
                    } else if (results[count] == 2) {
                        bitmap.setPixel(j, i, 100);
                    } else {
                        bitmap.setPixel(j, i, 200);
                    }
                    count++;
                }
            }

            Drawable drawable = new BitmapDrawable(bitmap);
            PhotoView photoView = rootView.findViewById(R.id.photo_view);
            photoView.setImageDrawable(drawable);
        }
    };
}
