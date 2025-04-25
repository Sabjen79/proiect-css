<script lang="ts">
    import { goto } from "$app/navigation";
    import { page } from '$app/state';  

    let {
        url,
        name
    }: {
        url: string,
        name: string
    } = $props();

    let hover = $state(false);
    let pressed = $state(false);
    let active = $state(false);

    $effect(() => {
        active = (page.url.pathname.split('/')[1] === url);
    });
</script>

<button
    class={`
        w-fit h-full px-3 py-4 pt-5 
        flex items-center duration-200
        hover:cursor-pointer select-none
        text-xl font-bold
        ${active || hover ? "border-b-3" : ""}
        ${active ? "border-amber-500 text-amber-500" 
        : pressed ? "border-amber-600 text-amber-600"
        : hover ? "border-amber-700 text-amber-700" : ""}
        
    `}
    onmouseenter={() => { hover = true; }}
    onmouseleave={() => { hover = false; pressed = false;}}
    onmousedown={() => { pressed = true; }}
    onmouseup={() => { pressed = false; }}
    onclick={() => { goto("/" + url); }}
>
    {#if url.startsWith('http')}
        <a href={url} target="_blank" rel="noopener noreferrer">{name}</a>
    {:else}
        {name}
    {/if}
</button>