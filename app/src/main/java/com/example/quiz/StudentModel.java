package com.example.quiz;

public class StudentModel {
    Integer id;
    String name;
    String studentId;
    String major;

    public StudentModel(String name, String studentId, String major) {
        this.name = name;
        this.studentId = studentId;
        this.major = major;
    }

    public StudentModel(Integer id, String name, String studentId, String major) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.major = major;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return this.major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
