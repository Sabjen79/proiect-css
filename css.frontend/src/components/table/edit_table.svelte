<script lang="ts">
    import { setContext, type Snippet } from "svelte";
    import type { EditTableData } from "./edit_table_data";
    import type { EntityInterface } from "../../stores/entity";
    import EditTableItem from "./edit_table_item.svelte";

    let {
        items,
        columns,
        onEdit,
        onDelete,
    }: {
        items: EntityInterface[]
        columns: EditTableData[]
        onEdit: (item: EntityInterface) => void
        onDelete: (item: EntityInterface) => void
    } = $props();

    setContext('table-data', columns);
</script>

<div class={`
    w-full h-full
    bg-stone-800 rounded-2xl shadow-[0_2px_5px_2px_#00000040]
    overflow-clip
`}>
    <div class="relative h-10 shadow-[0_2px_5px_2px_#00000040] flex items-center">
        {#each columns as col, index}
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
                columns={obj.getEditTableColumns()}
                onEdit={() => {onEdit(obj)}}
                onDelete={() => {onDelete(obj)}}
            />
        {/each}
    </div>
</div>