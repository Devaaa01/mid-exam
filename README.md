# Student SQLite CRUD Application

An Android application developed in Java to manage student records using a local SQLite database. The app allows users to input student details, enforces unique Student IDs, displays records in a RecyclerView, and supports in-place editing and deletion.

---

## 📸 Application Results

| Main Screen (Input Form) | View Screen (Student List & CRUD) |
|:---:|:---:|
| <img src="screenshots/main_screen.png" width="300"/> | <img src="screenshots/view_screen.png" width="300"/> |

---

## 🚀 Step-by-Step Implementation

### Step 1: Add Dependencies
In `app/build.gradle.kts`, added the RecyclerView library to support listing student records:
```kotlin
dependencies {
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // ...
}
```

### Step 2: Create Data Model (`StudentModel.java`)
Created a model class representing student data with fields:
- `id` (Integer): primary key.
- `name` (String): student's name.
- `studentId` (String): unique student ID.
- `major` (String): selected major.

Implemented constructors, getters, and setters for data access and encapsulation.

### Step 3: Implement SQLite Database Helper (`DBHelper.java`)
Created a helper class extending `SQLiteOpenHelper` to handle all database transactions:
- **Database & Table Setup**: Database name `student_db` (version 1), table `students`.
- **Unique Constraint**: Declared `student_id TEXT NOT NULL UNIQUE` in table creation schema.
- **Uniqueness Validations**:
  - `isStudentIdExists(studentId)`: checks if an ID already exists before inserting a new student.
  - `isStudentIdExistsOther(id, studentId)`: checks if an ID already belongs to another record before updating.
- **CRUD Methods**:
  - `addStudent(StudentModel)`: inserts a new record using `ContentValues`. Returns `false` if student ID already exists.
  - `updateStudent(StudentModel)`: updates record by primary key `id`. Returns `false` if updated student ID conflicts with another record.
  - `deleteStudent(int id)`: deletes record by primary key `id`.
  - `getStudentList()`: retrieves all student records using `rawQuery` and iterates with a `Cursor`.

### Step 4: Build Main Input Screen (`activity_main.xml` & `MainActivity.java`)
- **Layout**: Created an input form matching requirements:
  - `editTextName`: Student Name input.
  - `editTextStudentId`: Student ID input.
  - `spinnerMajor`: Dropdown populated with `"Informatics"` and `"Information Systems"`.
  - `btnAdd`: "Add Student" button.
  - `btnView`: "View Students" button.
- **Logic**:
  - Populates `spinnerMajor` using `ArrayAdapter<String>`.
  - Validates non-empty input fields.
  - Calls `dbHelper.addStudent()`. If the Student ID is duplicate, alerts the user with a Toast notification: *"Student ID already exists! Must be unique."*.
  - On success, displays confirmation Toast and clears text fields.
  - Navigates to `ViewStudent` activity on clicking "View Students" using an explicit `Intent`.

### Step 5: Design Item Layout & Adapter (`student_item_list.xml` & `StudentAdapter.java`)
- **Item Layout**: Created an item row showing student ID, editable name, editable student ID, editable major, and two action buttons: `Edit` and `Delete`.
- **Adapter**:
  - Binds data to views in `onBindViewHolder`.
  - **Edit Button**: Reads edited values, validates inputs, checks for ID uniqueness against other records, calls `dbHelper.updateStudent()`, and refreshes the view.
  - **Delete Button**: Calls `dbHelper.deleteStudent()`, removes item from list, and calls `notifyDataSetChanged()`.

### Step 6: Create Student List Screen (`activity_view_student.xml` & `ViewStudent.java`)
- **Activity**: Contains a `RecyclerView` configured with `LinearLayoutManager`.
- Queries `dbHelper.getStudentList()` and binds it to `StudentAdapter`.
- Displays a Toast if no students are recorded.
- Registered `ViewStudent` activity in `AndroidManifest.xml`.

### Step 7: Build & Verification
- Cleaned and compiled the project using Gradle:
  ```bash
  ./gradlew compileDebugSources assembleDebug
  ```
- Verified zero errors and exit code 0 (`BUILD SUCCESSFUL`).

---

## 📂 Project Structure

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/quiz/
│   ├── MainActivity.java      # Main entry screen: form inputs, Add & View actions
│   ├── ViewStudent.java       # Second screen: hosts RecyclerView of all students
│   ├── StudentModel.java      # Data model (id, name, studentId, major)
│   ├── DBHelper.java          # SQLiteOpenHelper managing student_db and CRUD methods
│   └── StudentAdapter.java    # RecyclerView.Adapter handling card binding, Edit & Delete
└── res/layout/
    ├── activity_main.xml          # Form layout with Name, ID, Major Spinner, Add/View buttons
    ├── activity_view_student.xml  # RecyclerView container layout
    └── student_item_list.xml      # Individual student card layout with Edit/Delete buttons
```
