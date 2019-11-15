package com.vaibhav.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vaibhav.notesapp.DataModel.TextData;

import java.util.ArrayList;
import java.util.List;

public class OldNotes extends AppCompatActivity {
    private ImageView addNotes;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;

    private List<TextData> textDataList=new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    private List<String> uid= new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_notes);
        addNotes=findViewById(R.id.addNotes);
        recyclerView=findViewById(R.id.oldNotesRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(OldNotes.this));
        notesAdapter=new NotesAdapter(OldNotes.this,textDataList,uid);
        recyclerView.setAdapter(notesAdapter);

        getNotesList();
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OldNotes.this,Choice.class));
            }
        });


    }

    private void getNotesList() {
        firebaseFirestore=FirebaseFirestore.getInstance();
        collectionReference=firebaseFirestore.collection("TextNotes");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                        TextData textData=snapshot.toObject(TextData.class);
                        textDataList.add(textData);
                        uid.add(snapshot.getId());

                        Log.d("NOTED", textData.getTextData());
                    }
                    notesAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(OldNotes.this,"No previous notes found",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
