import { refreshDisciplines } from "./disciplines";
import { refreshFacultyGroups } from "./facultyGroups";
import { refreshRooms } from "./rooms";
import { refreshSchedules } from "./schedule";
import { refreshSemiYears } from "./semiYears";
import { refreshTeachers } from "./teacher";
import { refreshTeacherDisciplines } from "./teacherDisciplines";

export interface EntityInterface {
    getEditTableColumns(): string[];
}

export async function refreshAllStores() {
    await refreshTeachers();
    await refreshDisciplines();
    await refreshTeacherDisciplines();
    await refreshRooms();
    await refreshSemiYears();
    await refreshFacultyGroups();
    await refreshSchedules();
}