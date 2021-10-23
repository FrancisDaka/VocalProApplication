package com.example.vocalpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer, samplemp;

    private static int MICROPHONE_PERMISSION_CODE=200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isMicPresent()){
            getMicPermission();
        }

        samplemp = MediaPlayer.create(getApplicationContext(), R.raw.samplebeat);
    }


    public void recordButtonPressed(View view){
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingPath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();


            samplemp.start();

            Toast.makeText(this, "Recording has started", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void stopRecording(View view){

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;

        samplemp.stop();
        samplemp.release();
        samplemp=null;


        Toast.makeText(this, "Recording has stopped", Toast.LENGTH_SHORT).show();
    }

    public void playRecording(View view){
        try{
            mediaPlayer= new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Recording is playing", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private boolean isMicPresent(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }else{
            return false;
        }

    }

    private void getMicPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        ==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingPath(){

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory,"testRecordingFile"+ ".mp3");

        return file.getPath();
    }
}

