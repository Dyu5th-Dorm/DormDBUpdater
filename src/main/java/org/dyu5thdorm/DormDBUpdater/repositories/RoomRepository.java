package org.dyu5thdorm.DormDBUpdater.repositories;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.RoomDataFetcher.models.Room;
import org.dyu5thdorm.RoomDataFetcher.models.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Room repository be used to operate room table of database.
 */
public class RoomRepository implements DormitoryRepository<Room> {

    /**
     * Check if this `room r1` exists on `room r2` table.
     * @param room To be checked room instance.
     * @return Does instance `room r1` exists in `room r2` table?
     */
    @Override
    public boolean exists(Room room) {
        try {
            PreparedStatement checkRoomTable = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "SELECT room_id FROM room WHERE room_id = ?;"
                    );
            checkRoomTable.setString(1, room.roomId());
            return checkRoomTable.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Insert instance `room r1` to `room r2` table if !exists(r1)
     * @param room `Room r1` instance of to be inserted to `room r2`.
     */
    @Override
    public void insert(Room room) {
        if (!exists(room)) {
            try {
                PreparedStatement insertRoom = DormDBUpdater.database
                        .getConnection()
                        .prepareStatement(
                                "INSERT INTO room (room_id, s_id) VALUES (?, ?)"
                        );
                insertRoom.setString(1, room.roomId());
                insertStudent(insertRoom, 2, room.student());
                insertRoom.execute();

                DormDBUpdater.logger.info(
                        String.format(
                                "Insert room data %s successfully.", room
                        )
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (isDifferentStudent(room)) {
            update(room);
        }
    }

    /**
     * Update student of this `room r1` instance if r1 in `room r2` table and
     * living in r1 student data be updated.
     * @param room` Room r1` instance of to be updated to `room r2`.
     */
    @Override
    public void update(Room room) {
        ifExistsSetNull(room);

        try {
            PreparedStatement replaceStudent = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "UPDATE room SET s_id = ? WHERE room_id = ?;"
                    );
            insertStudent(replaceStudent, 1, room.student());
            replaceStudent.setString(2, room.roomId());
            replaceStudent.execute();

            DormDBUpdater.logger.info(
                    String.format(
                            "Update room data %s successfully.", room
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete student of `room r1` from `room r2` table.
     * @param room living in `Room r1` instance of to be deleted student from `Room r2` table.
     */
    @Deprecated
    @Override
    public void delete(Room room) {
        if (!exists(room)) return;

        try {
            var deleteRoom = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "DELETE FROM room WHERE room_id = ?;"
                    );
            deleteRoom.setString(1, room.roomId());
            deleteRoom.execute();

            DormDBUpdater.logger.info(
                    String.format(
                            "Delete room data %s successfully.", room
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if student of `room r1` instance from student of living in `room r2` table different.
     * @param room To be checked student of room instance.
     * @return Does student of `room r1` instance from student of living in `room r2` table different?
     */
    private boolean isDifferentStudent(Room room) {
        try {
            String currentStudent = room.student() == null ?
                    null :
                    room.student().studentId();

            PreparedStatement getRoomId = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "SELECT s_id FROM room where room_id = ?;"
                    );
            getRoomId.setString(1, room.roomId());
            ResultSet r = getRoomId.executeQuery();

            if (!r.next()) return false; // has not select any room return false
            String inDBStudent = r.getString(1);

            return !isSame(inDBStudent, currentStudent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To Check data of get from database same.
     * @param a data1
     * @param b data2
     * @return Does data of get from database same?
     */
    private boolean isSame(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    /**
     * If student id is null set student of living room to null.
     * @param p PreparedStatement.
     * @param index To be Replaced string Location.
     * @param student Student instance.
     * @throws SQLException When set null or string exception.
     */
    private void insertStudent(PreparedStatement p, int index ,Student student) throws SQLException {
        if (student == null) {
            p.setNull(index, Types.CHAR);
        } else {
            p.setString(index, student.studentId());
        }
    }

    private void ifExistsSetNull(Room room) {
        try {
            if (room.student() == null) return;
            if (!exists(room)) return;

            PreparedStatement setToNull = DormDBUpdater.database
                    .getConnection()
                    .prepareStatement(
                            "UPDATE room SET s_id = ? where s_id = ?;"
                    );

            setToNull.setNull(1, Types.CHAR);
            setToNull.setString(2, room.student().studentId());
            setToNull.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
