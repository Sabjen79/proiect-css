<script lang="ts">
    import FormDialog from "../../../components/form_dialog.svelte";
    import Input from "../../../components/input.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import type { EditTableData } from "../../../components/table/edit_table_data";
    import { Discipline, disciplineStore } from "../../../stores/disciplines";

    const columns: EditTableData[] = [
        { name: "ID", flex: 1 },
        { name: "Name", flex: 1 },
        { name: "Description", flex: 3 },
    ];

    let editDialog: FormDialog;

    let formDiscipline = $state<Discipline>(new Discipline("", "", ""));
</script>

<FormDialog
    bind:this={editDialog}
    name={"Discipline"}
    onSubmitCreate={async () => {
        editDialog.setError("ERROR: NU MERGE SA IMI BAG CIOACA IN EA CA E ORA 2:41 SI NU M-AM CULCAT");
    }}
    onSubmitEdit={async () => {}}
>
    <Input bind:value={formDiscipline.name} />
    <Input bind:value={formDiscipline.description} />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            {columns}
            items={$disciplineStore}
            onEdit={(item) => {
                Object.assign(formDiscipline, item);

                editDialog.openEdit();
            }}
            onDelete={(item) => {
                Object.assign(formDiscipline, new Discipline("", "", ""));

                editDialog.openCreate();
            }}
        />
    </div>
</div>