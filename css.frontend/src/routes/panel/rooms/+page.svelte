<script lang="ts">
    import { onMount } from "svelte";
    import FormDialog from "../../../components/form_dialog.svelte";
    import Input from "../../../components/input.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import { createRoom, deleteRoom, roomStore, refreshRooms, updateRooms, type Room } from "../../../stores/rooms";
    import { getEnumAsOptions, RoomType } from "../../../stores/enums";
    import Select from "../../../components/select.svelte";
    
    let editDialog: FormDialog;

    let formRoom: Room = $state({
        id: "",
        name: "",
        capacity: 0,
        roomType: 0
    })
</script>

<FormDialog
    bind:this={editDialog}
    name={"Room"}
    onSubmitCreate={async () => {
        createRoom(formRoom).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
    onSubmitEdit={async () => {
        updateRooms(formRoom).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
>
    <Input label="Name" bind:value={formRoom.name} />
    <Input label="Capacity" bind:value={formRoom.capacity} />
    <Select 
        bind:value={formRoom.roomType} 
        options={getEnumAsOptions(RoomType)}
    />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            items={$roomStore}
            header={[
                { name: "Name", flex: 1 },
                { name: "Capacity", flex: 1 },
                { name: "Room Type", flex: 4 }
            ]}
            columns={(item) => {
                return [item.name, item.capacity.toString(), RoomType[item.roomType]]
            }}
            onCreate={() => {
                Object.assign(formRoom, {
                    id: "",
                    name: "",
                    capacity: 0,
                    roomType: 0
                });

                editDialog.openCreate();
            }}
            onEdit={(item) => {
                Object.assign(formRoom, item);

                editDialog.openEdit();
            }}
            onDelete={(item) => {
                deleteRoom(item.id).catch((err) => {
                    editDialog.openDelete(err.toString());
                })
            }}
        />
    </div>
</div>