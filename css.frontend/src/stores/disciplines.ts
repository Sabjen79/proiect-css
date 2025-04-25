import { writable } from "svelte/store";
import type { EntityInterface } from "./entity";

export class Discipline implements EntityInterface {
    id: string
    name: string
    description: string

    constructor(id: string, name: string, description: string) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    getEditTableColumns(): string[] {
        return [
            this.id, this.name, this.description
        ];
    }
}

export let disciplineStore = writable<Discipline[]>([]);
