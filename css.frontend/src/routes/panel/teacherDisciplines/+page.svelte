<script lang="ts">
    import { onMount } from "svelte";
    import FormDialog from "../../../components/form_dialog.svelte";
    import EditTable from "../../../components/table/edit_table.svelte";
    import { getDiscipline, getDisciplinesAsOptions, refreshDisciplines } from "../../../stores/disciplines";
    import Select from "../../../components/select.svelte";
    import { createTeacherDiscipline, deleteTeacherDiscipline, refreshTeacherDisciplines, teacherDisciplineStore, type TeacherDiscipline } from "../../../stores/teacherDisciplines";
    import { getTeacher, getTeachersAsOptions, refreshTeachers } from "../../../stores/teacher";

    let editDialog: FormDialog;

    let formTeacherDiscipline: TeacherDiscipline = $state({
        id: "",
        teacherId: "",
        disciplineId: ""
    })
</script>

<FormDialog
    bind:this={editDialog}
    name={"Discipline"}
    onSubmitCreate={async () => {
        createTeacherDiscipline(formTeacherDiscipline).then(editDialog.close).catch((err) => {
            editDialog.setError(err);
        })
    }}
    onSubmitEdit={async () => {}}
>
    <Select 
        bind:value={formTeacherDiscipline.teacherId} 
        label="Teacher"
        options={getTeachersAsOptions()}
    />
    <Select 
        bind:value={formTeacherDiscipline.disciplineId} 
        label="Discipline"
        options={getDisciplinesAsOptions()}
    />
</FormDialog>

<div class="w-full h-full flex justify-center items-center">
    <div class="max-w-300 w-[90%] h-full py-10">
        <EditTable
            items={$teacherDisciplineStore}
            header={[
                { name: "Teacher", flex: 1 },
                { name: "Discipline", flex: 3 }
            ]}
            columns={(item) => {
                let t = getTeacher(item.teacherId)
                let d = getDiscipline(item.disciplineId)
                return [`${t.title} ${t.name}`, d.name]
            }}
            onCreate={() => {
                Object.assign(formTeacherDiscipline, {
                    id: "",
                    name: "",
                    title: ""
                });

                editDialog.openCreate();
            }}
            onEdit={(item) => {}}
            onDelete={(item) => {
                deleteTeacherDiscipline(item.id).catch((err) => {
                    editDialog.openDelete(err.toString());
                })
            }}
        />
    </div>
</div>