package com.tp4.gradesappfirebase;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lst;

    ArrayList<String> allStudents = new ArrayList<>();// = {"Sara", "Samira", "Sami"};
    ArrayList<String> Notes = new ArrayList<>();
    String[] allTopics = {"Android", "Angular", "UX", "Databases", "C++", "Big Data"};

    String selectedStudent;

    //1st Version
//    String[] NotesSara = {"12", "7", "3.5", "15", "8", "10.5"};
//    String[] NotesSamira = {"2", "4", "18.5", "16", "18", "5.75"};
//    String[] NotesSami = {"11", "17", "5.5", "10", "2", "10.25"};

    //2nd Version
//    String[][] NotesAll = {
//            {"12", "7", "3.5", "15", "8", "10.5"},
//            {"2", "4", "18.5", "16", "18", "5.75"},
//            {"11", "17", "5.5", "10", "2", "10.25"}
//    };

    AutoCompleteTextView autoSaisie;
    MyAdapter adapter;
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lst = this.findViewById(R.id.notesLst);

        this.autoSaisie = findViewById(R.id.saisieAuto);

         adapter = new MyAdapter(MainActivity.this, Notes);

        lst.setAdapter(adapter);



        // databaseReference = database.getReference("Student");
        autoSaisie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStudent = ((TextView) view).getText().toString();

                String[] all = selectedStudent.split("-");

                getNotes(all[1].trim());

                adapter.notifyDataSetChanged();
                lst.invalidateViews();


                lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        String selectedTopic = allTopics[i];
                        //TextView myNote = (TextView) view;
                        TextView txt = (TextView) view.findViewById(R.id.note);
                        Double note = Double.parseDouble(txt.getText().toString());



                        if (note >= 10)
                            Toast.makeText(getApplicationContext(), selectedTopic + ": Pass", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), selectedTopic + ": Fail", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        getStudentsName();

        ArrayAdapter ar = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, this.allStudents);

        autoSaisie.setAdapter(ar);
    }

    public void goToAdd(View v) {
        Intent i = new Intent(this, AddActivity.class);
        i.putExtra("collection_name", "Student");
        startActivityForResult(i, 1);
    }

    private void getStudentsName() {
        this.allStudents.clear();

        CollectionReference studentsRef = db.collection("students");

        studentsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Student> studentList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Student student = document.toObject(Student.class);
                    this.allStudents.add(student.getName() + " - " + student.getCin());
                }
            } else {
                System.err.println("Erreur lors de la récupération des étudiants : " + task.getException());
            }
        });



    }

    public void getNotes(String cin) {
        Notes.clear();


        CollectionReference studentsRef = db.collection("students");

        studentsRef.document(cin).get()
                .addOnSuccessListener(documentSnapshot ->  {
                    if (documentSnapshot.exists()) {
                        Student student = documentSnapshot.toObject(Student.class);

                        if(student.getCin().equals(cin)) {
                            Notes.add(String.valueOf(student.getNoteAndroid()));
                            Notes.add(String.valueOf(student.getNoteAngular()));
                            Notes.add(String.valueOf(student.getNoteUX()));
                            Notes.add(String.valueOf(student.getNoteDB()));
                            Notes.add(String.valueOf(student.getNoteC()));
                            Notes.add(String.valueOf(student.getNoteBigData()));
                            MainActivity.this.adapter.notifyDataSetChanged();
                        }
                    } else {
                        System.out.println("Aucun étudiant trouvé avec ce CIN.");
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Erreur : " + e.getMessage());
                });
    }
}
