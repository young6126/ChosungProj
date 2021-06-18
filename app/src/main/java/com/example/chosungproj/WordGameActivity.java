package com.example.chosungproj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


public class WordGameActivity extends AppCompatActivity {
    XmlPullParser xpp;
   /* String key = "86F3B8C49B9C4187A83294E47AA9058F";*/
    Boolean data;
    String[] wordlist = new String[]{"가랫밥", "가맛밥", "가윗밥", "가첨밥",
            "간질밥", "갈큇밥", "감자밥", "감투밥", "강정밥", "강조밥"};
    String[] chosungList1 = new String[]{"ㄱ", "ㄱ", "ㄱ", "ㄱ",
            "ㄱ", "ㄱ", "ㄱ", "ㄱ", "ㄱ", "ㄱ"};
    String[] chosungList2 = new String[]{"ㄹ", "ㅁ", "ㅇ", "ㅊ",
            "ㅈ", "ㅋ", "ㅈ", "ㅌ", "ㅈ", "ㅈ"};
    String[] chosungList3 = new String[]{"ㅂ", "ㅂ", "ㅂ", "ㅂ",
            "ㅂ", "ㅂ", "ㅂ", "ㅂ", "ㅂ", "ㅂ"};
    String[] defineList = new String[] {"가래로 떠낸 흙의 덩이", "가마솥에 지은 밥", "천 끝단에 가위로 벤 자리를 넣는 일",
            "먹을 만큼 먹은 뒤에 더 먹는 밥", "간지럼", "갈퀴로 긁어모은 검불이나 갈잎","감자로 지은 밥",
            "밥을 그릇에 어떻게 담는가에 따라서 불려진 이름으로 밥그릇 위까지 소복하게 올라오도록 높이 담은 밥을 말한다",
            "강정을 만들기 위하여 찹쌀을 물에 불려 시루에 찐 밥", "좁쌀로만 지은 밥"
    };

    Button button1;
    TextView word_score, chosung1, chosung2, chosung3, define;
    EditText edittext1;
    int answerint = 0;
    int numberint = 0;

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
        setContentView(R.layout.wordgame_view);

        button1 = (Button) findViewById(R.id.button1);
        word_score = (TextView) findViewById(R.id.word_score);
        edittext1 = (EditText) findViewById(R.id.edittext1);
        chosung1 = (TextView) findViewById(R.id.chosung1);
        chosung2 = (TextView) findViewById(R.id.chosung2);
        chosung3 = (TextView) findViewById(R.id.chosung3);
        define = (TextView) findViewById(R.id.word_define);
        word_score.setText("정답 수 : " + answerint + " / " + numberint);
        chosung1.setText(chosungList1[0]);
        chosung2.setText(chosungList2[0]);
        chosung3.setText(chosungList3[0]);
        define.setText(defineList[0]);

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
                listData.add("단어게임");
                listData.add(Integer.toString(answerint));
                listData.add(Integer.toString(numberint));
                listData.add(timeLeftText);
                map.put("단어게임", listData);
                saveDataToFirestore();
                word_score.setText("시간 끝");
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
                word_score.setText("정답 수 : " + answerint + " / " + numberint);
                if (numberint == 10) {
                    stopTimer();
                    listData.add("단어게임");
                    listData.add(Integer.toString(answerint));
                    listData.add(Integer.toString(numberint));
                    listData.add(timeLeftText);
                    map.put("단어게임", listData);
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
                    define.setText(defineList[numberint]);
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


/*public class WordGameActivity extends AppCompatActivity {

    static String[] word_list = new String[100];
    static String[] definition_list = new String[100];
    static int firestore_count = 0;
    static final String STATE_SCORE = "playerScore";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordgame_view);
        TextView definition_view = (TextView) findViewById(R.id.definition_view);
        TextView countTimer_view = (TextView) findViewById(R.id.countTimer);
        EditText users_answer = (EditText)findViewById(R.id.word_answer_text);
        //queryFirebaseData();
//        savedInstanceState.putInt(STATE_SCORE, firestore_count);
//        if(savedInstanceState != null) {
//            firestore_count = savedInstanceState.getInt(STATE_SCORE);
//        }
//        super.onSaveInstanceState(savedInstanceState);

        //카운트하고 게임 시작하기
        long maxCounter = 5;
        long diff = 10;
        new CountDownTimer(maxCounter, diff){

            @Override
            public void onTick(long l) {
                long diff = maxCounter - l;
                countTimer_view.setText((int) (diff/1000));
            }

            @Override
            public void onFinish() {
                countTimer_view.setText("START");
            }
        };



        //firestore에서 정의 및 단어 가져오기
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("test").document("word")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Map<String, Object> map = document.getData();
                                for (Map.Entry<String,Object> entry : map.entrySet()){
                                    String word = entry.getKey();

                                    //Log.d("store", word);
                                    word_list[firestore_count]=word;

                                    if(entry.getKey().equals(word)){
                                        definition_list[firestore_count++] = entry.getValue().toString();
                                        definition_view.setText(entry.getValue().toString());
                                        //Log.d("store", entry.getValue().toString());
                                    }
                                }
                            }
                        }

                    }
                });


        for(int i=0;i<firestore_count;i++){
            Log.d("store", word_list[i] + "," + definition_list[i]);
        }

    }
}*/
