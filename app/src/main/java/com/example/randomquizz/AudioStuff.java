package com.example.randomquizz;


import android.content.Context;
import android.media.MediaPlayer;

public class AudioStuff {


    private Context mContext;
    private MediaPlayer mediaPlayer;

    public AudioStuff(Context mContext) {
        this.mContext = mContext;
    }


    public void setAudioforAnswer(int flag){

        switch (flag){

            case 1:

                int correctAudio = R.raw.goodanswer;
                playMusic(correctAudio);
                break;
            case 2:
                int wrongAudio = R.raw.falseanswer;
                playMusic(wrongAudio);
                break;
            case 3:
                int timerAudio = R.raw.clocktick;
                playMusic(timerAudio);
                break;


        }

    }

    private void playMusic(int audiofile) {

        mediaPlayer = MediaPlayer.create(mContext,audiofile);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });



    }

}
