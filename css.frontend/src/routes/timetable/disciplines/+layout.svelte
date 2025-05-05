<script lang="ts">
    import { onMount } from 'svelte';
    import SidebarButton from '../../../components/panel/sidebar_button.svelte';
    import { disciplineStore, refreshDisciplines } from '../../../stores/disciplines';
    import { refreshTeachers } from '../../../stores/teacher';
    import { refreshSemiYears } from '../../../stores/semiYears';
    import { refreshSchedules } from '../../../stores/schedule';
    import { refreshRooms } from '../../../stores/rooms';
    import { refreshFacultyGroups } from '../../../stores/facultyGroups';
    import { refreshAllStores } from '../../../stores/entity';

	let { children } = $props();
</script>

<div class="w-full h-full flex overflow-clip">
    <div class="w-70 h-full bg-blue-200 flex flex-col items-center px-2 pt-2 shadow-[0_0_8px_-1px_#00000070] overflow-scroll pb-10">
        {#each $disciplineStore as discipline}
            <SidebarButton
                name={discipline.name}
                icon="book"
                color="var(--color-green-500)"
                url={`/timetable/disciplines/${discipline.id}`}
            />
        {/each}
    </div>
	<div class="w-full h-full">
		{@render children()}
	</div>
</div>