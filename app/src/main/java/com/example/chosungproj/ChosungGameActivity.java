package com.example.chosungproj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class ChosungGameActivity extends AppCompatActivity {
    XmlPullParser xpp;
    String key = "86F3B8C49B9C4187A83294E47AA9058F";
    Boolean data;
    String[] wordlist = new String[]{"가랫밥", "가맛밥", "가윗밥", "가첨밥",
            "간질밥", "갈큇밥", "감자밥", "감투밥", "강정밥", "강조밥"};
    String[] chosungList1 = new String[]{"ㄱ", "ㄱ", "ㄱ", "ㄱ",
            "ㄱ", "ㄱ", "ㄱ", "ㄱ", "ㄱ", "ㄱ"};
    String[] chosungList2 = new String[]{"ㄹ", "ㅁ", "ㅇ", "ㅊ",
            "ㅈ", "ㅋ", "ㅈ", "ㅌ", "ㅈ", "ㅈ"};
    String[] chosungList3 = new String[]{"ㅂ", "ㅂ", "ㅂ", "ㅂ",
            "ㅂ", "ㅂ", "ㅂ", "ㅂ", "ㅂ", "ㅂ"};
    Button button1;
    TextView textView, chosung1, chosung2, chosung3;
    EditText edittext1;
    int answerint = 0;
    int numberint = 0;

    private CountDownTimer countdownTimer;
    private TextView countdownText;
    private long timeLeftInMilliseconds = 600000; //10 mins
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosunggame_view);

        button1 = (Button) findViewById(R.id.button1);
        textView = (TextView) findViewById(R.id.text1);
        edittext1 = (EditText) findViewById(R.id.edittext1);
        chosung1 = (TextView) findViewById(R.id.chosung1);
        chosung2 = (TextView) findViewById(R.id.chosung2);
        chosung3 = (TextView) findViewById(R.id.chosung3);
        textView.setText("정답 수 : " + String.valueOf(answerint) + " / " + String.valueOf(numberint));
        chosung1.setText(chosungList1[0]);
        chosung2.setText(chosungList2[0]);
        chosung3.setText(chosungList3[0]);

        countdownText = findViewById(R.id.chosung_timer);

        startTimer();
    }

    private void startTimer() {
        countdownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }
    public void updateTimer(){
        int minutes = (int)timeLeftInMilliseconds/60000;
        int seconds = (int)timeLeftInMilliseconds% 60000 / 1000;

        String timeLeftText;

        timeLeftText= "" + minutes;
        timeLeftText +=":";
        if(seconds<10) timeLeftText+="0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                CheckAnswer(edittext1.getText().toString());
                edittext1.setText(null);
                textView.setText("정답 수 : " + String.valueOf(answerint) + " / " + String.valueOf(numberint));
                if (numberint == 10) {
                    chosung1.setVisibility(View.INVISIBLE);
                    chosung2.setVisibility(View.INVISIBLE);
                    chosung3.setVisibility(View.INVISIBLE);
                    edittext1.setVisibility(View.INVISIBLE);
                    button1.setVisibility(View.INVISIBLE);
                }
                else {
                    chosung1.setText(chosungList1[numberint]);
                    chosung2.setText(chosungList2[numberint]);
                    chosung3.setText(chosungList3[numberint]);
                }
                break;
        }
    }

    void CheckAnswer(String s) {
        if (s.equals(wordlist[numberint])) {
            answerint += 1;
        }
        numberint += 1;
    }
}
