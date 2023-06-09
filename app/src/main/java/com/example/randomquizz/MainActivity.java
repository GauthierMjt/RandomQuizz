package com.example.randomquizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {


    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;



    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;


    private ArrayList<Questions> questionList;
    private int questionCounter;
    private int questionTotalCount;
    private Questions currentQuestions;
    private boolean answerd;


    private final Handler handler = new Handler();




    private int correctAns = 0, wrongAns = 0;

    private Clock clock;
    private GoodAnswer goodAnswer;
    private FalseAnswer falseAnswer;

    private AudioStuff audioStuff;

    int FLAG = 0;

    int score =0;

    private int totalSizeofQuiz=0;

    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private CountDownTimer countDownTimer;
    private long timeleftinMillis;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        fetchDB();
        Log.i("BUGBUG","onCreate() in MainActivity");




        clock = new Clock(this);
        goodAnswer = new GoodAnswer(this);
        falseAnswer = new FalseAnswer(this);
        audioStuff = new AudioStuff(this);
    }



    private void setupUI(){
        textViewQuestion = findViewById(R.id.txtQuestionContainer);

        textViewScore = findViewById(R.id.txtScore);
        textViewQuestionCount = findViewById(R.id.txtTotalQuestion);
        textViewCountDown = findViewById(R.id.txtViewTimer);



        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button);
    }

    public void fetchDB() {
        Database dbHelper = new Database(this);
        questionList = dbHelper.getAllQuestions();
        startQuiz();

    }


    public void startQuiz() {

        questionTotalCount = questionList.size();
        Collections.shuffle(questionList);

        showQuestions();   // calling showQuestion() method



        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                switch (checkedId){

                    case R.id.radio_button1:

                        rb1.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                        rb2.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);



                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.anser_selection));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));

                        break;
                    case R.id.radio_button2:
                        rb2.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

                        rb1.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);



                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.anser_selection));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));

                        break;

                    case R.id.radio_button3:
                        rb3.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                        rb2.setTextColor(Color.BLACK);
                        rb1.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);


                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.anser_selection));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));

                        break;

                    case R.id.radio_button4:
                        rb4.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                        rb2.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb1.setTextColor(Color.BLACK);



                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.anser_selection));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));

                        break;
                }

            }
        });

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!answerd) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {

                        quizOperation();
                    } else {

                        Toast.makeText(MainActivity.this, "Choose an answer damn", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    public void showQuestions() {


        rbGroup.clearCheck();

        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));
        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_answer_background));

        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);


        if (questionCounter < questionTotalCount) {
            currentQuestions = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestions.getQuestion());
            rb1.setText(currentQuestions.getOption1());
            rb2.setText(currentQuestions.getOption2());
            rb3.setText(currentQuestions.getOption3());
            rb4.setText(currentQuestions.getOption4());

            questionCounter++;
            answerd = false;
            buttonConfirmNext.setText("Validate");

            textViewQuestionCount.setText("Questions: " + questionCounter + "/" + questionTotalCount);


            timeleftinMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();




        } else {

            // If Number of Questions Finishes then we need to finish the Quiz and Shows the User Quiz Performance


            Toast.makeText(this, "End of the quiz", Toast.LENGTH_SHORT).show();

            rb1.setClickable(false);
            rb2.setClickable(false);
            rb3.setClickable(false);
            rb4.setClickable(false);
            buttonConfirmNext.setClickable(false);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    finalResult();

                }
            }, 2000);
        }
    }

    private void quizOperation() {
        answerd = true;

        countDownTimer.cancel();

        RadioButton rbselected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) + 1;

        checkSolution(answerNr, rbselected);

    }


    private void checkSolution(int answerNr, RadioButton rbselected) {

        switch (currentQuestions.getAnswerNr()) {
            case 1:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb1.setBackgroundColor(0x00FF00);
                    rb1.setTextColor(Color.BLACK);

                    correctAns++;


                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    goodAnswer.correctDialog(score,this);


                    FLAG = 1;


                } else {
                    changetoIncorrectColor(rbselected);

                    wrongAns++;


                    String correctAnswer = (String) rb1.getText();
                    falseAnswer.wrongDialog(correctAnswer,this);

                    FLAG = 2;


                }
                audioStuff.setAudioforAnswer(FLAG);
                break;
            case 2:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb2.setBackgroundColor(0x00FF00);
                    rb2.setTextColor(Color.BLACK);

                    correctAns++;


                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    goodAnswer.correctDialog(score,this);

                    FLAG = 1;
                    audioStuff.setAudioforAnswer(FLAG);



                } else {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;


                    String correctAnswer = (String) rb2.getText();
                    falseAnswer.wrongDialog(correctAnswer,this);

                    FLAG = 2;
                    audioStuff.setAudioforAnswer(FLAG);




                }
                break;
            case 3:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb3.setBackgroundColor(0x00FF00);
                    rb3.setTextColor(Color.BLACK);


                    correctAns++;


                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    goodAnswer.correctDialog(score,this);

                    FLAG = 1;
                    audioStuff.setAudioforAnswer(FLAG);



                } else {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;


                    String correctAnswer = (String) rb3.getText();
                    falseAnswer.wrongDialog(correctAnswer,this);

                    FLAG = 2;
                    audioStuff.setAudioforAnswer(FLAG);




                }
                break;
            case 4:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    rb4.setBackgroundColor(0x00FF00);
                    rb4.setTextColor(Color.BLACK);


                    correctAns++;


                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    goodAnswer.correctDialog(score,this);

                    FLAG = 1;
                    audioStuff.setAudioforAnswer(FLAG);




                } else {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;


                    String correctAnswer = (String) rb4.getText();
                    falseAnswer.wrongDialog(correctAnswer,this);

                    FLAG = 2;
                    audioStuff.setAudioforAnswer(FLAG);




                }
                break;
        }
        if (questionCounter == questionTotalCount) {
            buttonConfirmNext.setText("Last thought");
        }
    }

    void changetoIncorrectColor(RadioButton rbselected) {
        rbselected.setBackgroundColor(0xFF0000);;

        rbselected.setTextColor(Color.BLACK);
    }



    //  the timer code

    private void startCountDown(){

        countDownTimer = new CountDownTimer(timeleftinMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftinMillis = millisUntilFinished;

                updateCountDownText();
            }

            @Override
            public void onFinish() {

                timeleftinMillis = 0;
                updateCountDownText();

            }
        }.start();

    }



    private void updateCountDownText(){

        int minutes = (int) (timeleftinMillis/1000) /60;
        int seconds = (int) (timeleftinMillis/1000) % 60;

        //  String timeFormatted = String.format(Locale.getDefault(),"02d:%02d",minutes,seconds);

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes, seconds);
        textViewCountDown.setText(timeFormatted);


        if (timeleftinMillis < 10000){


            textViewCountDown.setTextColor(ContextCompat.getColor(this,R.color.red));

            FLAG = 3;
            audioStuff.setAudioforAnswer(FLAG);


        }else {
            textViewCountDown.setTextColor(ContextCompat.getColor(this,R.color.white));
        }


        if (timeleftinMillis == 0 ){


            Toast.makeText(this, "Hep Hep Hep No more time!", Toast.LENGTH_SHORT).show();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    clock.timerDialog();

                }
            },2000);



        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BUGBUG","onRestart() in MainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BUGBUG","onStop() in MainActivity");
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("BUGBUG","onPause() in MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BUGBUG","onResume() in MainActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BUGBUG","onStart() in MainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        Log.i("BUGBUG","onDestroy() in MainActivity");


    }


    private void finalResult(){

        Intent resultData = new Intent(MainActivity.this,Result.class);

        resultData.putExtra("Your Score",score);
        resultData.putExtra("Total Question",questionTotalCount);
        resultData.putExtra("Good Answer",correctAns);
        resultData.putExtra("False Answer",wrongAns);
        startActivity(resultData);

    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){

            Intent intent = new Intent(MainActivity.this, StoryTelling.class);
            startActivity(intent);

        }else {
            Toast.makeText(this, "See you soon", Toast.LENGTH_SHORT).show();

        }
        backPressedTime = System.currentTimeMillis();
    }
}
