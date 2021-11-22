// TeacherWrapper.java
// Cursor wrapper for Teacher objects
// By Nicholas Verrochi
// Last Modified: 5/7/18

package com.bhcc.nick.gradenet.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bhcc.nick.gradenet.Student;
import com.bhcc.nick.gradenet.Teacher;
import com.bhcc.nick.gradenet.database.Schema.TeacherTable;

import java.util.UUID;

public class TeacherWrapper extends CursorWrapper {

    public TeacherWrapper(Cursor cursor) {
        super(cursor);
    }

    public Teacher getTeacher() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(TeacherTable.Cols.UUID)));
        String department = getString(getColumnIndex(TeacherTable.Cols.DEPARTMENT));
        String publicEmail = getString(getColumnIndex(TeacherTable.Cols.PUBLIC_EMAIL));

        Teacher teacher = new Teacher();
        teacher.setUUID(uuid);
        teacher.setDepartment(department);
        teacher.setPublicEmail(publicEmail);

        return teacher;
    }

}
