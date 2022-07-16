package com.example.crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCourseActivity extends AppCompatActivity {

    private TextInputEditText courseNameEdt, coursePriceEdt, courseSuitedFor, courseImgEdt, courseLinkEdt, courseDescEdt;
    private Button addCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        courseNameEdt = findViewById(R.id.EdtCourseName);
        coursePriceEdt = findViewById(R.id.EdtCoursePrice);
        courseSuitedFor = findViewById(R.id.EdtCourseSuitedFor);
        courseImgEdt = findViewById(R.id.EdtCourseImageLink);
        courseLinkEdt = findViewById(R.id.EdtCourseLink);
        courseDescEdt = findViewById(R.id.EdtCourseDescription);
        addCourseBtn = findViewById(R.id.BtnAddCourse);
        loadingPB = findViewById(R.id.PBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String suitedFor = courseSuitedFor.getText().toString();
                String courseImg = courseImgEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDesc = courseDescEdt.getText().toString();
                courseID = courseName;
                CourseRVModal courseRVModal = new CourseRVModal(courseName, courseDesc, coursePrice, suitedFor, courseImg, courseLink, courseID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.child(courseID).setValue(courseRVModal);
                        Toast.makeText(AddCourseActivity.this, "Course Added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCourseActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCourseActivity.this, "Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}