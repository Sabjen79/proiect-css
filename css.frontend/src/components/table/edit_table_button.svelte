<script lang="ts">
    let {
        icon,
        color,
        hover = $bindable()
    } : {
        icon: string,
        color: string,
        hover: boolean
    } = $props();
</script>

<button 
    class={`
        font-icons p-1 text-xl
        rounded-full duration-200
        hover:cursor-pointer
    `}
    style={`
        background-color: color-mix(in lab, ${color} 50%, white);
        color: color-mix(in lab, ${color} ${hover ? "100%" : "50%"}, black);
    `}
    onmouseenter={() => { hover = true  }}
    onmouseleave={() => { hover = false }}
>
    {icon}
</button>