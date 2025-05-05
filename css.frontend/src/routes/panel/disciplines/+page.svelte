<script lang="ts">
    import { onMount } from "svelte";
    import FormDialog from "../../../components/form_dialog.svelte";
    import Input from "../../../components/input.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import { createDiscipline, deleteDiscipline, disciplineStore, refreshDisciplines, updateDisciplines, type Discipline } from "../../../stores/disciplines";
    import { Degree, getEnumAsOptions } from "../../../stores/enums";
    import Select from "../../../components/select.svelte";
    import Dialog from "../../../components/dialog.svelte";

    let editDialog: FormDialog;

    let formDiscipline: Discipline = $state({
        id: "",
        name: "",
        degree: Degree.Bachelors,
        year: 0

    })
</script>

<FormDialog
    bind:this={editDialog}
    name={"Discipline"}
    onSubmitCreate={async () => {
        createDiscipline(formDiscipline).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
    onSubmitEdit={async () => {
        updateDisciplines(formDiscipline).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
>
    <Input label="Name" bind:value={formDiscipline.name} />
    <Select 
        bind:value={formDiscipline.degree} 
        options={getEnumAsOptions(Degree)}
    />
    <Input label="Study Year" bind:value={formDiscipline.year} />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            items={$disciplineStore}
            header={[
                { name: "Name", flex: 2 },
                { name: "Degree", flex: 1 },
                { name: "Year", flex: 1 }
            ]}
            columns={(item) => {
                return [item.name, Degree[item.degree], item.year.toString()]
            }}
            onCreate={() => {
                Object.assign(formDiscipline, {
                    id: "",
                    name: "",
                    degree: Degree.Bachelors,
                    year: 0
                });

                editDialog.openCreate();
            }}
            onEdit={(item) => {
                Object.assign(formDiscipline, item);

                editDialog.openEdit();
            }}
            onDelete={(item) => {
                deleteDiscipline(item.id).catch((err) => {
                    editDialog.openDelete(err.toString());
                })
            }}
        />
    </div>
</div>