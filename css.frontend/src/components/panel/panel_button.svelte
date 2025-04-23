<script lang="ts">
    import { goto } from "$app/navigation";
    import { page } from "$app/state";

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
</script>

<button
    class={`
        flex flex-col justify-end items-center
        w-60 h-50 m-4
        rounded-2xl shadow-md select-none duration-150
        hover:-translate-y-1 hover:cursor-pointer
    `}
    style="background-color: color-mix(in lab, {color} 20%, transparent); border: 2px solid {color}; color: {color}"
    onclick={() => {goto(page.url.pathname + url)}}
>
    <div class={`flex items-center`}>
        <p class={`font-icons text-8xl ![font-variation-settings:"FILL"_1,"wght"_500,"GRAD"_0,"opsz"_24]`}>
            {icon}
        </p>
    </div>
    <div class={`my-5 flex items-center`}>
        <p class={`font-bold text-xl`}>
            {name}
        </p>
    </div>
</button>