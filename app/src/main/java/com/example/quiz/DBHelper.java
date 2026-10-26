package com.example.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "student_db";
    private static final String TB_NAME = "students";
    private static final String Col_ID = "id";
    private static final String Col_Name = "name";
    private static final String Col_StudentID = "student_id";
    private static final String Col_Major = "major";

    private static final String CREATE_TABLE = "CREATE TABLE " + TB_NAME + " ("
            + Col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Col_Name + " TEXT NOT NULL, "
            + Col_StudentID + " TEXT NOT NULL UNIQUE, "
            + Col_Major + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public boolean isStudentIdExists(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Col_ID + " FROM " + TB_NAME + " WHERE " + Col_StudentID + " = ?", new String[]{studentId});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean isStudentIdExistsOther(int id, String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Col_ID + " FROM " + TB_NAME + " WHERE " + Col_StudentID + " = ? AND " + Col_ID + " != ?", new String[]{studentId, String.valueOf(id)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean addStudent(StudentModel studentModel) {
        if (isStudentIdExists(studentModel.getStudentId())) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Col_Name, studentModel.getName());
        cv.put(Col_StudentID, studentModel.getStudentId());
        cv.put(Col_Major, studentModel.getMajor());
        long result = db.insert(TB_NAME, null, cv);
        return result != -1;
    }

    public boolean updateStudent(StudentModel studentModel) {
        if (isStudentIdExistsOther(studentModel.getId(), studentModel.getStudentId())) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Col_Name, studentModel.getName());
        cv.put(Col_StudentID, studentModel.getStudentId());
        cv.put(Col_Major, studentModel.getMajor());
        int rows = db.update(TB_NAME, cv, Col_ID + " = ?", new String[]{String.valueOf(studentModel.getId())});
        return rows > 0;
    }

    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_NAME, Col_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<StudentModel> getStudentList() {
        String query = "SELECT " + Col_ID + ", " + Col_Name + ", " + Col_StudentID + ", " + Col_Major + " FROM " + TB_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        List<StudentModel> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String studentId = cursor.getString(2);
                String major = cursor.getString(3);
                list.add(new StudentModel(id, name, studentId, major));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
