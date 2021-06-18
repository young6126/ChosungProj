package com.example.chosungproj;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    TextView chosung_score, chosung1, chosung2, chosung3;
    EditText edittext1;
    int answerint = 0;
    int numberint = 0;


    //타이머 관련 변수
    private CountDownTimer countdownTimer;
    private TextView countdownText;
    private long timeLeftInMilliseconds = 60000; //10 mins
    private boolean timerRunning;
    //점수 기록을 위한 변수
    public String timeLeftText;

    //파이어스토어 저장
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Map<String, List> map = new HashMap<>();
    public List<String> listData = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosunggame_view);

        button1 = (Button) findViewById(R.id.button1);
        chosung_score = (TextView) findViewById(R.id.chosung_score);
        edittext1 = (EditText) findViewById(R.id.edittext1);
        chosung1 = (TextView) findViewById(R.id.chosung1);
        chosung2 = (TextView) findViewById(R.id.chosung2);
        chosung3 = (TextView) findViewById(R.id.chosung3);
        chosung_score.setText("정답 수 : " + answerint + " / " + numberint);
        chosung1.setText(chosungList1[0]);
        chosung2.setText(chosungList2[0]);
        chosung3.setText(chosungList3[0]);

        countdownText = findViewById(R.id.chosung_timer);

        startTimer();


    }

    private void saveDataToFirestore() {
        db.collection("game").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("record", "기록되었습니다.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("record", "기록 실패.");
                    }
                });
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
                //saveDataToFirestore();
                listData.add("초성게임");
                listData.add(Integer.toString(answerint));
                listData.add(Integer.toString(numberint));
                listData.add(timeLeftText);
                map.put("초성게임", listData);
                saveDataToFirestore();
                chosung_score.setText("시간 끝");
                chosung1.setVisibility(View.INVISIBLE);
                chosung2.setVisibility(View.INVISIBLE);
                chosung3.setVisibility(View.INVISIBLE);
                edittext1.setVisibility(View.INVISIBLE);
                button1.setVisibility(View.INVISIBLE);


            }
        }.start();
        timerRunning = true;

    }
    public void updateTimer(){
        int minutes = (int)timeLeftInMilliseconds/60000;
        int seconds = (int)timeLeftInMilliseconds% 60000 / 1000;



        timeLeftText= "" + minutes;
        timeLeftText +=":";
        if(seconds<10) timeLeftText+="0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }
    public void stopTimer(){
        countdownTimer.cancel();
        timerRunning = false;
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                CheckAnswer(edittext1.getText().toString());
                edittext1.setText(null);
                chosung_score.setText("정답 수 : " + answerint + " / " + numberint);
                //recode_score = "초성게임" + answerint + " / " + numberint;

                if (numberint == 10) {
                    stopTimer();
                    listData.add("초성게임");
                    listData.add(Integer.toString(answerint));
                    listData.add(Integer.toString(numberint));
                    listData.add(timeLeftText);
                    map.put("초성게임", listData);
                    saveDataToFirestore();
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