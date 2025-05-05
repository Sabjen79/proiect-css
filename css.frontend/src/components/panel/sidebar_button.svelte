<script lang="ts">
    import { goto } from "$app/navigation";
    import { page } from "$app/state";
    import { onMount } from "svelte";

    let {
        icon,
        name,
        color,
        url
    } : {
        icon: string,
        name: string,
        color: string,
        url: string
    } = $props();

    let hover = $state(false);
    let active = $state(false);

    $effect(() => {
        active = (page.url.pathname == url)
    });
</script>

<button
    class={`
        flex justify-start items-center
        w-full h-fit min-h-10 select-none duration-150
        hover:cursor-pointer
    `}
    style="color: color-mix(in lab, {color} {active ? "100%" :hover ? "80%" : "0"}, black)"
    onmouseenter={() => { hover = true  }}
    onmouseleave={() => { hover = false }}
    onclick={() => {goto(url)}}
>
    <div class={`flex items-center`}>
        <p class={`font-icons text-2xl ![font-variation-settings:"FILL"_1,"wght"_500,"GRAD"_0,"opsz"_24]`}>
            {icon}
        </p>
    </div>
    <p class={`ml-2 font-bold text-lg truncate`}>
        {name}
    </p>
</button>