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

import java.util.HashMap;
import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {

    private TextInputEditText courseNameEdt, coursePriceEdt, courseSuitedForEtn, courseImgEdt, courseLinkEdt, courseDescEdt;
    private Button updateCourseBtn, deleteCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseID;
    private CourseRVModal courseRVModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        firebaseDatabase = FirebaseDatabase.getInstance();
        courseNameEdt = findViewById(R.id.EdtCourseName);
        coursePriceEdt = findViewById(R.id.EdtCoursePrice);
        courseSuitedForEtn = findViewById(R.id.EdtCourseSuitedFor);
        courseImgEdt = findViewById(R.id.EdtCourseImageLink);
        courseLinkEdt = findViewById(R.id.EdtCourseLink);
        courseDescEdt = findViewById(R.id.EdtCourseDescription);
        updateCourseBtn = findViewById(R.id.BtnUpdateCourse);
        deleteCourseBtn = findViewById(R.id.BtnDeleteCourse);
        loadingPB = findViewById(R.id.PBLoading);
        courseRVModal = getIntent().getParcelableExtra("course");

        if(courseRVModal != null) {
            courseNameEdt.setText(courseRVModal.getCourseName());
            coursePriceEdt.setText(courseRVModal.getCoursePrice());
            courseSuitedForEtn.setText(courseRVModal.getBestSuitedFor());
            courseImgEdt.setText(courseRVModal.getCourseImg());
            courseLinkEdt.setText(courseRVModal.getCourseLink());
            courseDescEdt.setText(courseRVModal.getCourseDescription());
            courseID = courseRVModal.getCourseID();
        }

        databaseReference = firebaseDatabase.getReference("Courses").child(courseID);

        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String suitedFor = courseSuitedForEtn.getText().toString();
                String courseImg = courseImgEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDesc = courseDescEdt.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("courseName", courseName);
                map.put("courseDescription", courseDesc);
                map.put("coursePrice", coursePrice);
                map.put("bestSuitedFor", suitedFor);
                map.put("courseImg", courseImg);
                map.put("courseLink", courseLink);
                map.put("courseID", courseID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);

                        databaseReference.updateChildren(map);
                        Toast.makeText(EditCourseActivity.this, "Course Updated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditCourseActivity.this, "Failed to Update Course!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });
    }

    private void deleteCourse() {
        databaseReference.removeValue();
        Toast.makeText(this, "Course Deleted!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
    }
}