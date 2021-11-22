// StudentWrapper.java
// Cursor wrapper for the Student class
// By Nicholas Verrochi
// Last Modified: 5/3/18

package com.bhcc.nick.gradenet.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bhcc.nick.gradenet.Student;
import com.bhcc.nick.gradenet.database.Schema.StudentTable;

import java.util.UUID;

public class StudentWrapper extends CursorWrapper {

    public StudentWrapper(Cursor cursor) {
        super(cursor);
    }

    public Student getStudent() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(StudentTable.Cols.UUID)));
        String program = getString(getColumnIndex(StudentTable.Cols.PROGRAM));
        String gpa = getString(getColumnIndex(StudentTable.Cols.GPA));

        Student student = new Student();
        student.setUUID(uuid);
        student.setProgram(program);
        student.setGPA(gpa);

        return student;
    }

}
