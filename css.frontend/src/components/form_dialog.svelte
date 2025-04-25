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
    let isCreate = $state(false);

    export function setError(err: string) {
        error = err;
    }

    export function openCreate() { 
        isCreate = true;
        error = "";
        dialog.open();
    }

    export function openEdit() { 
        isCreate = false;
        error = "";
        dialog.open();
    }

    export function close() { dialog.close() }
</script>

<Dialog bind:this={dialog} title="{isCreate ? "Create" : "Edit"} {name}">
    
    <div class="w-80 flex flex-col gap-2">
        <div class="w-full bg-red-300/20 rounded-md border-1 border-red-500 text-red-500 font-semibold break-words px-2 {error == "" ? "hidden" : ""}">
            {error}
        </div>
        {@render children()}
        <div class="w-full flex justify-end mt-4">
            <Button label="Submit" onClick={() => {isCreate ? onSubmitCreate() : onSubmitEdit()}}/>
        </div>
    </div>
    
</Dialog>