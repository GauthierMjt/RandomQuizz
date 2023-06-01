package com.example.randomquizz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartApp extends AppCompatActivity {

    private final static int EXIT_CODE = 100;

    TextView txtSplashText;
    ImageView imgViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        txtSplashText = findViewById(R.id.textviewLogoText);
        imgViewLogo = findViewById(R.id.imgviewLogo);


        Animation animation = AnimationUtils.loadAnimation(this,R.anim.transition);
        imgViewLogo.setAnimation(animation);
        txtSplashText.setAnimation(animation);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                try{

                    sleep(3000);


                }catch (Exception e){

                    e.printStackTrace();
                }finally {


                    GotoPlayActivity();

                }


            }
        });
        thread.start();

    }

    private void GotoPlayActivity() {

        startActivityForResult( new Intent(StartApp.this, StoryTelling.class),EXIT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXIT_CODE){

            if (resultCode == RESULT_OK){
                if (data.getBooleanExtra("EXIT",true)){
                    finish();
                }
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BUGBUG","onStop() in StartApp");
        finish();

    }
}
