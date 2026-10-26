package com.example.quiz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    List<StudentModel> studentList;
    Context context;
    DBHelper dbHelper;

    public StudentAdapter(List<StudentModel> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.student_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel student = studentList.get(position);
        holder.textViewID.setText("ID: " + student.getId());
        holder.editTextName.setText(student.getName());
        holder.editTextStudentId.setText(student.getStudentId());
        holder.editTextMajor.setText(student.getMajor());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = holder.editTextName.getText().toString().trim();
                String newStudentId = holder.editTextStudentId.getText().toString().trim();
                String newMajor = holder.editTextMajor.getText().toString().trim();

                if (newName.isEmpty() || newStudentId.isEmpty() || newMajor.isEmpty()) {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean updated = dbHelper.updateStudent(new StudentModel(student.getId(), newName, newStudentId, newMajor));
                if (!updated) {
                    Toast.makeText(context, "Student ID already taken by another record!", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteStudent(student.getId());
                studentList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewID;
        EditText editTextName, editTextStudentId, editTextMajor;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.textViewID);
            editTextName = itemView.findViewById(R.id.editTextName);
            editTextStudentId = itemView.findViewById(R.id.editTextStudentId);
            editTextMajor = itemView.findViewById(R.id.editTextMajor);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
