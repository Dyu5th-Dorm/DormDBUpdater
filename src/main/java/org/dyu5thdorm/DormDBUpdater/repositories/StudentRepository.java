package org.dyu5thdorm.DormDBUpdater.repositories;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.RoomDataFetcher.models.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRepository implements DormitoryRepository<Student> {
    /**
     * Check if this `student s1` exists on `student r2` table.
     * @param student To be checked student instance.
     * @return Does instance `student s1` exists in `student s2` table?
     */
    @Override
    public boolean exists(Student student) {
        return existsByStudentId(student.studentId());
    }

    /**
     * Same with exists(Student).
     * @param sId Student id.
     * @return Does instance `student s1` exists in `student s2` table?
     */
    public boolean existsByStudentId(String sId) {
        try {
            PreparedStatement checkStudent = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "SELECT s_id FROM student WHERE s_id = ?;"
                    );
            checkStudent.setString(1, sId);
            return checkStudent.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Insert instance `student s1` to `student s2` table if !exists(s1)
     * @param student `Student r1` instance of to be inserted to `Student r2`.
     */
    @Override
    public void insert(Student student) {
        if (student == null) return;

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

                DormDBUpdater.logger.info(
                        String.format("Insert student data %s successfully.", student)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (hasStudentDataChanged(student)) {
            update(student);
        }
    }

    /**
     * Update student of this `student s1` instance if r1 in `student s2` table data is changed.
     * @param student `Room r1` instance of to be updated to `room r2`.
     */
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

            DormDBUpdater.logger.info(
                    String.format("Update student data %s successfully.", student)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete `student s1` from `student s2` table.
     * @param student `student s1` instance of to be deleted student from `student s2` table.
     */
    @Override
    public void delete(Student student) {
        if (!exists(student)) return;
        deleteByStudentId(student.studentId());
    }

    /**
     * Same with delete(Student).
     * @param sId student id of to be deleted student from `student s2` table.
     */
    public void deleteByStudentId(String sId) {
        if (!existsByStudentId(sId)) return;

        try {
            PreparedStatement deleteStudent = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "DELETE FROM student WHERE s_id = ?;"
                    );
            deleteStudent.setString(1, sId);
            deleteStudent.execute();

            DormDBUpdater.logger.info(
                    String.format("Delete student data by student_id %s successfully.", sId)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To check if data of `student s1` from `student s2` table changed.
     * @param student To be checked student.
     * @return Does data of `student s1` from `student s2` table changed.
     */
    private boolean hasStudentDataChanged(Student student) {
        if (student == null) return false;

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
