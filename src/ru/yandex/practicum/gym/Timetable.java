package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    // Инициализируем HashMap, в котором будет храниться расписание по дням недели
    public Timetable() {

        timetable = new HashMap<>();

    }

    public void addNewTrainingSession(TrainingSession trainingSession) {

        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        // 1. Если такого дня ещё нет — создаём TreeMap для этого дня
        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<>());
        }

        // 2. Получаем расписание конкретного дня
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        // 3. Если такого времени ещё нет — создаём список тренировок
        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }

        // 4. Получаем список тренировок на это время
        List<TrainingSession> sessionsAtTime = daySchedule.get(time);

        // 5. Добавляем новую тренировку
        sessionsAtTime.add(trainingSession);
    }

    /*
    Поиск расписания нужного дня в HashMap выполняется в среднем за O(1).
    Но дальше мы проходим по всем временным слотам этого дня и складываем
    все TrainingSession в новый список, поэтому итоговая сложность метода O(n),
    где n - количество тренировок в выбранный день.
    Если требуется O(1) для всего метода, можно дополнительно хранить в классе
    готовый список тренировок для каждого дня и обновлять его при добавлении занятий
    */
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {

        // Получаем расписание выбранного дня
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        // Если в этот день тренировок нет, возвращаем пустой список
        if (daySchedule == null) {
            return new ArrayList<>();
        }

        // Создаем список для всех тренировок этого дня
        List<TrainingSession> trainingSessions = new ArrayList<>();

        // Добавляем тренировки в порядке времени
        for (List<TrainingSession> sessionsAtTime : daySchedule.values()) {
            trainingSessions.addAll(sessionsAtTime);
        }

        return trainingSessions;
    }

    /*
    Поиск нужного дня в HashMap выполняется в среднем за O(1).
    Дальше поиск нужного времени выполняется в TreeMap за O(log n),
    где n - количество временных слотов в выбранный день.
    Поэтому итоговая сложность метода O(log n).

    Если требуется O(1) для всего метода, можно дополнительно хранить в классе
    HashMap<DayOfWeek, HashMap<TimeOfDay, List<TrainingSession>>> для быстрого поиска
    по дню и времени.

    Но тогда появится дублирование данных: придется одновременно поддерживать
    TreeMap для хранения тренировок в отсортированном по времени виде и HashMap
    для поиска за O(1). При добавлении тренировки нужно будет обновлять обе структуры,
    иначе данные в них могут перестать соответствовать друг другу.
    */

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {

        // Получаем расписание выбранного дня
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        // Если для этого дня расписания нет, возвращаем пустой список
        if (daySchedule == null) {
            return new ArrayList<>();
        }

        // Получаем список тренировок на выбранное время
        List<TrainingSession> sessionsAtTime = daySchedule.get(timeOfDay);

        // Если в это время тренировок нет, возвращаем пустой список
        if (sessionsAtTime == null) {
            return new ArrayList<>();
        }

        return sessionsAtTime;
    }

    public List<CounterOfTrainings> getCountByCoaches() {

        // Храним количество тренировок для каждого тренера
        HashMap<Coach, Integer> countByCoaches = new HashMap<>();

        // Проходим по расписанию каждого дня
        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {

            // Проходим по спискам тренировок для каждого времени
            for (List<TrainingSession> sessionsAtTime : daySchedule.values()) {

                // Проходим по каждой тренировке
                for (TrainingSession trainingSession : sessionsAtTime) {

                    Coach coach = trainingSession.getCoach();

                    // Получаем текущее количество тренировок тренера
                    int currentCount = countByCoaches.getOrDefault(coach, 0);

                    // Увеличиваем количество тренировок на одну
                    countByCoaches.put(coach, currentCount + 1);
                }
            }
        }

        // Создаем итоговый список
        List<CounterOfTrainings> result = new ArrayList<>();

        // Преобразуем пары Coach -> количество в CounterOfTrainings
        for (Map.Entry<Coach, Integer> entry : countByCoaches.entrySet()) {

            CounterOfTrainings counter = new CounterOfTrainings(entry.getKey(), entry.getValue());

            result.add(counter);
        }

        // Сортируем по количеству тренировок по убыванию
        result.sort((first, second) -> Integer.compare(second.getCount(), first.getCount()));

        return result;
    }


}
