package com.example.thefactor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> answers = new ArrayList<>();
    ArrayList<String> scores = new ArrayList<>();
    ArrayList<Integer> boom = new ArrayList<>();
    ArrayList<Integer> ref = new ArrayList<>();
    InputMethodManager imm;
    EditText editText;
    TextView scoreTextView;
    SharedPreferences sharedPreferences;
    TextView highTextView;
    TextView countdownTextView;
    ImageView imageView;
    Button button;
    Button button1;
    Button button2;
    Button button3;
    int b;
    Button button4;
    int answer1;
    Vibrator vibrator;
    int score = 0;
    int count = 0;
    int l;
    int num;
    CountDownTimer countDownTimer;

    public void generateNum(View view) {
        resetCountDownTimer();
        num = editText.getText().toString().equals("") ? 0 : Integer.parseInt(editText.getText().toString());
        if (num == 0) {
            editText.setError("Enter a Valid Number.");
        } else if (num == 1 || num==2 || num==3 || num==4) {
            Toast.makeText(this, "1,2,3,4 cant be entered due to Inavailability of Options", Toast.LENGTH_SHORT).show();
        } else {
            newQuestion(num);
            editText.setEnabled(false);
            button1.setEnabled(false);
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
            startCountDownTimer();
        }
    }

    public void select(View view) {
        imageView.setVisibility(View.VISIBLE);
        count++;
        if (Integer.toString(b).equals(view.getTag().toString())) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            stopCounDownTimer();
            imageView.setImageResource(R.drawable.green);
            new CountDownTimer(200, 200) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    imageView.setVisibility(View.INVISIBLE);
                    imageView.setImageResource(R.drawable.white);
                }
            }.start();
            editText.setEnabled(true);
            button1.setEnabled(true);
            scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(count));
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
            editText.setText("");
            editText.requestFocus();
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            newQuestion(num);
        } else {
            stopCounDownTimer();
            Toast.makeText(this, "Wrong): The Answer is " + Integer.toString(answer1), Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.drawable.red);
            vibrator.vibrate(200);
            new CountDownTimer(200, 200) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    imageView.setVisibility(View.INVISIBLE);
                    imageView.setImageResource(R.drawable.white);
                }
            }.start();
            scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(count));
            scores.add(Integer.toString(score));
            ref.add(score);
            try{
                sharedPreferences.edit().putString("streak",ObjectSerializer.serialize(scores)).apply();
            }catch(Exception e){
                e.printStackTrace();
            }
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
            if(Collections.max(ref)>l){
                highTextView.setText("Longest Streak: "+Integer.toString(Collections.max(ref)));
            }
        }

    }

    public void playAgain(View view) {
        button.setVisibility(View.INVISIBLE);
        editText.setEnabled(true);
        button1.setEnabled(true);
        score = 0;
        count = 0;
        scoreTextView.setText("0/0");
        editText.setText("");
        editText.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        scoreTextView = findViewById(R.id.scoreTextView);
        countdownTextView = findViewById(R.id.countdownTextView);
        imageView = findViewById(R.id.imageView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        highTextView = findViewById(R.id.highTextView);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button = findViewById(R.id.button);
        sharedPreferences = this.getSharedPreferences("com.example.thefactorfactor",Context.MODE_PRIVATE);
        try{
            scores = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("streak",ObjectSerializer.serialize(new ArrayList<String>())));
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.i("scoreLength",String.valueOf(scores.size()));
        boom.clear();
        if(scores.size()>0) {
            for (int o = 0; o < scores.size(); o++) {
                boom.add(Integer.parseInt(scores.get(o)));
            }
            l = Collections.max(boom);
            highTextView.setText("Longest Streak: " + Integer.toString(l));
        }else{
            highTextView.setText("Longest Streak: 0");
        }
        editText.requestFocus();
    }

    public void newQuestion(int ui) {
        ArrayList<Integer> f = new ArrayList<>();
        for (int i = 1; i <= ui; i++) {
            if (ui % i == 0) {
                f.add(i);
            }
        }
        Random rand = new Random();
        b = rand.nextInt(3);
        Log.i("randomplace", Integer.toString(b));
        answers.clear();
        answer1 = f.get(rand.nextInt(f.size()));
        for (int y = 0; y <= 2; y++) {
            if (y == b) {
                answers.add(Integer.toString(answer1));
            } else {
                int w = rand.nextInt(ui) + 1;
                while (f.contains(w) || answers.contains(Integer.toString(w))) {
                    w = rand.nextInt(ui) + 1;
                }
                Log.i("opt", Integer.toString(w));
                answers.add(Integer.toString(w));
            }
        }
        button2.setText(answers.get(0));
        button3.setText(answers.get(1));
        button4.setText(answers.get(2));
    }

    public void startCountDownTimer(){
        countDownTimer = new CountDownTimer(11000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Time Up!!!!", Toast.LENGTH_SHORT).show();
                vibrator.vibrate(200);
                button1.setEnabled(false);
                editText.setEnabled(false);
                button2.setVisibility(View.INVISIBLE);
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                Log.i("timeUp Score",Integer.toString(score));
                scores.add(Integer.toString(score));
                ref.add(score);
                if(Collections.max(ref)>l){
                    highTextView.setText("Longest Streak: "+Integer.toString(Collections.max(ref)));
                }
                try{
                    sharedPreferences.edit().putString("streak",ObjectSerializer.serialize(scores)).apply();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stopCounDownTimer(){
        countDownTimer.cancel();
    }

    public void resetCountDownTimer(){
        countdownTextView.setText("0s");
    }

    @Override
    protected void onPause() {
        scores.add(Integer.toString(score));
        ref.add(score);
        try{
            sharedPreferences.edit().putString("streak",ObjectSerializer.serialize(scores)).apply();
        }catch(Exception e){
            e.printStackTrace();
        }
        hideKeyboardFrom(getApplicationContext(),imageView);
        super.onPause();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
