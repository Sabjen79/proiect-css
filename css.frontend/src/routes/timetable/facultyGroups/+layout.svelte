<script lang="ts">
    import { onMount } from 'svelte';
    import SidebarButton from '../../../components/panel/sidebar_button.svelte';
    import { disciplineStore, refreshDisciplines } from '../../../stores/disciplines';
    import { refreshTeachers, teacherStore } from '../../../stores/teacher';
    import { getSemiYear, refreshSemiYears, semiYearStore } from '../../../stores/semiYears';
    import { refreshSchedules } from '../../../stores/schedule';
    import { refreshRooms, roomStore } from '../../../stores/rooms';
    import { facultyGroupStore, refreshFacultyGroups } from '../../../stores/facultyGroups';
    import { refreshAllStores } from '../../../stores/entity';

	let { children } = $props();
</script>

<div class="w-full h-full flex overflow-clip">
    <div class="w-70 h-full bg-blue-200 flex flex-col items-center px-2 pt-2 shadow-[0_0_8px_-1px_#00000070] overflow-scroll pb-10">
        {#each $facultyGroupStore as facultyGroup}
            <SidebarButton
                name={`${facultyGroup.name} - Year ${getSemiYear(facultyGroup.semiYearId).year}`}
                icon="groups"
                color="var(--color-blue-500)"
                url={`/timetable/facultyGroups/${facultyGroup.id}`}
            />
        {/each}
    </div>
	<div class="w-full h-full">
		{@render children()}
	</div>
</div>