<script lang="ts">
    let {children} = $props();
</script>

<div class={`
    fixed top-0 h-12 w-full
    z-50 bg-amber-100 shadow-md
    flex gap-2 justify-center items-center
`}>
    {@render children()}
</div>
