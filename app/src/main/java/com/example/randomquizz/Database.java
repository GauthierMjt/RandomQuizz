package com.example.randomquizz;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.randomquizz.DB_basis.*;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GoQuiz.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteDatabase db;


    Database(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);

    }


    private void fillQuestionsTable()
    {

        Questions q1 = new Questions("Name of Gauthier's tortoise ?","Mindy","Queen Elizabeth","The Rock","Paul",3);
        addQuestions(q1);


        Questions q2 = new Questions("Capital of Australia","Canberra","Sydney","Adelaide","Cairns",1);
        addQuestions(q2);


        Questions q3 = new Questions("Top scorer of the french's national football team ?","Henry","Platini","Giroud","Papin",3);
        addQuestions(q3);


        Questions q4 = new Questions("Who's the most streamed artist of all-time ?","BTS","Drake","Bad Bunny","The Weeknd",2);
        addQuestions(q4);


        Questions q5 = new Questions("Who's the best teacher ?","Yvan Monka","Mr.Ossonce","Chat-GPT","Mr.Benabou",4);
        addQuestions(q5);


        Questions q6 = new Questions("What's the best place for Tax Evasion ?","Malta","Luxembourg","Bermuda","USA",3);
        addQuestions(q6);
    }

    private void addQuestions(Questions question){

        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION,question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuestionTable.COLUMN_OPTION4,question.getOption4());
        cv.put(QuestionTable.COLUMN_ANSWER_NR,question.getAnswerNr());
        db.insert(QuestionTable.TABLE_NAME,null,cv);

    }

    @SuppressLint("Range")
    public ArrayList<Questions> getAllQuestions() {

        ArrayList<Questions> questionList = new ArrayList<>();
        db = getReadableDatabase();



        String Projection[] = {

                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.COLUMN_OPTION1,
                QuestionTable.COLUMN_OPTION2,
                QuestionTable.COLUMN_OPTION3,
                QuestionTable.COLUMN_OPTION4,
                QuestionTable.COLUMN_ANSWER_NR
        };



        Cursor c = db.query(QuestionTable.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null);


        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));

                questionList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionList;

    }

}


