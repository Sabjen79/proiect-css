<script lang="ts" generics="T">
    import { setContext, type Snippet } from "svelte";
    import type { TableHeader } from "./table_data";
    import type { EntityInterface } from "../../stores/entity";
    import EditTableItem from "./edit_table_item.svelte";

    let {
        items,
        header,
        columns,
        onCreate,
        onEdit,
        onDelete,
    }: {
        items: T[]
        header: TableHeader[]
        columns: (item: T) => string[]
        onCreate: () => void
        onEdit: (item: T) => void
        onDelete: (item: T) => void
    } = $props();

    setContext('table-data', header);
</script>

<div class={`
    w-full h-full relative
    bg-stone-800 rounded-2xl shadow-[0_2px_5px_2px_#00000040]
    overflow-clip
`}>
    <button 
        class={`
            absolute right-4 top-1.5 z-10
            font-icons p-1 text-xl
            rounded-full duration-200
           bg-blue-300 text-black
            hover:cursor-pointer hover:text-blue-500
        `}
        onclick={() => onCreate()}
    >
        add
    </button>
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
            <EditTableItem 
                {index}
                columns={columns(obj)}
                onEdit={() => {onEdit(obj)}}
                onDelete={() => {onDelete(obj)}}
            />
        {/each}
    </div>
</div>