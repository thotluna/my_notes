package ve.com.teeac.mynotes.feature_note.domain.use_cases

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import ve.com.teeac.mynotes.feature_note.data.repository.FakeNotesRepository
import ve.com.teeac.mynotes.feature_note.domain.model.InvalidNoteException
import ve.com.teeac.mynotes.feature_note.domain.model.Note

class AddNoteTest{

    private val repository = FakeNotesRepository()
    private val addNote = AddNote(repository)
    private val getListNotes = GetListNotes(repository)

    @Test
    fun addNewNote()= runBlocking {
        val list = getListNotes().first()
        val newNote = Note(
            title = "New Note",
            content = "New Not Content",
        )

        addNote(note = newNote)

        val newList = getListNotes().first()

        assertEquals(list.size + 1, newList.size)
    }

    @Test(expected = InvalidNoteException::class)
    fun addNewNoteWithoutTitle()= runBlocking {
        val newNote = Note(
            content = "New Not Content",
        )

        addNote(note = newNote)

    }

    @Test(expected = InvalidNoteException::class)
    fun addNewNoteWithoutContent()= runBlocking {
        val newNote = Note(
            title = "New Not Content"
        )

        addNote(note = newNote)

    }
}