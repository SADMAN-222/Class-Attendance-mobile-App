package edu.ewubd.cse48920243;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class StudentListActivity extends AppCompatActivity {

    private ListView lvStudentList;
    private Button btnAddNew, btnAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        lvStudentList = findViewById(R.id.lvStudentList);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnAttendance = findViewById(R.id.btnAttendance);

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentListActivity.this, AttendanceActivity.class);
                startActivity(i);
            }
        });
    }
}