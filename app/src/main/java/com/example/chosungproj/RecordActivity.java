package com.example.chosungproj;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecordActivity  extends AppCompatActivity {
    public TextView record_text;
    public RecyclerView rv;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> list1 = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_view);

        //record_text = (TextView)findViewById(R.id.record_text);
        rv = (RecyclerView) findViewById(R.id.record_list);

        rv.setLayoutManager(new LinearLayoutManager(this));


        SimpleTextAdapter adapter = new SimpleTextAdapter(list1);
        db.collection("game").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String data = document.getData().toString();

                                list1.add(data);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
        getdataFromFirebase();
        rv.setAdapter(adapter);
    }

    private void getdataFromFirebase() {

    }
}
