<script lang="ts">
	import '../app.css';
    import Navbar from '../components/navbar.svelte';
    import NavbarButton from '../components/navbar_button.svelte';
	
	let { children } = $props();
</script>

<Navbar>
	<NavbarButton name="Main Page" url="/"/>
    <NavbarButton name="Timetable" url="/schedule"/>
	<NavbarButton name="Admin Panel" url="/panel"/>
</Navbar>

<div class="w-[100vw] h-[100vh] flex">
	<div class="pt-12 grow">
		{@render children()}
	</div>
</div>


