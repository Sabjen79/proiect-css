<script lang="ts">
    import { setContext } from "svelte";
    import type { TableHeader, ViewTableEntry } from "./table_data";
    import ViewTableItem from "./view_table_item.svelte";
    import type { Schedule } from "../../stores/schedule";
    import { DayOfWeek } from "../../stores/enums";

    let {
        items,
        header,
        columns,
    }: {
        items: Schedule[]
        header: TableHeader[]
        columns: (item: Schedule) => ViewTableEntry[]
    } = $props();

    setContext('table-data', header);
</script>

<div class={`
    w-full h-full relative
    bg-stone-800 rounded-2xl shadow-[0_2px_5px_2px_#00000040]
    overflow-clip
`}>
    <div class="relative h-10 shadow-[0_2px_5px_2px_#00000040] flex items-center">
        {#each header as col, index}
            <div 
                class={`
                    text-stone-50 font-bold px-2 border-stone-200
                    ${index == 0 ? "" : "border-l-2"}
                `}
                style="flex: {col.flex};"
            >
                {col.name}
            </div>
        {/each}
    </div>
    <div class="w-full h-full overflow-y-scroll bg-white pb-10 relative">
        {#each items as obj, index}
            {#if index == 0 || items[index - 1].dayOfWeek != obj.dayOfWeek}
                <div class="w-full h-8 flex items-center text-md font-semibold pl-2 bg-stone-700 text-stone-50 select-none">
                    {DayOfWeek[obj.dayOfWeek]}
                </div>
            {/if}
            
            <ViewTableItem 
                {index}
                columns={columns(obj)}
            />
        {/each}
    </div>
</div>