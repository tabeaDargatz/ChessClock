package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{
    enum gameStates {reset, started, finished, paused}
    ImageButton btnPause;
    Button btnPlayer1;
    Button btnPlayer2;
    TextView moveCountP1;
    TextView moveCountP2;
    boolean p1Turn = false;
    boolean firstClick = true;
    CountDownTimer timerPlayer1;
    CountDownTimer timerPlayer2;
    long timeLeft1;
    long timeLeft2;
    short moveCounterP1 = 0;
    short moveCounterP2 = 0;
    MediaPlayer mp1;
    MediaPlayer mp2;
    MediaPlayer mpTimeEnded;
    TimeControlSettings currentSetting;
    gameStates state = gameStates.reset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlayer1 = findViewById(R.id.btn_player1);
        btnPlayer2 = findViewById(R.id.btn_player2);
        moveCountP1 = findViewById(R.id.moveCounterP1);
        moveCountP2 = findViewById(R.id.moveCounterP2);
        btnPause = findViewById(R.id.btn_pause);
        mp1 = MediaPlayer.create(this, R.raw.chess_clock_switch1);
        mp2 = MediaPlayer.create(this, R.raw.chess_clock_switch2);
        mpTimeEnded = MediaPlayer.create(this, R.raw.time_ended);
        currentSetting = new TimeControlSettings("Default", 180000, 180000, 0, 0);
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            String nTemp = b.getString("name");
            Long p1Temp = b.getLong("p1");
            Long p2Temp = b.getLong("p2");
            Long inc1Temp = b.getLong("incP1");
            Long inc2Temp = b.getLong("incP2");
            currentSetting = new TimeControlSettings(nTemp, p1Temp, p2Temp, inc1Temp, inc2Temp);
        }
        if(currentSetting.timeControlP1 == 0 || currentSetting.timeControlP2 == 0)
        {
            currentSetting = new TimeControlSettings("Default", 180000, 180000, 0,0);
        }
        timeLeft1 = currentSetting.timeControlP1;
        timeLeft2 = currentSetting.timeControlP2;
        btnPlayer1.setText(convertTimeControl(timeLeft1));
        btnPlayer2.setText(convertTimeControl(timeLeft2));
    }

    public String convertTimeControl(long tc)
    {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(tc),
                TimeUnit.MILLISECONDS.toMinutes(tc) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tc)),
                TimeUnit.MILLISECONDS.toSeconds(tc) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tc)));
    }
    public void p1PressedBtn(View view)
    {
        if(state == gameStates.reset || (p1Turn && state == gameStates.started) )
        {
            state = gameStates.started;
            mp1.start();
          if(timerPlayer1 != null)
          {
              timerPlayer1.cancel();
          }
            p1Turn = false;
            btnPlayer2.setEnabled(true);
            btnPlayer1.setEnabled(false);
            createTimer();
        }
    }

    public void p2PressedBtn(View view)
    {
        if(state == gameStates.reset || (!p1Turn && state == gameStates.started) )
        {
            mp2.start();
            state = gameStates.started;
            p1Turn = true;
            if(timerPlayer2 != null)
            {
                timerPlayer2.cancel();
            }
            btnPlayer2.setEnabled(false);
            btnPlayer1.setEnabled(true);
            createTimer();
        }
    }

    public void pause(View view)
    {
        if(state == gameStates.started)
        {
            state = gameStates.paused;
            btnPause.setImageResource(R.drawable.play);
            if(p1Turn)
            {
                timerPlayer1.cancel();
                btnPlayer1.setEnabled(false);
            }
            else {
                timerPlayer2.cancel();
                btnPlayer2.setEnabled(false);
            }
        }
        else if(state == gameStates.paused)
        {
            btnPause.setImageResource(R.drawable.pause);
            if(p1Turn)
            {
                btnPlayer1.setEnabled(true);
            }
            else{
                btnPlayer2.setEnabled(true);
            }
            createTimer();
            state = gameStates.started;
        }
    }

    public void reset(View view)
    {
     if(state != gameStates.reset)
     {
        if(timerPlayer1 != null)
        {
            timerPlayer1.cancel();
        }
         if(timerPlayer2 != null)
         {
             timerPlayer2.cancel();
         }
         btnPause.setImageResource(R.drawable.pause);
         timeLeft1 = currentSetting.timeControlP1;
         timeLeft2 = currentSetting.timeControlP2;
         btnPlayer1.setText(convertTimeControl(timeLeft1));
         btnPlayer2.setText(convertTimeControl(timeLeft2));
         btnPlayer1.setEnabled(true);
         btnPlayer2.setEnabled(true);
         state = gameStates.reset;
         firstClick = true;
         moveCounterP1 = 0;
         moveCounterP2 = 0;
         moveCountP1.setText("Move: 0");
         moveCountP2.setText("Move: 0");
     }
    }

    public void createTimer()
    {
        if(p1Turn)
        {
            if(firstClick)
            {
                firstClick = false;
            }
            else{
                timeLeft2 += currentSetting.incrementP2;
                btnPlayer2.setText(convertTimeControl(timeLeft2));
                if(state != gameStates.paused)
                {
                    moveCounterP2++;
                    moveCountP2.setText("Move: " +moveCounterP2);
                }
            }

            timerPlayer1 = new CountDownTimer(timeLeft1,1000)
            {
                @Override
                public void onTick(long l)
                {
                    timeLeft1 = l;
                    btnPlayer1.setText(convertTimeControl(l));
                }

                @Override
                public void onFinish()
                {
                    mpTimeEnded.start();
                    if(state != gameStates.finished)
                    {
                        state = gameStates.finished;
                        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                        b.setMessage("Player 1 ran out of Time!")
                                .setTitle("Game Over");
                        AlertDialog d = b.create();
                        d.show();
                        btnPlayer1.setEnabled(false);
                    }


                }
            }.start();
        }
        else{
            if(firstClick)
            {
               firstClick = false;
            }
            else{
                timeLeft1 += currentSetting.incrementP1;
                btnPlayer1.setText(convertTimeControl(timeLeft1));
                if(state != gameStates.paused)
                {
                    moveCounterP1++;
                    moveCountP1.setText("Move: " + moveCounterP1);
                }
            }

            timerPlayer2 = new CountDownTimer(timeLeft2,1000)
            {
                @Override
                public void onTick(long l)
                {
                    timeLeft2 = l;
                    btnPlayer2.setText(convertTimeControl(l));
                }

                @Override
                public void onFinish()
                {
                    mpTimeEnded.start();
                    if(state != gameStates.finished)
                    {
                        state = gameStates.finished;
                        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                        b.setMessage("Player 2 ran out of Time!")
                                .setTitle("Game Over");
                        AlertDialog d = b.create();
                        d.show();
                        btnPlayer2.setEnabled(false);
                    }
                }
            }.start();
        }
    }

    public void openSettings(View view)
    {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public Context getActivity()
    {
        return this;
    }
}