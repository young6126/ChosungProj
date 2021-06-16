package com.example.chosungproj;

import android.os.Bundle;
import android.util.Log;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordgame_view);
        TextView definition_view = (TextView) findViewById(R.id.definition_view);
        //queryFirebaseData();
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
                                        //Log.d("store", entry.getValue().toString());
                                    }
                                }
                            }
                        }
                        definition_view.setText(definition_list[0]);
                    }
                });


        for(int i=0;i<firestore_count;i++){
            Log.d("store", word_list[i] + "," + definition_list[i]);
        }

    }
    public void showLoding(){

    }
}
