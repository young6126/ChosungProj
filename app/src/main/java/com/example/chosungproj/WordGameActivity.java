package com.example.chosungproj;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WordGameActivity extends AppCompatActivity {

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
}
