<script lang="ts">
    import { getContext } from "svelte";
    import EditTableButton from "./edit_table_button.svelte";
    import type { TableHeader } from "./table_data";

    let {
        columns,
        index,
        onEdit,
        onDelete,
    }: {
        columns: string[],
        index: number,
        onEdit: Function,
        onDelete: Function
    } = $props();

    let data = getContext<TableHeader[]>('table-data');

    let hover = $state(false);
    let deleteHover = $state(false);
</script>

<!-- svelte-ignore a11y_no_static_element_interactions -->
<button 
    class={`
        w-full h-10 flex items-center border-b-1 border-stone-400
        duration-200 hover:bg-blue-100 hover:cursor-pointer
        ${index % 2 == 0 ? "bg-white" : "bg-stone-100"}
    `}
    onmouseenter={() => { hover = true  }}
    onmouseleave={() => { hover = false }}
    onclick={() => {
        if(deleteHover)
            onDelete();
        else
            onEdit();
    }}
>
    {#each data as col, i}
        <p 
            class={`
                text-stone-800 font-bold px-2 border-stone-200 truncate text-left
                ${i == 0 ? "" : "border-l-2"}
            `}
            style="flex: {col.flex};"
        >
            {columns[i]}
        </p>
    {/each}

    <div class={`absolute right-4 duration-100 ${hover ? "opacity-100" : "opacity-0"}`}>
        <EditTableButton icon="delete" color="var(--color-red-500)" bind:hover={deleteHover}/>
    </div>
</button>
