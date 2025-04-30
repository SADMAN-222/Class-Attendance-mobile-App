package edu.ewubd.cse48920243;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AttendanceActivity extends AppCompatActivity {

    private Button btnDate, btnBack, btnSave;
    private ListView lvAttendance;

    private String course = "CSE489";
    private int section = 2;
    private String[][] students = {
            {"2022-1-60-001", "ABC"},
            {"2022-1-60-002", "BBC"},
            {"2022-1-60-003", "CNN"},
            {"2022-1-60-004", "DBC"}
    };
    private ArrayList<Attendance> records = new ArrayList<>();
    private CustomAttendanceAdapter adapter;
    private boolean shouldUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        //
        lvAttendance = findViewById(R.id.lvAttendance);
        btnDate = findViewById(R.id.btnDate);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        adapter = new CustomAttendanceAdapter(this, records);
        lvAttendance.setAdapter(adapter);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = btnDate.getText().toString();
                int year, month, date;
                if(selectedDate.equals("DD-MM-YYYY")){
                    Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    date = c.get(Calendar.DATE);
                } else {
                    String[] dateParts = selectedDate.split("-");
                    date = Integer.parseInt(dateParts[0]);
                    month = Integer.parseInt(dateParts[1]) - 1;
                    year = Integer.parseInt(dateParts[2]);
                }
                DatePickerDialog dpd =new DatePickerDialog(AttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        btnDate.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                        loadAttendances();
                    }
                }, year, month, date);
                dpd.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = btnDate.getText().toString();
                if(selectedDate.equals("DD-MM-YYYY")){
                    Toast.makeText(AttendanceActivity.this, "Select a date", Toast.LENGTH_LONG).show();
                    return;
                }
                long timeInMilliSeconds = getCurrentDateInMilliseconds(selectedDate);
                AttendanceDB db = new AttendanceDB(AttendanceActivity.this);
                for(Attendance a : records){
                    System.out.println(a.name+" "+a.status);
                    if(shouldUpdate){
                        db.updateAttendance(a.id, course, timeInMilliSeconds, a.status);
                    } else {
                        db.insertAttendance(a.id, a.name, course, section, timeInMilliSeconds, a.status);
                    }
                }
                db.close();
            }
        });
    }

    private long getCurrentDateInMilliseconds(String selectedDate) {
        if(selectedDate.equals("DD-MM-YYYY")){
            return 0;
        }
        String[] dateParts = selectedDate.split("-");
        int date = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1;
        int year = Integer.parseInt(dateParts[2]);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, date);
        long timeInMilliSeconds = c.getTimeInMillis();
        return timeInMilliSeconds;
    }

    public void onStart(){
        super.onStart();
        loadAttendances();
    }
    private void loadAttendances() {
        records.clear();
        String date = btnDate.getText().toString();
        // check date validity here
        long selectedDate = getCurrentDateInMilliseconds(date);
        // if a valid date is selected, then load data from db for that date
        String q = "SELECT ID, name, status FROM attendance WHERE course='"+course+"' AND section="+section+" AND date="+selectedDate;
        AttendanceDB db = new AttendanceDB(this);
        Cursor c = db.selectAttendances(q);
        System.out.println(c.getCount()); //1734480000000 1734480000000
        if(c!=null && c.getCount()>0){
            shouldUpdate = true;
            while (c.moveToNext()){
                String id = c.getString(0);
                String name = c.getString(1);
                int status = c.getInt(2);
                Attendance a = new Attendance(id, name, status);

                System.out.println(a.name+" "+a.status);

                records.add(a);
            }
        } else {
            shouldUpdate = false;
            for(int i=0; i < students.length; i++){
                Attendance a = new Attendance(students[i][0], students[i][1], 0);
                records.add(a);
            }
        }
        db.close();
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();
    }
}