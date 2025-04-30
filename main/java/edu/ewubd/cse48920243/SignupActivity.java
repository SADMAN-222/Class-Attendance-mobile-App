package edu.ewubd.cse48920243;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignupActivity extends Activity {

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private CheckBox cbRememberUser, cbRememberLogin;
    private Button btnLogin, btnSignInstructor, btnSignStudent;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("att_prefs", MODE_PRIVATE);
        String email = sp.getString("USER-EMAIL", "NO-ACCOUNT-CREATED");
        if(!email.equals("NO-ACCOUNT-CREATED")) {
            goToLoginPage();
        }
        setContentView(R.layout.activity_signup);
        //
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignInstructor = findViewById(R.id.btnSignInstructor);
        btnSignStudent = findViewById(R.id.btnSignStudent);
        //
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        //
        btnSignInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignup(true);
            }
        });
        btnSignStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignup(false);
            }
        });
    }
    private void processSignup(boolean isInstructor){
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String conPass = etConfirmPassword.getText().toString().trim();
        //
        System.out.println(name);
        System.out.println(email);
        System.out.println(phone);
        System.out.println(pass);
        System.out.println(conPass);
        if(name.length() < 4){
            Toast.makeText(SignupActivity.this, "User name should be 4-8 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if(!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email).matches()){
            Toast.makeText(SignupActivity.this, "Invalid email id", Toast.LENGTH_LONG).show();
            return;
        }
        if(phone.length() < 8){
            Toast.makeText(SignupActivity.this, "Phone number should be 8-13 digits", Toast.LENGTH_LONG).show();
            return;
        }
        if(pass.length() != 4){
            Toast.makeText(SignupActivity.this, "Password must be be 4 digits", Toast.LENGTH_LONG).show();
            return;
        }
        if(!pass.equals(conPass)){
            Toast.makeText(SignupActivity.this, "Confirm password didn't match", Toast.LENGTH_LONG).show();
            return;
        }
        // store signup information to Shared Preferences
        SharedPreferences.Editor e = sp.edit();
        e.putString("USER-NAME", name);
        e.putString("USER-EMAIL", email);
        e.putString("USER-PHONE", phone);
        e.putString("USER-PASS", pass);
        e.putBoolean("REM-USER", cbRememberUser.isChecked());
        e.putBoolean("REM-LOGIN", cbRememberLogin.isChecked());
        e.putBoolean("IS-INSTRUCTOR", isInstructor);
        e.apply();
        goToLoginPage();
    }

    private void goToLoginPage(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finishAffinity();
    }
}