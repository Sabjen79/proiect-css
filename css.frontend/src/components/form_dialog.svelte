<script lang="ts">
    import type { Snippet } from "svelte";
    import Dialog from "./dialog.svelte";
    import Button from "./button.svelte";

    let {
        name,
        onSubmitEdit,
        onSubmitCreate,
        children
    } : {
        name: string
        onSubmitEdit: Function,
        onSubmitCreate: Function
        children: Snippet
    } = $props();

    let dialog: Dialog;
    let error = $state("");
    let type = $state(0);

    export function setError(err: string) {
        error = err;
    }

    export function openCreate() { 
        type = 0;
        error = "";
        dialog.open();
    }

    export function openEdit() { 
        type = 1;
        error = "";
        dialog.open();
    }

    export function openDelete(err: string) { 
        type = 2;
        error = err;
        dialog.open();
    }

    export function close() { dialog.close() }
</script>

<Dialog bind:this={dialog} title="{type == 2 ? "Delete" : type == 1 ? "Edit" : "Create"} {name}">
    
    <div class="w-80 flex flex-col gap-2">
        <div class="w-full bg-red-300/20 rounded-md border-1 border-red-500 text-red-500 font-semibold break-words px-2 {error == "" ? "hidden" : ""}">
            {error}
        </div>
        {#if type != 2}
            {@render children()}
            <div class="w-full flex justify-end mt-4">
                <Button label="Submit" onClick={() => {type == 0 ? onSubmitCreate() : onSubmitEdit()}}/>
            </div>
        {/if}
    </div>
    
</Dialog>