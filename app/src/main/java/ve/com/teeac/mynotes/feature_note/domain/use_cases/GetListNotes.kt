package ve.com.teeac.mynotes.feature_note.domain.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType

class GetListNotes(
    private val repository: NoteRepository
) {

    operator fun invoke(
        notesOrder: NotesOrder = NotesOrder.Title(OrderType.Ascending)
    ): Flow<List<Note>>{

        return repository.getNotes().map { notes ->
            when(notesOrder.orderType){
                is OrderType.Ascending -> {
                    when(notesOrder){
                        is NotesOrder.Title ->{ notes.sortedBy{ it.title.lowercase()}}
                        is NotesOrder.Date ->{notes.sortedBy{ it.timestamp}}
                        is NotesOrder.Color ->{notes.sortedBy{ it.color}}
                    }
                }
                is OrderType.Descending -> {
                    when(notesOrder) {
                        is NotesOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NotesOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NotesOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }

    }
}