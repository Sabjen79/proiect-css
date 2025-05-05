<script lang="ts">
    interface Options {
        label: string
        value: string | number
    }

    let {
        value = $bindable(""),
        label = "Label",
        options = []
    }: {
        value: string | number,
        label?: string,
        options: Options[]
    } = $props();

    let focused = $state(false);
</script>

<div>
    <p class="select-none ml-2 text-sm duration-200 {focused ? "text-blue-500 font-semibold" : "text-gray-950"}">
        {label}
    </p>
    <select 
            class={`
                w-full h-8 px-2
                border-[1.5px] rounded-md
                outline-0 duration-200 bg-gray-50
                ${focused ? "border-blue-500" : "border-gray-800"}
            `}
            bind:value
            onfocusin={() => { focused = true }}
            onfocusout={() => { focused = false }}
        >
        {#each options as option}
            <option value={option.value}>{option.label}</option>
        {/each}
    </select>
</div>