package com.bing.lan.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

            }
        });
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

    public void onDrawableClick(View view) {
        log.i("onDrawableClick(): ================Drawable===================");
        printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.bitmap));
    }

    public void onMdpiClick(View view) {
        log.i("onMdpiClick(): =================mdpi==================");
        printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_mdpi));
    }

    public void onHdpiClick(View view) {
        log.i("onHdpiClick(): ==================hdpi=================");
        printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_hdpi));
    }

    public void onXhdpiClick(View view) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        log.i("onXhdpiClick(): =================xhdpi==================");
        printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_xhdpi, options));
        //log.i("onXhdpiClick(): ===================================");
        //printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.poster_xhdpi));
    }

    /**
     * 根据当前手机(xhdpi)屏幕密度，1:1 加载当前dpi文件夹bitmap, 如果当前文件夹(xhdpi)没有该图片A，
     * 而xxhdpi中有该图片A，则按一定比例进行加载，规则如下：
     * <p>
     * B 图片 实际大小 1000  在xhdpi 中    加载后尺寸 1000   ===>1:1加载
     * A 图片 实际大小 1000  在xxhdpi 中   加载后尺寸 1000 x 2 = ? x 3  ==>  ? = 666
     *
     * @param view
     */

    public void onXxhdpiClick(View view) {
        log.i("onXxhdpiClick(): =================xxhdpi==================");
        printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_xxhdpi));
        //log.i("onXxhdpiClick(): ===================================");
        //printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.poster_xxhdpi));
    }

    public void onXxxhdpiClick(View view) {
        log.i("onXxxhdpiClick(): ==================xxxhdpi=================");
        printBitmapInfo(BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_xxxhdpi));
    }

    private void printBitmapInfo(Bitmap bitmap) {
        log.i("printBitmapInfo(): density:  " + Resources.getSystem().getDisplayMetrics().density);
        log.i("printBitmapInfo(): getHeight:  " + bitmap.getHeight());
        log.i("printBitmapInfo(): getWidth:  " + bitmap.getWidth());
        log.i("printBitmapInfo(): getRowBytes:  " + bitmap.getRowBytes());
        log.i("printBitmapInfo(): getByteCount:  " + bitmap.getByteCount());
        log.i("printBitmapInfo(): getDensity:  " + bitmap.getDensity());
    }
}
