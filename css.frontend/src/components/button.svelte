<script lang="ts">
    let {
        label = "Button Test",
        onClick = () => {}
    }: {
        label?: String,
        onClick?: Function
    } = $props();

    let hover = $state(false);
    let pressed = $state(false);
</script>

<button
    class={`
        w-fit h-6 px-3 py-4 flex items-center
        rounded-md border-1 duration-200
        hover:cursor-pointer select-none
        ${pressed ? "bg-blue-600 border-blue-500" 
        : hover ? "bg-blue-500 border-blue-400" 
        : "bg-blue-400 border-blue-300"}
    `}
    onmouseenter={() => { hover = true; }}
    onmouseleave={() => { hover = false; pressed = false;}}
    onmousedown={() => { pressed = true; }}
    onmouseup={() => { pressed = false; }}
    onclick={() => onClick()}
>
    {label}
</button>
