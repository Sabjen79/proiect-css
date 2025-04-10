<script lang="ts">
    let a = $state("");
</script>

<div class={`
    h-[100vh] w-[100vw]
    flex justify-center items-center
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
</div>