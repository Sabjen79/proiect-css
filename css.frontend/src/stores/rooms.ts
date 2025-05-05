import { get, writable } from "svelte/store";
import { Degree } from "./enums";

export interface Room {
    id: string;
    name: string;
    capacity: number;
    roomType: number;
}

export let roomStore = writable<Room[]>([]);

export async function refreshRooms() {
    const response = await fetch('http://localhost:8080/rooms', {
        method: 'GET'
    });

    if(!response.ok) {
        throw new Error(await response.json());
    }
    
    let result = await response.json();

    let array: Room[] = [];

    result.forEach((item: any) => {
        array.push({
            id: item.id,
            name: item.name,
            capacity: item.capacity,
            roomType: item.roomType
        });
    });

    roomStore.set(array.sort((a, b) => a.name.localeCompare(b.name)));
}

export function getRoomsAsOption() {
    return get(roomStore).map(r => {
        return { label: r.name, value: r.id }
    });
}

export function getRoom(id: string): Room {
    for(let r of get(roomStore)) {
        if(r.id == id) {
            return r;
        }
    }

    throw new Error("Not found");
}

export async function createRoom(room: Room) {
    const formData = new FormData();

    formData.append("name", room.name);
    formData.append("capacity", room.capacity.toString());
    formData.append("roomType", room.roomType.toString());

    const response = await fetch(`http://localhost:8080/rooms`, {
        method: 'POST',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshRooms();
}

export async function updateRooms(room: Room) {
    const formData = new FormData();

    formData.append("name", room.name);
    formData.append("capacity", room.capacity.toString());
    formData.append("roomType", room.roomType.toString());

    const response = await fetch(`http://localhost:8080/rooms/${room.id}`, {
        method: 'PATCH',
        body:formData
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshRooms();
}

export async function deleteRoom(id: String) {
    const response = await fetch(`http://localhost:8080/rooms/${id}`, {
        method: 'DELETE',
    });

    if(!response.ok) {
        throw new Error(await response.text());
    }

    refreshRooms();
}