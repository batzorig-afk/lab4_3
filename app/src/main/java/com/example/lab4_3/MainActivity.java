package com.example.lab4_3;

import static android.icu.text.CaseMap.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.text.CaseMap;
import android.media.MediaDrm;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    public TextView songName, duration, total;
    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 10000, backwardTime = 10000;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }
    public void initializeViews(){
        songName = (TextView) findViewById(R.id.songName);
        mediaPlayer = MediaPlayer.create(this, R.raw.duu);
        finalTime = mediaPlayer.getDuration();
        total = (TextView) findViewById(R.id.totalSongDuration);
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        songName.setText("duu.mp3");
        seekbar.setMax((int) finalTime);
        seekbar.setClickable(true);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
    public void play(View view) {
        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekbar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            timeElapsed = mediaPlayer.getCurrentPosition();
            seekbar.setProgress((int) timeElapsed);
            double timeRemaining = finalTime - timeElapsed;
            total.setText(String.format("%d:%d ", TimeUnit.MILLISECONDS.toMinutes((long)
                    timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
            duration.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long)
                    timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            durationHandler.postDelayed(this, 100);
        }
    };

    public void pause(View view) {
        mediaPlayer.pause();
    }
    public void forward(View view) {
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + forwardTime;
            mediaPlayer.seekTo((int) timeElapsed);
        }
    }
    public void rewind(View view){
        timeElapsed = timeElapsed - backwardTime;
        mediaPlayer.seekTo((int) timeElapsed);
    }

    public void next(View view)  {
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(this, R.raw.duu2);
        songName.setText("duu2.mp3");
        finalTime = mediaPlayer.getDuration();
        seekbar.setMax((int) finalTime);
        mediaPlayer.start();
    }
    public void previous(View view){
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(this, R.raw.duu3);
        songName.setText("duu3.mp3");
        finalTime = mediaPlayer.getDuration();
        seekbar.setMax((int) finalTime);
        mediaPlayer.start();
    }
}