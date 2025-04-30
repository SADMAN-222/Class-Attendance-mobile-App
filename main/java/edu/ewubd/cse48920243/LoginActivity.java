package edu.ewubd.cse48920243;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRememberUser, cbRememberLogin;
    private Button btnSignup, btnLoginInstructor, btnLoginStudent;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnLoginInstructor = findViewById(R.id.btnLoginInstructor);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);
        //
        sp = this.getSharedPreferences("att_prefs", MODE_PRIVATE);
        String email = sp.getString("USER-EMAIL", "NO-ACCOUNT-CREATED");
        if(email.equals("NO-ACCOUNT-CREATED")){
            Toast.makeText(this, "Signup first", Toast.LENGTH_LONG).show();
            return;
        }
        String pass = sp.getString("USER-PASS", "");
        boolean isRemUser = sp.getBoolean("REM-USER", false);
        boolean isRemLogin = sp.getBoolean("REM-LOGIN", false);
        if(isRemUser){
            etEmail.setText(email);
            cbRememberUser.setChecked(true);
        }
        if(isRemLogin){
            etPassword.setText(pass);
            cbRememberLogin.setChecked(true);
        }
        //
        btnLoginInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!processLogin(email, pass)){
                    return;
                }
                boolean isInstructor = sp.getBoolean("IS-INSTRUCTOR", false);
                if(isInstructor) {
                    Intent i = new Intent(LoginActivity.this, StudentListActivity.class);
                    startActivity(i);
                    finishAffinity();
                } else {
                    Toast.makeText(LoginActivity.this, "You can't login as an instructor", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnLoginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!processLogin(email, pass)){
                    return;
                }
                boolean isInstructor = sp.getBoolean("IS-INSTRUCTOR", false);
                if(!isInstructor) {
                    Intent i = new Intent(LoginActivity.this, null); // need to add Student's Home Activity class here
                    startActivity(i);
                    finishAffinity();
                } else {
                    Toast.makeText(LoginActivity.this, "You can't login as a student", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean processLogin(String email, String pass){
        String em = etEmail.getText().toString().trim();
        String p = etPassword.getText().toString().trim();
        //
        System.out.println(em);
        System.out.println(p);
        if(!email.equals(em)){
            Toast.makeText(LoginActivity.this, "Email id didn't match", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!pass.equals(p)){
            Toast.makeText(LoginActivity.this, "Password didn't match", Toast.LENGTH_LONG).show();
            return false;
        }
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("REM-USER", cbRememberUser.isChecked());
        e.putBoolean("REM-LOGIN", cbRememberLogin.isChecked());
        e.apply();
        return true;
    }
}