<script lang="ts">
    let {
        value = $bindable(),
        placeholder = "Value",
        label = "Label"
    }: {
        value: string | number,
        placeholder?: string,
        label?: string
    } = $props();

    let focused = $state(false);
</script>

<div>
    <p class="select-none ml-2 text-sm duration-200 {focused ? "text-blue-500 font-semibold" : "text-gray-950"}">
        {label}
    </p>
    <input 
        class={`
            w-full h-8 px-2
            border-[1.5px] rounded-md
            outline-0 duration-200 bg-gray-50
            ${focused ? "border-blue-500" : "border-gray-800"}
        `}
        bind:value
        {placeholder}
        onfocusin={() => { focused = true }}
        onfocusout={() => { focused = false }}
    >
</div>
