<script lang="ts">
    import Button from "../components/button.svelte";
    import Dialog from "../components/dialog.svelte";
    import Input from "../components/input.svelte";
    import Select from "../components/select.svelte";

    let a = $state("");
    let b = $state("2");
    let dialog: Dialog;
</script>

<Dialog bind:this={dialog} title="Hello Dialog!">
    <div class="w-70 wrap-anywhere">
        AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    </div>
</Dialog>

<div class={`
    h-full w-full
    flex flex-col justify-center items-center
    bg-blue-100
`}>
    <p class={`
        absolute overflow-hidden inset-0
        text-4xl text-center select-none
        opacity-20
    `}>
        {#each { length: 1000 } as _}
            🚧
        {/each}
    </p>

    <p class={`font-black text-7xl z-10 text-black stroke-1 text-shadow-lg/20`}>
        ȘANTIER ÎN LUCRU...
    </p>

    <p class={`
        font-black text-xl z-10 text-black
    `}>
        {Math.ceil((new Date(2025, 4, 7).getTime() - Date.now()) / (1000 * 60 * 60 * 24))} Days Left...
    </p>

    <div class={`
        mt-2 bg-blue-50 px-4 py-3
        flex flex-col z-10 rounded-md
        shadow-md/30 gap-2
    `}>
        <Button 
            label="Open Dialog..."
            onClick={() => {
                dialog.open();
            }}
        />
        <Input bind:value={a}/>
        <Select bind:value={b} options={[
            { label: "Option 1", value: "1" },
            { label: "Option 2", value: "2" },
            { label: "Option 3", value: "3" }
        ]}/>
    </div>
</div>