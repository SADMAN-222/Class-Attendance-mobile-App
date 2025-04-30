package edu.ewubd.cse48920243;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttendanceDB extends SQLiteOpenHelper {

	public AttendanceDB(Context context) {
		super(context, "AttendanceDB.db", null, 2);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("DB@OnCreate");
		String sql = "CREATE TABLE attendance  ("
								+ "ID TEXT,"
								+ "name TEXT,"
								+ "course TEXT,"
								+ "section INT,"
								+ "date INT,"
								+ "status INT DEFAULT 0,"
								+ "PRIMARY KEY (ID, course, date)"
				                + ")";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Write code to modify database schema here");
		// db.execSQL("ALTER table my_table  ......");
	}
	public void insertAttendance(String studentID, String name, String course, int section, long date, int status) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues cols = new ContentValues();
			cols.put("ID", studentID);
			cols.put("name", name);
			cols.put("course", course);
			cols.put("section", section);
			cols.put("date", date);
			cols.put("status", status);
			db.insert("attendance", null, cols);
		}catch (Exception e){
			System.out.println(e.getMessage());
			updateAttendance(studentID, course, date, status);
		}
		db.close();
	}
	public void updateAttendance(String studentID, String course, long date, int status) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("status", status);
  		db.update("attendance", cols, "ID=? AND course=? AND date=?", new String[] {studentID, course, String.valueOf(date)} );

		db.close();
	}
	public void deleteAttendance(String studentID, String course, long date) {
		SQLiteDatabase db = this.getWritableDatabase();
  		db.delete("attendance", "ID=?,course=?,date=?", new String[] {studentID, course, String.valueOf(date)} );
		db.close();
	}
	public Cursor selectAttendances(String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = null;
		try {
			c = db.rawQuery(query, null);
		} catch (Exception e){
			e.printStackTrace();
		}
		return c;
	}
}