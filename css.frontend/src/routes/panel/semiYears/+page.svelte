<script lang="ts">
    import FormDialog from "../../../components/form_dialog.svelte";
    import Input from "../../../components/input.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import { createSemiYear, deleteSemiYear, semiYearStore, updateSemiYears, type SemiYear } from "../../../stores/semiYears";
    import { Degree, getEnumAsOptions } from "../../../stores/enums";
    import Select from "../../../components/select.svelte";
    
    let editDialog: FormDialog;

    let formSemiYear: SemiYear = $state({
        id: "",
        name: "",
        degree: Degree.Bachelors,
        year: 0
    })
</script>

<FormDialog
    bind:this={editDialog}
    name={"Semi-Year"}
    onSubmitCreate={async () => {
        createSemiYear(formSemiYear).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
    onSubmitEdit={async () => {
        updateSemiYears(formSemiYear).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
>
    <Input label="Name" bind:value={formSemiYear.name} />
    <Select 
        bind:value={formSemiYear.degree} 
        options={getEnumAsOptions(Degree)}
    />
    <Input label="Study Year" bind:value={formSemiYear.year} />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            items={$semiYearStore}
            header={[
                { name: "Name", flex: 1 },
                { name: "Degree", flex: 1 },
                { name: "Year", flex: 4 }
            ]}
            columns={(item) => {
                return [item.name, Degree[item.degree], item.year.toString()]
            }}
            onCreate={() => {
                Object.assign(formSemiYear, {
                    id: "",
                    name: "",
                    degree: Degree.Bachelors,
                    year: 0
                });

                editDialog.openCreate();
            }}
            onEdit={(item) => {
                Object.assign(formSemiYear, item);

                editDialog.openEdit();
            }}
            onDelete={(item) => {
                deleteSemiYear(item.id).catch((err) => {
                    editDialog.openDelete(err.toString());
                })
            }}
        />
    </div>
</div>