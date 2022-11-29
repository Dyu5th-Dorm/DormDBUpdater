package org.dyu5thdorm.DormDBUpdater.repositories;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.RoomDataFetcher.models.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRepository implements DormitoryRepository<Student> {
    @Override
    public boolean exists(Student student) {
        try {
            PreparedStatement checkStudent = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "SELECT s_id FROM student WHERE s_id = ?;"
                    );
            checkStudent.setString(1, student.studentId());
            return checkStudent.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Student student) {
        if (!exists(student)) {
            try {
                PreparedStatement insertStudent = DormDBUpdater.database
                        .getConnection()
                        .prepareStatement(
                                "INSERT INTO student (s_id, name, sex, major, citizenship) " +
                                        "VALUES (?, ?, ?, ?, ?);"
                        );

                insertStudent.setString(1, student.studentId());
                insertStudent.setString(2, student.name());
                insertStudent.setString(3, student.sex());
                insertStudent.setString(4, student.major());
                insertStudent.setString(5, student.citizenship());

                insertStudent.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (hasStudentDataChanged(student)) {
            update(student);
        }
    }

    @Override
    public void update(Student student) {
        try {
            PreparedStatement replaceStudent = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "UPDATE student SET name = ?, sex = ?, major = ?, citizenship = ? WHERE s_id = ?;"
                    );
            replaceStudent.setString(1, student.name());
            replaceStudent.setString(2, student.sex());
            replaceStudent.setString(3, student.major());
            replaceStudent.setString(4, student.citizenship());
            replaceStudent.setString(5, student.studentId());

            replaceStudent.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Student student) {
        if (!exists(student)) return;

        try {
            PreparedStatement deleteStudent = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "DELETE FROM student WHERE s_id = ?;"
                    );
            deleteStudent.setString(1, student.studentId());
            deleteStudent.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasStudentDataChanged(Student student) {
        if (student == null) return true;

        try {
            PreparedStatement getStudent = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "SELECT * FROM student WHERE s_id = ?;"
                    );
            getStudent.setString(1, student.studentId());
            ResultSet r = getStudent.executeQuery();
            r.next();
            String dbName = r.getString("name");
            String dbMajor = r.getString("major");
            return !dbName.equals(student.name()) || !dbMajor.equals(student.major()) ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
