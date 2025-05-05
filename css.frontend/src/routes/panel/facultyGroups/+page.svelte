<script lang="ts">
    import { onMount } from "svelte";
    import FormDialog from "../../../components/form_dialog.svelte";
    import Input from "../../../components/input.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import { Degree } from "../../../stores/enums";
    import Select from "../../../components/select.svelte";
    import { createFacultyGroup, deleteFacultyGroup, refreshFacultyGroups, facultyGroupStore, updateFacultyGroups, type FacultyGroup } from "../../../stores/facultyGroups";
    import { getSemiYear, getSemiYearsAsOptions, refreshSemiYears } from "../../../stores/semiYears";

    let editDialog: FormDialog;

    let formFacultyGroup: FacultyGroup = $state({
        id: "",
        name: "",
        semiYearId: ""
    })
</script>

<FormDialog
    bind:this={editDialog}
    name={"Faculty Group"}
    onSubmitCreate={async () => {
        createFacultyGroup(formFacultyGroup).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
    onSubmitEdit={async () => {
        updateFacultyGroups(formFacultyGroup).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
>
    <Input label="Name" bind:value={formFacultyGroup.name} />
    <Select 
        bind:value={formFacultyGroup.semiYearId} 
        label="Semi-Year"
        options={getSemiYearsAsOptions()}
    />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            items={$facultyGroupStore}
            header={[
                { name: "Name", flex: 1 },
                { name: "Semi-Year", flex: 4 }
            ]}
            columns={(item) => {
                let sy = getSemiYear(item.semiYearId);
                return [item.name, `${sy.name} - ${Degree[sy.degree]} Year ${sy.year}`]
            }}
            onCreate={() => {
                Object.assign(formFacultyGroup, {
                    id: "",
                    name: "",
                    title: ""
                });

                editDialog.openCreate();
            }}
            onEdit={(item) => {
                Object.assign(formFacultyGroup, item);

                editDialog.openEdit();
            }}
            onDelete={(item) => {
                deleteFacultyGroup(item.id).catch((err) => {
                    editDialog.openDelete(err.toString());
                })
            }}
        />
    </div>
</div>