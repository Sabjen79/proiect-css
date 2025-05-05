<script lang="ts">
    import { getContext } from "svelte";
    import type { TableHeader, ViewTableEntry } from "./table_data";
    import EditTableButton from "./edit_table_button.svelte";
    import { text } from "@sveltejs/kit";
    import { goto } from "$app/navigation";

    let {
        columns,
        index,
    }: {
        columns: ViewTableEntry[],
        index: number,
    } = $props();

    let data = getContext<TableHeader[]>('table-data');

    let hover = $state(false);
    let deleteHover = $state(false);
</script>

<div 
    class={`
        w-full h-10 flex items-center border-b-1 border-stone-400
        duration-200 hover:bg-blue-100
        ${index % 2 == 0 ? "bg-white" : "bg-stone-100"}
    `}
>
    {#each data as col, i}
        <!-- svelte-ignore a11y_click_events_have_key_events -->
        <!-- svelte-ignore a11y_no_noninteractive_element_interactions -->
        <p 
            class={`
                font-bold px-2 border-stone-200 truncate text-left
                ${i == 0 ? "" : "border-l-2"}
                ${columns[i].url == "" ? "text-stone-800" : "text-blue-600 hover:cursor-pointer"}
            `}
            onclick={() => {
                if(columns[i].url != "") goto(columns[i].url);
            }}
            style="flex: {col.flex};"
        >
            {columns[i].text}
        </p>
    {/each}
    </div>
