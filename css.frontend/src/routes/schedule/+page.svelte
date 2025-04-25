<script lang="ts">
    import PanelButton from "../../components/panel/color_card.svelte";
</script>

<div>
    <div
        class={`
        flex justify-center items-center
        w-[80%] m-auto mt-10
    `}>
        <PanelButton
            name="Full Timetable"
            icon="calendar_month"
            color="var(--color-yellow-500)"
            url="/timetable"
        />
        <PanelButton
            name="Teachers"
            icon="group"
            color="var(--color-amber-600)"
            url="/teachers"
        />
        <PanelButton
            name="Disciplines"
            icon="book"
            color="var(--color-green-500)"
            url="/disciplines"
        />
    </div>
    <div
        class={`
            flex justify-center items-center
            w-[80%] m-auto
        `}
    >
        <PanelButton
            name="Rooms"
            icon="meeting_room"
            color="var(--color-blue-500)"
            url="/rooms"
        />
        <!-- <PanelButton
            name="Room Types"
            icon="room_preferences"
            color="var(--color-blue-600)"
            url="/room_types"
        /> -->
        <PanelButton
            name="Study Years"
            icon="school"
            color="var(--color-cyan-500)"
            url="/years"
        />
        <!-- <PanelButton
            name="Time Slots"
            icon="schedule"
            color="var(--color-cyan-600)"
            url="/time_slots"
        /> -->
    </div>
</div>
