export enum Degree {
    Bachelors = 0,
    Master = 1
}

export enum RoomType {
    Course = 0,
    Laboratory = 1
}

export enum DayOfWeek {
    Monday = 0,
    Tuesday = 1,
    Wednesday = 2,
    Thursday = 3,
    Friday = 4
}

export enum ClassType {
    Course = 0,
    Laboratory = 1,
    Seminary = 2
}

export function getEnumAsOptions(e: any): { label: string, value: number }[] {
    return Object.keys(e)
        .filter(key => isNaN(Number(key)))
        .map(key => ({ label: key, value: e[key] }));
}