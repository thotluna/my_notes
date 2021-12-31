package ve.com.teeac.mynotes.feature_note.domain.use_cases

data class NotesUseCases(
    val getListNotes: GetListNotes,
    val getNote: GetNote,
    val addNote: AddNote,
    val deleteNotes: DeleteNote
)
