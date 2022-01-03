package ve.com.teeac.mynotes.feature_note.data.repository

import kotlinx.coroutines.flow.flow
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

    private val notesToInsert = mutableListOf<Note>()

    init{
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
        notesToInsert.shuffle()
    }

    private val flowList = flow {
            emit(notesToInsert.toList())
    }

    override fun getNotes()= flowList

    override suspend fun getNoteById(id: Int): Note? {
        return notesToInsert.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        notesToInsert.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notesToInsert.remove(note)

    }
}