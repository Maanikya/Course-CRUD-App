package com.example.crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userNameEdt, pwdEdt;
    private TextView registerTV;
    private Button loginBtn;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        userNameEdt = findViewById(R.id.EdtUserName);
        pwdEdt = findViewById(R.id.EdtPwd);
        loginBtn = findViewById(R.id.BtnLogin);
        loadingPB = findViewById(R.id.PBLoading);
        registerTV = findViewById(R.id.TVRegister);
        mAuth = FirebaseAuth.getInstance();

        videoView = (VideoView) findViewById(R.id.lgVideo);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.watch);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(mp -> mp.setLooping(true));

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String userName = userNameEdt.getText().toString();
                String pwd = pwdEdt.getText().toString();

                if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Credentials!!", Toast.LENGTH_SHORT).show();
                }

                else {
                    mAuth.signInWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                            else {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }
}