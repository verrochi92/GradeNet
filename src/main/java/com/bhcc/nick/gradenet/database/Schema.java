// database.Schema.java
// Database schema for the GradeNet app
// By Nicholas Verrochi
// Last Modified: 4/24/18

package com.bhcc.nick.gradenet.database;

public class Schema {

    // user table - contains common user information
    public static final class UserTable {
        public static final String NAME = "user";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String FIRST_NAME = "first_name";
            public static final String LAST_NAME = "last_name";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String USER_LEVEL = "user_level";
        }
    }

    // student table
    public static final class StudentTable {
        public static final String NAME = "student";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PROGRAM = "program";
            public static final String GPA = "gpa";
        }
    }

    // teacher table
    public static final class TeacherTable {
        public static final String NAME = "teacher";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DEPARTMENT = "department";
            public static final String PUBLIC_EMAIL = "public_email";
        }
    }

    // course table
    public static final class CourseTable {
        public static final String NAME = "course";

        public static final class Cols {
            public static final String CLASS_ID = "class_id";
            public static final String TEACHER_ID = "teacher_id";
            public static final String COURSE_NAME = "course_name";
            public static final String SECTION_NUMBER = "section_number";
        }
    }

    // course - student table
    public static final class CourseStudentTable {
        public static final String NAME = "course_student";

        public static final class Cols {
            public static final String CLASS_ID = "class_id";
            public static final String STUDENT_ID = "student_id";
            public static final String GRADE = "grade";
        }
    }

}
