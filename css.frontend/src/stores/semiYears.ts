import { get, writable } from "svelte/store";
import { Degree } from "./enums";

export interface SemiYear {
    id: string;
    name: string;
    degree: number;
    year: number;
}

export let semiYearStore = writable<SemiYear[]>([]);

export async function refreshSemiYears() {
    const response = await fetch('http://localhost:8080/semiYears', {
        method: 'GET'
    });

    if(!response.ok) {
        throw new Error(await response.json());
    }
    
    let result = await response.json();

    let array: SemiYear[] = [];

    result.forEach((item: any) => {
        array.push({
            id: item.id,
            name: item.name,
            degree: item.degree,
            year: item.year
        });
    });

    semiYearStore.set(array.sort((a, b) => {
        if(a.degree != b.degree) return a.degree - b.degree;
            if(a.year != b.year) return a.year - b.year;
            return a.name.localeCompare(b.name);
    }));
}

export function getSemiYearsAsOptions() {
    return get(semiYearStore).map(sy => {
        return { label: `${sy.name} - ${Degree[sy.degree]} Year ${sy.year}`, value: sy.id }
    });
}

export function getSemiYear(id: string): SemiYear {
    for(let sy of get(semiYearStore)) {
        if(sy.id == id) {
            return sy;
        }
    }

    throw new Error("Not found");
}

export function getSemiYearOrNull(id: string): SemiYear | null {
    for(let sy of get(semiYearStore)) {
        if(sy.id == id) {
            return sy;
        }
    }

    return null;
}

export async function createSemiYear(semiYear: SemiYear) {
    const formData = new FormData();

    formData.append("name", semiYear.name);
    formData.append("degree", semiYear.degree.toString());
    formData.append("year", semiYear.year.toString());

    const response = await fetch(`http://localhost:8080/semiYears`, {
        method: 'POST',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshSemiYears();
}

export async function updateSemiYears(semiYear: SemiYear) {
    const formData = new FormData();

    formData.append("name", semiYear.name);
    formData.append("degree", semiYear.degree.toString());
    formData.append("year", semiYear.year.toString());

    const response = await fetch(`http://localhost:8080/semiYears/${semiYear.id}`, {
        method: 'PATCH',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshSemiYears();
}

export async function deleteSemiYear(id: String) {
    const response = await fetch(`http://localhost:8080/semiYears/${id}`, {
        method: 'DELETE',
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshSemiYears();
}