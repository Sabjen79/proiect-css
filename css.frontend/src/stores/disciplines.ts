import { get, writable } from "svelte/store";
import { Degree } from "./enums";

export interface Discipline {
    id: string;
    name: string;
    degree: number;
    year: number;
}

export let disciplineStore = writable<Discipline[]>([]);

export async function refreshDisciplines() {
    const response = await fetch('http://localhost:8080/disciplines', {
        method: 'GET'
    });

    if(!response.ok) {
        throw new Error(await response.json());
    }
    
    let result = await response.json();

    let array: Discipline[] = [];

    result.forEach((item: any) => {
        array.push({
            id: item.id,
            name: item.name,
            degree: item.degree,
            year: item.year
        });
    });

    disciplineStore.set(array.sort((a, b) => {
        if(a.degree != b.degree) return a.degree - b.degree;
        if(a.year != b.year) return a.year - b.year;
        return a.name.localeCompare(b.name);
    }));
}

export function getDisciplinesAsOptions() {
    return get(disciplineStore).map(d => {
        return { label: d.name, value: d.id }
    });
}

export function getDiscipline(id: string): Discipline {
    for(let d of get(disciplineStore)) {
        if(d.id == id) {
            return d;
        }
    }

    throw new Error("Not found");
}

export async function createDiscipline(discipline: Discipline) {
    const formData = new FormData();

    formData.append("name", discipline.name);
    formData.append("degree", discipline.degree.toString());
    formData.append("year", discipline.year.toString());

    const response = await fetch(`http://localhost:8080/disciplines`, {
        method: 'POST',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshDisciplines();
}

export async function updateDisciplines(discipline: Discipline) {
    const formData = new FormData();

    formData.append("name", discipline.name);
    formData.append("degree", discipline.degree.toString());
    formData.append("year", discipline.year.toString());

    const response = await fetch(`http://localhost:8080/disciplines/${discipline.id}`, {
        method: 'PATCH',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshDisciplines();
}

export async function deleteDiscipline(id: String) {
    const response = await fetch(`http://localhost:8080/disciplines/${id}`, {
        method: 'DELETE',
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshDisciplines();
}