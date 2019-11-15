package com.vaibhav.notesapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vaibhav.notesapp.DataModel.TextData;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Items> {
    private Context context;
    private List<TextData> textDataList;
    private LayoutInflater inflater;


    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;

    private List<String> uid;
    private ProgressDialog progressDialog;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public NotesAdapter(Context context,List<TextData> texts,List<String> uid){
        this.context=context;
        this.textDataList=texts;
        inflater=LayoutInflater.from(context);
        this.uid=uid;
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Wait..");
        progressDialog.setCancelable(false);

    }

    @NonNull
    @Override
    public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Items(inflater.inflate(R.layout.notes_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Items holder, int position) {
        holder.textView.setText(String.valueOf(textDataList.get(position).getTextData()));

    }

    @Override
    public int getItemCount() {
        return textDataList.size();
    }

    public class Items extends RecyclerView.ViewHolder {
        TextView textView;
        Button button;

        public Items(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.notesCard);
            button=itemView.findViewById(R.id.deleteNotes);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDocument();
                }


                private void deleteDocument() {
                    progressDialog.show();
                    firebaseFirestore=FirebaseFirestore.getInstance();
                    documentReference=firebaseFirestore.collection("TextNotes").document(uid.get(getAdapterPosition()));
                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                            textDataList.remove(getAdapterPosition());
                            uid.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }

        private void openDialog() {
            builder=new AlertDialog.Builder(context);
            View view=LayoutInflater.from(context).inflate(R.layout.text_dialog,null);
            Button cancel,save;
            cancel=view.findViewById(R.id.cancel);
            save=view.findViewById(R.id.save);
            EditText editText;
            editText=view.findViewById(R.id.editText);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            editText.setText(String.valueOf(textDataList.get(getAdapterPosition()).getTextData()));



            builder.setView(view);
            builder.create();
            dialog=builder.create();
            dialog.show();
        }
    }
}
