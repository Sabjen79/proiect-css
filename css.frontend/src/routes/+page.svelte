<script lang="ts">
    import Button from "../components/button.svelte";
    import Input from "../components/input.svelte";

    let a = $state("");
</script>

<div class={`
    h-[100vh] w-[100vw]
    flex flex-col justify-center items-center
    bg-amber-100
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

    <p class={`
        font-black text-7xl z-10 text-black stroke-1 text-shadow-lg/20
    `}>
        ȘANTIER ÎN LUCRU...
    </p>

    <div class={`
        mt-2 bg-amber-50 px-4 py-3
        flex flex-col z-10 rounded-md
        shadow-md/30 gap-2
    `}>
        <Button />
        <Input bind:value={a}/>
    </div>
</div>