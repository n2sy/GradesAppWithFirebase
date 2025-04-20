package com.tp4.gradesappfirebase;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText editName;
    EditText editcin;
    EditText editnote1;
    EditText editnote2;
    EditText editnote3;
    EditText editnote4;
    EditText editnote5;
    EditText editnote6;
    DatabaseReference reff;

    Student student;

    FirebaseFirestore db;


// String[] allTopics = {"Android", "Angular", "UX", "Databases", "C++", "Big Data"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        this.editName = findViewById(R.id.editName);
        this.editcin = findViewById(R.id.cin);
        this.editnote1 = findViewById(R.id.editNoteAndroid);
        this.editnote2 = findViewById(R.id.editNoteAngular);
        this.editnote3 = findViewById(R.id.editNoteUX);
        this.editnote4 = findViewById(R.id.editNoteDB);
        this.editnote5 = findViewById(R.id.editNoteC);
        this.editnote6 = findViewById(R.id.editNoteBigData);

        Intent i = getIntent(); // facultatif sauf si y'a un extra ou un data

        reff = FirebaseDatabase.getInstance().getReference().child(i.getStringExtra("collection_name"));
        db = FirebaseFirestore.getInstance();


    }

    public Map<String, Object> convertStudentToMap(Student student) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(gson.toJson(student), type);
    }

    public void CommitData(View v) {

        String name = editName.getText().toString();
        String cin = editcin.getText().toString();

        float note1 = Float.parseFloat(editnote1.getText().toString());
        float note2 = Float.parseFloat(editnote2.getText().toString());
        float note3 = Float.parseFloat(editnote3.getText().toString());
        float note4 = Float.parseFloat(editnote4.getText().toString());
        float note5 = Float.parseFloat(editnote5.getText().toString());
        float note6 = Float.parseFloat(editnote6.getText().toString());


        student = new Student(name, cin, note1, note2, note3, note4, note5, note6);
        Map<String, Object> studentData = convertStudentToMap(student);

            // Ajouter l'étudiant à Firestore
            db.collection("students").document(student.getCin()) // Utiliser le CIN comme ID
                    .set(studentData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Étudiant ajouté avec succès !", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Erreur lors de l'ajout de l'étudiant : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        finish();

    }
}