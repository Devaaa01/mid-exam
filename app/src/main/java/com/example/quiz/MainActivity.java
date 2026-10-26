package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextStudentId;
    private Spinner spinnerMajor;
    private Button btnAdd, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.editTextName);
        editTextStudentId = findViewById(R.id.editTextStudentId);
        spinnerMajor = findViewById(R.id.spinnerMajor);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        String[] majors = {"Informatics", "Information Systems"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, majors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String studentId = editTextStudentId.getText().toString().trim();
            String major = spinnerMajor.getSelectedItem().toString();

            if (name.isEmpty() || studentId.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(MainActivity.this);
            boolean success = dbHelper.addStudent(new StudentModel(name, studentId, major));

            if (!success) {
                Toast.makeText(MainActivity.this, "Student ID already exists! Must be unique.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                editTextName.setText("");
                editTextStudentId.setText("");
            }
        });

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewStudent.class);
            startActivity(intent);
        });
    }
}
