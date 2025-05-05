import { get, writable } from "svelte/store";
import { Degree } from "./enums";

export interface Schedule {
    id: string;
    teacherId: string;
    disciplineId: string;
    classType: number;
    roomId: string;
    studentsId: string;
    dayOfWeek: number;
    startHour: number;
}

export let scheduleStore = writable<Schedule[]>([]);

export async function refreshSchedules() {
    const response = await fetch('http://localhost:8080/schedules', {
        method: 'GET'
    });

    if(!response.ok) {
        throw new Error(await response.json());
    }
    
    let result = await response.json();

    let array: Schedule[] = [];

    result.forEach((item: any) => {
        array.push({
            id: item.id,
            teacherId: item.teacherId,
            disciplineId: item.disciplineId,
            classType: item.classType,
            roomId: item.roomId,
            studentsId: item.studentsId,
            dayOfWeek: item.dayOfWeek,
            startHour: item.startHour
        });
    });

    scheduleStore.set(array.sort((a, b) => (a.dayOfWeek - b.dayOfWeek)*2 + (a.startHour - b.startHour)));
}

export async function createSchedule(schedule: Schedule) {
    const formData = new FormData();

    formData.append("teacherId", schedule.teacherId);
    formData.append("disciplineId", schedule.disciplineId);
    formData.append("classType", schedule.classType.toString());
    formData.append("roomId", schedule.roomId);
    formData.append("studentsId", schedule.studentsId);
    formData.append("dayOfWeek", schedule.dayOfWeek.toString());
    formData.append("startHour", schedule.startHour.toString());

    const response = await fetch(`http://localhost:8080/schedules`, {
        method: 'POST',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshSchedules();
}

export async function updateSchedules(schedule: Schedule) {
    const formData = new FormData();

    formData.append("teacherId", schedule.teacherId);
    formData.append("disciplineId", schedule.disciplineId);
    formData.append("classType", schedule.classType.toString());
    formData.append("roomId", schedule.roomId);
    formData.append("studentsId", schedule.studentsId);
    formData.append("dayOfWeek", schedule.dayOfWeek.toString());
    formData.append("startHour", schedule.startHour.toString());

    const response = await fetch(`http://localhost:8080/schedules/${schedule.id}`, {
        method: 'PATCH',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshSchedules();
}

export async function deleteSchedule(id: String) {
    const response = await fetch(`http://localhost:8080/schedules/${id}`, {
        method: 'DELETE',
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshSchedules();
}