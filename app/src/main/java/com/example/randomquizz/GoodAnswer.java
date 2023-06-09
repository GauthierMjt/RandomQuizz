package com.example.randomquizz;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GoodAnswer {

    private Context mContext;

    private Dialog correctDialog;

    private MainActivity mquizActivity;

    public GoodAnswer(Context mContext) {
        this.mContext = mContext;
    }

    public void correctDialog(int score, final MainActivity quizActivity){

        mquizActivity = quizActivity;

        correctDialog = new Dialog(mContext);
        correctDialog.setContentView(R.layout.activity_good);

        Button btCorrectDialog = (Button) correctDialog.findViewById(R.id.bt_correct_dialog);

        score(score);


        btCorrectDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctDialog.dismiss();
                quizActivity.showQuestions();
            }
        });

        correctDialog.show();
        correctDialog.setCancelable(false);
        correctDialog.setCanceledOnTouchOutside(false);

        correctDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void score(int score) {

        TextView textViewScore = (TextView) correctDialog.findViewById(R.id.text_score);
        textViewScore.setText("Score: " + String.valueOf(score));
    }


}
