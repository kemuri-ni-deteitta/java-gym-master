package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);


        // Проверяем понедельник
        List<TrainingSession> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertSame(singleTrainingSession, mondaySessions.get(0));

        // Проверяем вторник
        List<TrainingSession> tuesdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertTrue(tuesdaySessions.isEmpty());

    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertSame(mondayChildTrainingSession, mondaySessions.get(0));


        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        Assertions.assertEquals(2, thursdaySessions.size());

        Assertions.assertEquals(
                new TimeOfDay(13, 0),
                thursdaySessions.get(0).getTimeOfDay()
        );

        Assertions.assertEquals(
                new TimeOfDay(20, 0),
                thursdaySessions.get(1).getTimeOfDay()
        );


        // Проверить, что за вторник не вернулось занятий

        // Проверяем вторник
        List<TrainingSession> tuesdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertTrue(tuesdaySessions.isEmpty());


    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        // Проверяем понедельник в 13:00
        List<TrainingSession> sessionsAt13 =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY,
                        new TimeOfDay(13, 0)
                );

        // Проверяем, что возвращается одно
        Assertions.assertEquals(1, sessionsAt13.size());
        Assertions.assertSame(singleTrainingSession, sessionsAt13.get(0));


        //Проверить, что за понедельник в 14:00 не вернулось занятий

        List<TrainingSession> sessionsAt14 =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY,
                        new TimeOfDay(14, 0)
                );

        Assertions.assertTrue(sessionsAt14.isEmpty());
    }

    }


