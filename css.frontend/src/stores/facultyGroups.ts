import { get, writable } from "svelte/store";
import { Degree } from "./enums";
import { getSemiYear } from "./semiYears";

export interface FacultyGroup {
    id: string;
    name: string;
    semiYearId: string;
}

export let facultyGroupStore = writable<FacultyGroup[]>([]);

export async function refreshFacultyGroups() {
    const response = await fetch('http://localhost:8080/facultyGroups', {
        method: 'GET'
    });

    if(!response.ok) {
        throw new Error(await response.json());
    }
    
    let result = await response.json();

    let array: FacultyGroup[] = [];

    result.forEach((item: any) => {
        array.push({
            id: item.id,
            name: item.name,
            semiYearId: item.semiYearId
        });
    });

    facultyGroupStore.set(
        array.sort((a, b) => {
            let aS = getSemiYear(a.semiYearId);
            let bS = getSemiYear(b.semiYearId);

            return (aS.degree - bS.degree)*5 + (aS.year - bS.year)*3 + a.name.localeCompare(b.name);
        })
    )
}

export function getFacultyGroupsAsOptions() {
    
    return get(facultyGroupStore).map(fg => {
        let sy = getSemiYear(fg.semiYearId);
        return { label: `${fg.name} - ${Degree[sy.degree]} Year ${sy.year}`, value: fg.id }
    });
}

export function getFacultyGroup(id: string): FacultyGroup {
    for(let fg of get(facultyGroupStore)) {
        if(fg.id == id) {
            return fg;
        }
    }

    throw new Error("Not found");
}

export function getFacultyGroupOrNull(id: string): FacultyGroup | null {
    for(let fg of get(facultyGroupStore)) {
        if(fg.id == id) {
            return fg;
        }
    }

    return null;
}

export async function createFacultyGroup(facultyGroup: FacultyGroup) {
    const formData = new FormData();

    formData.append("name", facultyGroup.name);
    formData.append("semiYearId", facultyGroup.semiYearId);

    const response = await fetch(`http://localhost:8080/facultyGroups`, {
        method: 'POST',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshFacultyGroups();
}

export async function updateFacultyGroups(facultyGroup: FacultyGroup) {
    const formData = new FormData();

    formData.append("name", facultyGroup.name);
    formData.append("semiYearId", facultyGroup.semiYearId);

    const response = await fetch(`http://localhost:8080/facultyGroups/${facultyGroup.id}`, {
        method: 'PATCH',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshFacultyGroups();
}

export async function deleteFacultyGroup(id: String) {
    const response = await fetch(`http://localhost:8080/facultyGroups/${id}`, {
        method: 'DELETE',
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshFacultyGroups();
}