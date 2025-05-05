import { get, writable } from "svelte/store";
import { Degree } from "./enums";
import { getTeacher } from "./teacher";

export interface TeacherDiscipline {
    id: string;
    teacherId: string;
    disciplineId: string;
}

export let teacherDisciplineStore = writable<TeacherDiscipline[]>([]);

export async function refreshTeacherDisciplines() {
    const response = await fetch('http://localhost:8080/teacherDisciplines', {
        method: 'GET'
    });

    if(!response.ok) {
        throw new Error(await response.json());
    }
    
    let result = await response.json();

    let array: TeacherDiscipline[] = [];

    result.forEach((item: any) => {
        array.push({
            id: item.id,
            teacherId: item.teacherId,
            disciplineId: item.disciplineId
        });
    });

    teacherDisciplineStore.set(array.sort((a, b) => 
        getTeacher(a.teacherId).name.localeCompare(getTeacher(b.teacherId).name)
    ));
}

export async function createTeacherDiscipline(teacherDiscipline: TeacherDiscipline) {
    const formData = new FormData();

    formData.append("teacherId", teacherDiscipline.teacherId);
    formData.append("disciplineId", teacherDiscipline.disciplineId);

    const response = await fetch(`http://localhost:8080/teacherDisciplines`, {
        method: 'POST',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshTeacherDisciplines();
}

export async function deleteTeacherDiscipline(id: String) {
    const response = await fetch(`http://localhost:8080/teacherDisciplines/${id}`, {
        method: 'DELETE',
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshTeacherDisciplines();
}