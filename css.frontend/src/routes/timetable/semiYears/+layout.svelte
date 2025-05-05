<script lang="ts">
    import { onMount } from 'svelte';
    import SidebarButton from '../../../components/panel/sidebar_button.svelte';
    import { disciplineStore, refreshDisciplines } from '../../../stores/disciplines';
    import { refreshTeachers, teacherStore } from '../../../stores/teacher';
    import { refreshSemiYears, semiYearStore } from '../../../stores/semiYears';
    import { refreshSchedules } from '../../../stores/schedule';
    import { refreshRooms, roomStore } from '../../../stores/rooms';
    import { refreshFacultyGroups } from '../../../stores/facultyGroups';
    import { refreshAllStores } from '../../../stores/entity';
    import { Degree } from '../../../stores/enums';

	let { children } = $props();
</script>

<div class="w-full h-full flex overflow-clip">
    <div class="w-70 h-full bg-blue-200 flex flex-col items-center px-2 pt-2 shadow-[0_0_8px_-1px_#00000070] overflow-scroll pb-10">
        {#each $semiYearStore as semiYear}
            <SidebarButton
                name={`${semiYear.name} - Year ${semiYear.year}`}
                icon="school"
                color="var(--color-cyan-500)"
                url={`/timetable/semiYears/${semiYear.id}`}
            />
        {/each}
    </div>
	<div class="w-full h-full">
		{@render children()}
	</div>
</div>