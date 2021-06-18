package com.example.chosungproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity  extends AppCompatActivity {

    //파이어스토어 저장
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Map<String, List> map = new HashMap<>();
    public List<String> listData = new ArrayList<>();

    Button modify;
    EditText nickname, age;


    private void saveDataToFirestore() {
        db.collection("info").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("record", "수정되었습니다.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("record", "수정 실패.");
                    }
                });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_view);

        nickname = (EditText) findViewById(R.id.user_in_nickname);

        age = (EditText) findViewById(R.id.user_in_age);

        modify = (Button) findViewById(R.id.user_info_modify);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                listData.add(nickname.getText().toString());
                listData.add(age.getText().toString());

                map.put("유저정보", listData);

                saveDataToFirestore();

                startActivity(intent);


            }
        });




        /*SeekBar ScreenLightBar = findViewById(R.id.ScreenLight);

        ScreenLightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setBrightness(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }



    /*private void setBrightness(int value) {
        if(value<10){
            value=10;
        }else if(value > 100){
            value = 100;
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness =(float) value/100;
        getWindow().setAttributes(params);
    }*/
}
