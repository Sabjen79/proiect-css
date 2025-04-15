<script lang="ts">
	import '../app.css';
    import Navbar from '../components/navbar.svelte';
    import NavbarButton from '../components/navbar_button.svelte';
	
	let { children } = $props();
</script>

<Navbar>
	<NavbarButton name="Main Page" url="/"/>
    <NavbarButton name="Empty Page" url="/page1"/>
    <NavbarButton name="Secret" url="https://tinyurl.com/2ackkd6t"/>
</Navbar>

<div class="w-[100vw] h-[100vh] flex">
	<div class="pt-12 grow">
		{@render children()}
	</div>
</div>


