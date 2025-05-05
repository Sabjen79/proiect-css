<script lang="ts">
    import { page } from '$app/state';
    import { onMount } from 'svelte';
    import ViewTable from "../../../../components/table/view_table.svelte";
    import { getDiscipline, type Discipline } from "../../../../stores/disciplines";
    import { ClassType, Degree } from '../../../../stores/enums';
    import { getFacultyGroup } from '../../../../stores/facultyGroups';
    import { getRoom } from '../../../../stores/rooms';
    import { scheduleStore, type Schedule } from "../../../../stores/schedule";
    import { getSemiYear } from '../../../../stores/semiYears';
    import { getTeacher } from '../../../../stores/teacher';
    import { refreshAllStores } from '../../../../stores/entity';
    
    let discipline: Discipline = $derived(getDiscipline(page.params.id));

    let list: Schedule[] = $derived($scheduleStore
            .filter(s => s.disciplineId == discipline.id)
    );
</script>

<div class="w-full h-full flex justify-center items-center">
    
    <div class="max-w-300 w-[90%] h-full py-10 relative">
        <p class="absolute -left-4 top-2.5 font-semibold">{discipline.name}</p>
        <ViewTable 
            items={list}
            header={[
                { name: "Time", flex: 1 },
                { name: "Teacher", flex: 4 },
                { name: "Type", flex: 4 },
                { name: "Students", flex: 4 },
                { name: "Room", flex: 4 },
            ]}
            columns={(item) => {
                let t = getTeacher(item.teacherId);
                let d = getDiscipline(item.disciplineId);
                let r = getRoom(item.roomId);
                let st = (item.classType === ClassType.Course) ? getSemiYear(item.studentsId) : getFacultyGroup(item.studentsId);

                let displayGroup = ('degree' in st) 
                    ? `${st.name} - ${Degree[st.degree]} Year ${st.year}` 
                    : `${st.name} - ${Degree[getSemiYear(st.semiYearId).degree]} Year ${getSemiYear(st.semiYearId).year}`;

                let groupUrl = ('degree' in st)
                    ? `/timetable/semiYears/${st.id}`
                    : `/timetable/facultyGroups/${st.id}`
                    
                return [
                    { text: `${item.startHour}:00`, url: "" },
                    { text: `${t.title} ${t.name}`, url: `/timetable/teachers/${t.id}` },
                    { text: ClassType[item.classType], url: "" },
                    { text: displayGroup, url: groupUrl },
                    { text: r.name, url: `/timetable/rooms/${r.id}` }
                ];
            }}
        />
    </div>
</div>