<script lang="ts">
    import { onMount } from "svelte";
    import FormDialog from "../../../components/form_dialog.svelte";
    import Input from "../../../components/input.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import { createTeacher, deleteTeacher, refreshTeachers, teacherStore, updateTeacher, type Teacher } from "../../../stores/teacher";

    let editDialog: FormDialog;

    let formTeacher: Teacher = $state({
        id: "",
        name: "",
        title: ""
    })
</script>

<FormDialog
    bind:this={editDialog}
    name={"Discipline"}
    onSubmitCreate={async () => {
        createTeacher(formTeacher).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
    onSubmitEdit={async () => {
        updateTeacher(formTeacher).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
>
    <Input label="Name" bind:value={formTeacher.name} />
    <Input label="Title" bind:value={formTeacher.title} />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            items={$teacherStore}
            header={[
                { name: "Title", flex: 1 },
                { name: "Name", flex: 10 }
            ]}
            columns={(item) => {
                return [item.title, item.name]
            }}
            onCreate={() => {
                Object.assign(formTeacher, {
                    id: "",
                    name: "",
                    title: ""
                });

                editDialog.openCreate();
            }}
            onEdit={(item) => {
                Object.assign(formTeacher, item);

                editDialog.openEdit();
            }}
            onDelete={(item) => {
                deleteTeacher(item.id).catch((err) => {
                    editDialog.openDelete(err.toString());
                })
            }}
        />
    </div>
</div>