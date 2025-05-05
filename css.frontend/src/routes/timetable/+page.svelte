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
            name="Teachers"
            icon="group"
            color="var(--color-amber-500)"
            url="/teachers"
        />
        <PanelButton
            name="Disciplines"
            icon="book"
            color="var(--color-green-500)"
            url="/disciplines"
        />
        <PanelButton
            name="Rooms"
            icon="meeting_room"
            color="var(--color-orange-500)"
            url="/rooms"
        />
    </div>
    <div
        class={`
            flex justify-center items-center
            w-[80%] m-auto
        `}
    >
        <PanelButton
            name="Semi-Years"
            icon="school"
            color="var(--color-cyan-500)"
            url="/semiYears"
        />
        <PanelButton
            name="Groups"
            icon="groups"
            color="var(--color-blue-500)"
            url="/facultyGroups"
        />
    </div>
</div>
