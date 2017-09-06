package cn.jzvd.demo;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.jzvd.JCVideoPlayer;
import cn.jzvd.JCVideoPlayerSimple;
import cn.jzvd.JCVideoPlayerStandard;
import cn.jzvd.demo.R;

/**
 * Created by Nathen on 16/7/31.
 */
public class ApiActivity extends AppCompatActivity implements View.OnClickListener {
    Button mSmallChange, mBigChange, mOrientation;
    JCVideoPlayerSimple mJcVideoPlayerSimple;
    JCVideoPlayerStandard mJcVideoPlayerStandard;
    JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("About Api");
        setContentView(R.layout.activity_api);

        mSmallChange = (Button) findViewById(R.id.small_change);
        mBigChange = (Button) findViewById(R.id.big_change);
        mOrientation = (Button) findViewById(R.id.orientation);

        mSmallChange.setOnClickListener(this);
        mBigChange.setOnClickListener(this);
        mOrientation.setOnClickListener(this);

        mJcVideoPlayerSimple = (JCVideoPlayerSimple) findViewById(R.id.simple_demo);
        mJcVideoPlayerSimple.setUp("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子在家吗");


        mJcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        LinkedHashMap map = new LinkedHashMap();
        map.put("高清", VideoConstant.videoUrlList[0]);
        map.put("标清", VideoConstant.videoUrls[0][6]);
        map.put("普清", VideoConstant.videoUrls[0][4]);
        mJcVideoPlayerStandard.setUp(map, 2
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子不信");
        Picasso.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(mJcVideoPlayerStandard.thumbImageView);
        mJcVideoPlayerStandard.loop = true;
        mJcVideoPlayerStandard.headData = new HashMap<>();
        mJcVideoPlayerStandard.headData.put("key", "value");


//        JCVideoPlayer.SAVE_PROGRESS = false;
        /** Play video in local path, eg:record by system camera **/
//        cpAssertVideoToLocalPath();
//        mJcVideoPlayerStandard.setUp(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4"
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子不信");
        /** Play video in assert, but not work now **/
//        mJcVideoPlayerStandard.setUp("file:///android_asset/local_video.mp4"
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子不信");

        /** ImageLoader **/
//        ImageLoader.getInstance().displayImage(VideoConstant.videoThumbs[0][1],
//                videoController1.thumbImageView);
        /** Glide **/
//        Glide.with(this)
//                .load(VideoConstant.videoThumbs[0][1])
//                .into(videoController1.thumbImageView);
        /** volley omit **/
        /** Fresco omit **/
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.small_change:
                startActivity(new Intent(ApiActivity.this, UISmallChangeActivity.class));
                break;
            case R.id.big_change:
                Toast.makeText(ApiActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(ApiActivity.this, UIBigChangeActivity.class));
                break;
            case R.id.orientation:
                startActivity(new Intent(ApiActivity.this, OrientationActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.clearSavedProgress(this, null);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cpAssertVideoToLocalPath() {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4");
            myInput = this.getAssets().open("local_video.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}