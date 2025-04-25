<script lang="ts">
    import SidebarButton from '../../components/panel/sidebar_button.svelte';

	let { children } = $props();
</script>

<div class="w-full h-full flex">
    <div class="w-50 h-full bg-amber-100 flex flex-col items-center px-2 pt-2 shadow-[0_0_8px_-1px_#00000070]">
        <SidebarButton
            name="Timetable"
            icon="calendar_month"
            color="var(--color-yellow-500)"
            url="timetable"
        />
        <SidebarButton
            name="Teachers"
            icon="group"
            color="var(--color-amber-500)"
            url="teachers"
        />
        <SidebarButton
            name="Disciplines"
            icon="book"
            color="var(--color-green-500)"
            url="disciplines"
        />
        <SidebarButton
            name="Rooms"
            icon="meeting_room"
            color="var(--color-blue-500)"
            url="rooms"
        />
        <SidebarButton
            name="Study Years"
            icon="school"
            color="var(--color-cyan-500)"
            url="years"
        />
    </div>
	<div class="w-full h-full">
		{@render children()}
	</div>
</div>