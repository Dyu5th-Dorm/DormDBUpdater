package org.dyu5thdorm.DormDBUpdater.repositories;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.RoomDataFetcher.models.Room;
import org.dyu5thdorm.RoomDataFetcher.models.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class RoomRepository implements DormitoryRepository<Room> {
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

    @Override
    public void update(Room room) {
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

    private boolean isSame(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private void insertStudent(PreparedStatement p, int index ,Student student) throws SQLException {
        if (student== null) {
            p.setNull(index, Types.CHAR);
        } else {
            p.setString(index, student.studentId());
        }
    }
}
