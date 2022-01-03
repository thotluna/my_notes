package ve.com.teeac.mynotes.feature_note.presentation.notes

import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder

sealed class NotesEvent {
    object ToggleOrderSection: NotesEvent()
    data class Order(val notesOrder: NotesOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
}