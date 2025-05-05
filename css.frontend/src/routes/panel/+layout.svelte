<script lang="ts">
    import SidebarButton from '../../components/panel/sidebar_button.svelte';

	let { children } = $props();
</script>

<div class="w-full h-full flex">
    <div class="w-70 h-full bg-blue-200 flex flex-col items-center px-2 pt-2 shadow-[0_0_8px_-1px_#00000070]">
        <SidebarButton
            name="Schedule"
            icon="calendar_month"
            color="var(--color-blue-500)"
            url="/panel/schedule"
        />
        <SidebarButton
            name="Teachers"
            icon="group"
            color="var(--color-green-500)"
            url="/panel/teachers"
        />
        <SidebarButton
            name="Disciplines"
            icon="article"
            color="var(--color-green-500)"
            url="/panel/disciplines"
        />
        <SidebarButton
            name="Teacher-Disciplines"
            icon="article_person"
            color="var(--color-green-500)"
            url="/panel/teacherDisciplines"
        />
        <SidebarButton
            name="Rooms"
            icon="meeting_room"
            color="var(--color-amber-500)"
            url="/panel/rooms"
        />
        <SidebarButton
            name="Semi-Years"
            icon="school"
            color="var(--color-cyan-500)"
            url="/panel/semiYears"
        />
        <SidebarButton
            name="Faculty Groups"
            icon="groups"
            color="var(--color-cyan-500)"
            url="/panel/facultyGroups"
        />
    </div>
	<div class="w-full h-full">
		{@render children()}
	</div>
</div>