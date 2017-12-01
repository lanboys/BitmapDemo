package com.bing.lan.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Bitmap mBitmap;
    private Bitmap mBitmapSrc;
    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mBitmapSrc = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap);

        log.i("drawScannerLaser()  Src bitmapHeight:  " + mBitmapSrc.getHeight());
        log.i("drawScannerLaser()  Src bitmapWidth:  " + mBitmapSrc.getWidth());

    }

    public native String stringFromJNI();

    public static Bitmap getThumbnail(Bitmap srcBmp, float reqWidth, float reqHeight) {
        Bitmap dstBmp;
        if (srcBmp.getWidth() < reqWidth && srcBmp.getHeight() < reqHeight) {
            dstBmp = ThumbnailUtils.extractThumbnail(srcBmp, (int) reqWidth, (int) reqHeight);
            // Otherwise the ratio between measures is calculated to fit requested thumbnail's one
        } else {
            int x = 0, y = 0, width = srcBmp.getWidth(), height = srcBmp.getHeight();
            float ratio = ((float) reqWidth / (float) reqHeight) * ((float) srcBmp.getHeight() / (float) srcBmp.getWidth());
            if (ratio < 1) {
                x = (int) (srcBmp.getWidth() - srcBmp.getWidth() * ratio) / 2;
                width = (int) (srcBmp.getWidth() * ratio);
            } else {
                y = (int) (srcBmp.getHeight() - srcBmp.getHeight() / ratio) / 2;
                height = (int) (srcBmp.getHeight() / ratio);
            }
            dstBmp = Bitmap.createBitmap(srcBmp, x, y, width, height);
        }
        return dstBmp;
    }
}
