/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation.model;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Slf4j
public class ReservationAgent {

    private List<LectureRoom> lectureRoomList;

    public ReservationAgent(List<LectureRoom> lectureRoomList) {
        this.lectureRoomList = lectureRoomList;
    }

    public boolean canReserveRoom(String roomId, DaysOfWeek day, int fromPeriod, int toPeriod) {
        boolean available = true;

        // 강의 시간 & 사전 예약 확인
        // IEB916호실의 3,4교시 강의 존재 여부 확인
        Optional<LectureRoom> roomInfo = this.getLectureRoom(roomId);
        if (roomInfo.isPresent()) {
            for (int period = fromPeriod; period <= toPeriod; period++) {
                available = available && !roomInfo.get().occupied(day, period);
            }
        } else {
            log.warn("해당 강의실이 없습니다: {}", roomId);
            available = false;
        }
        String[] days = {"월", "화", "수", "목", "금", "토", "일"};
        log.info("{}: {}요일 {}교시부터 {}교시까지 사용 여부 = {}", 
                roomId, days[day.index()], fromPeriod, toPeriod, available);
        return available;
    }

    public boolean makeReservation(String roomId, DaysOfWeek day, int fromPeriod, int toPeriod, String userName) {
        boolean available = true;

        if (canReserveRoom(roomId, day, fromPeriod, toPeriod)) {
            Optional<LectureRoom> roomInfo = this.getLectureRoom(roomId);
            if (roomInfo.isPresent()) {
                LectureRoom room = roomInfo.get();
                room.addReservation(DaysOfWeek.TUESDAY, fromPeriod, toPeriod, userName);
                log.info("room = {}", room);
            }
        } else {
            available = false;
        }

        return available;
    }

    /**
     * lectureRoomList에서 roomId에 해당하는 강의실 객체를 찾아서 반환한다.
     *
     * @param roomId 찾으려는 강의실 ID
     * @return 찾으려는 강의실 객체를 반환한다. Optional을 사용하여 NULL 포인터 역참조 문제를 해결하였음.
     */
    private Optional<LectureRoom> getLectureRoom(String roomId) {
        Optional<LectureRoom> room = Optional.empty();
        for (LectureRoom r : lectureRoomList) {
            if (r.getId().equals(roomId)) {
                room = Optional.of(r);
                break;
            }
        }
        return room;
    }

}
