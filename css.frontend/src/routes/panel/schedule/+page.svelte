<script lang="ts">
    import { onMount } from "svelte";
    import FormDialog from "../../../components/form_dialog.svelte";
    import Input from "../../../components/input.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import { getDiscipline, getDisciplinesAsOptions, refreshDisciplines } from "../../../stores/disciplines";
    import { ClassType, DayOfWeek, Degree, getEnumAsOptions } from "../../../stores/enums";
    import Select from "../../../components/select.svelte";
    import { createSchedule, deleteSchedule, refreshSchedules, scheduleStore, updateSchedules, type Schedule } from "../../../stores/schedule";
    import { getSemiYear, getSemiYearsAsOptions, refreshSemiYears } from "../../../stores/semiYears";
    import { getTeacher, getTeachersAsOptions, refreshTeachers } from "../../../stores/teacher";
    import { getRoom, getRoomsAsOption, refreshRooms } from "../../../stores/rooms";
    import { getFacultyGroup, getFacultyGroupsAsOptions, refreshFacultyGroups } from "../../../stores/facultyGroups";
    import { refreshAllStores } from "../../../stores/entity";

    let editDialog: FormDialog;

    let formSchedule: Schedule = $state({
        id: "",
        teacherId: "",
        disciplineId: "",
        classType: 0,
        roomId: "",
        studentsId: "",
        dayOfWeek: 0,
        startHour: 0
    })
</script>

<FormDialog
    bind:this={editDialog}
    name={"Faculty Group"}
    onSubmitCreate={async () => {
        createSchedule(formSchedule).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
    onSubmitEdit={async () => {
        updateSchedules(formSchedule).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
>
    <Select 
        bind:value={formSchedule.teacherId} 
        label="Teacher"
        options={getTeachersAsOptions()}
    />
    <Select 
        bind:value={formSchedule.disciplineId} 
        label="Discipline"
        options={getDisciplinesAsOptions()}
    />

    <Select 
        bind:value={formSchedule.classType} 
        label="Class Type"
        options={getEnumAsOptions(ClassType)}
    />

    {#if formSchedule.classType === ClassType.Course}
        <Select 
            bind:value={formSchedule.studentsId} 
            label="Semi-Year"
            options={getSemiYearsAsOptions()}
        />
    {:else}
        <Select 
            bind:value={formSchedule.studentsId} 
            label="Group"
            options={getFacultyGroupsAsOptions()}
        />
    {/if}
    <Select 
        bind:value={formSchedule.roomId} 
        label="Room"
        options={getRoomsAsOption()}
    />
    <Select 
        bind:value={formSchedule.dayOfWeek} 
        label="Day of Week"
        options={getEnumAsOptions(DayOfWeek)}
    />
    <Input 
        bind:value={formSchedule.startHour} 
        label="Hour"
    />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            items={$scheduleStore}
            header={[
                { name: "Date", flex: 1},
                { name: "Teacher", flex: 1 },
                { name: "Discipline", flex: 2 },
                { name: "Group/SemiYear", flex: 2 },
                { name: "Class Type", flex: 1 },
                { name: "Room", flex: 1 }
            ]}
            columns={(item) => {
                let t = getTeacher(item.teacherId);
                let d = getDiscipline(item.disciplineId);
                let r = getRoom(item.roomId);
                let st = (item.classType === ClassType.Course) ? getSemiYear(item.studentsId) : getFacultyGroup(item.studentsId);

                let displayGroup = ('degree' in st) 
                    ? `${st.name} - ${Degree[st.degree]} Year ${st.year}` 
                    : `${st.name} - ${Degree[getSemiYear(st.semiYearId).degree]} Year ${getSemiYear(st.semiYearId).year}`;

                return [
                    `${DayOfWeek[item.dayOfWeek]} ${item.startHour}:00`,
                    `${t.title} ${t.name}`,
                    d.name,
                    displayGroup,
                    ClassType[item.classType],
                    r.name,
                ]
            }}
            onCreate={() => {
                Object.assign(formSchedule, {
                    id: "",
                    teacherId: "",
                    disciplineId: "",
                    classType: 0,
                    roomId: "",
                    studentsId: "",
                    dayOfWeek: 0,
                    startHour: 0
                });

                editDialog.openCreate();
            }}
            onEdit={(item) => {
                Object.assign(formSchedule, item);

                editDialog.openEdit();
            }}
            onDelete={(item) => {
                deleteSchedule(item.id).catch((err) => {
                    editDialog.openDelete(err.toString());
                })
            }}
        />
    </div>
</div>