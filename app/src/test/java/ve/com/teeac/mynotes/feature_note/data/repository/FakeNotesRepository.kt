package ve.com.teeac.mynotes.feature_note.data.repository

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository

class FakeNotesRepository: NoteRepository {


    var listNotes = mutableListOf(
        Note(
            title = "test2",
            id= 1,
            content = "Test2",
            timestamp = System.currentTimeMillis(),
            color = 3
        ),
        Note(
            title = "test1",
            id= 2,
            content = "Test1",
            timestamp = System.currentTimeMillis(),
            color = 2
        ),
        Note(
            title = "test3",
            id= 3,
            content = "Test2",
            timestamp = System.currentTimeMillis(),
            color = 1
        ),
    )

    private val flowList = flow {
            emit(listNotes.toList())
    }

    override fun getNotes()= flowList

    override suspend fun getNoteById(id: Int): Note? {
        return listNotes.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        listNotes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        listNotes.remove(note)

    }
}