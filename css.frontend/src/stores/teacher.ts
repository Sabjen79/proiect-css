import { get, writable } from "svelte/store";
import { Degree } from "./enums";

export interface Teacher {
    id: string;
    name: string;
    title: string;
}

export let teacherStore = writable<Teacher[]>([]);

export async function refreshTeachers() {
    const response = await fetch('http://localhost:8080/teachers', {
        method: 'GET'
    });

    if(!response.ok) {
        throw new Error(await response.json());
    }
    
    let result = await response.json();

    let array: Teacher[] = [];

    result.forEach((item: any) => {
        array.push({
            id: item.id,
            name: item.name,
            title: item.title
        });
    });

    teacherStore.set(array.sort((a, b) => a.name.localeCompare(b.name)))
}

export function getTeachersAsOptions() {
    return get(teacherStore).map(t => {
        return { label: t.name, value: t.id }
    });
}

export function getTeacher(id: string): Teacher {
    for(let t of get(teacherStore)) {
        if(t.id == id) {
            return t;
        }
    }

    throw new Error("Not found");
}

export async function createTeacher(teacher: Teacher) {
    const formData = new FormData();

    formData.append("name", teacher.name);
    formData.append("title", teacher.title);

    const response = await fetch(`http://localhost:8080/teachers`, {
        method: 'POST',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshTeachers();
}

export async function updateTeacher(teacher: Teacher) {
    const formData = new FormData();

    formData.append("name", teacher.name);
    formData.append("title", teacher.title);

    const response = await fetch(`http://localhost:8080/teachers/${teacher.id}`, {
        method: 'PATCH',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshTeachers();
}

export async function deleteTeacher(id: String) {
    const response = await fetch(`http://localhost:8080/teachers/${id}`, {
        method: 'DELETE',
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshTeachers();
}