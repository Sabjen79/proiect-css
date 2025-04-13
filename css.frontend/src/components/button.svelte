<script lang="ts">
    let {
        label = "Button Test"
    }: {
        label?: String
    } = $props();

    let hover = $state(false);
    let pressed = $state(false);
</script>

<button
    class={`
        w-fit h-6 px-3 py-4 flex items-center
        rounded-md border-1 duration-200
        hover:cursor-pointer select-none
        ${pressed ? "bg-amber-600 border-amber-500" 
        : hover ? "bg-amber-500 border-amber-400" 
        : "bg-amber-400 border-amber-300"}
    `}
    onmouseenter={() => { hover = true; }}
    onmouseleave={() => { hover = false; pressed = false;}}
    onmousedown={() => { pressed = true; }}
    onmouseup={() => { pressed = false; }}
>
    {label}
</button>
