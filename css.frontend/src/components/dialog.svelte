<script lang="ts">
    let { children, title = "" } = $props();

    export function open() {
        opened = true;

        new Promise((resolve) => {
            setTimeout(resolve, 1);
        }).then(() => {
            visible = true;
        });
    }

    export function close() {
        if (blocked) return;
        visible = false;

        new Promise((resolve) => {
            setTimeout(resolve, 200);
        }).then(() => {
            opened = false;
        });
    }

    export function block() {
        blocked = true;
    }

    export function unblock() {
        blocked = false;
    }

    let opened = $state(false);
    let visible = $state(false);
    let blocked = $state(false);
</script>

<!-- svelte-ignore a11y_click_events_have_key_events -->
<!-- svelte-ignore a11y_no_static_element_interactions -->
{#if opened}
    <div
        id="container"
        class={`
            fixed inset-0 w-[100vw] h-[100vh]
            flex justify-center items-center z-100
        `}
    >
        <div 
            id="background"
            class={`
                absolute w-full h-full bg-[#0000008d]
                duration-250 ease-out 
                ${visible ? "opacity-100" : "opacity-0"}
            `}
            onclick={close}
        ></div>

        <div 
            id="dialog" 
            class={`
                absolute flex flex-col justify-center
                m-0 p-0 bg-amber-50 shadow-container
                border-1 border-white rounded-sm
                duration-250 ease-out w-auto h-auto 
                ${visible ? "opacity-100 translate-y-0" : "opacity-0 -translate-y-3"}
            `}
        >
            <button 
                class={`
                    absolute top-3 right-3
                    bg-transparent border-0
                    duration-200 ease-out
                    text-gray-700 hover:text-amber-500
                    hover:cursor-pointer
                `}
                onclick={close}
            >
                <div class="font-icons text-2xl flex justify-center items-center">
                    close
                </div>
            </button>

            <div 
                class={`
                    flex items-center w-full h-6
                    mx-4 my-3 text-xl font-semibold
                    text-gray-800
                `}
            >
                <p>{title}</p>
            </div>

            <div class="px-3 py-3 -mt-3 overflow-visible">
                {@render children()}
            </div>
        </div>
    </div>
{/if}
