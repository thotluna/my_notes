package ve.com.teeac.mynotes.feature_note.domain.use_cases

import com.google.common.truth.Truth.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mynotes.feature_note.domain.model.InvalidNoteException
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository

class AddNoteTest{

    @MockK
    private lateinit var repository: NoteRepository
    private lateinit var addNote: AddNote
    private val note = Note(
        title = "New Note",
        content = "New Not Content",
    )

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        addNote = AddNote(repository)
    }

    @Test
    fun `Save Note correctly`()= runBlocking {

        val noteSlot = slot<Note>()

        coEvery {
            repository.insertNote(capture(noteSlot))
        } coAnswers {
            assertThat(noteSlot.captured).isEqualTo(note)
        }

        addNote(note)

        coVerify(exactly = 1){repository.insertNote(note)}
        confirmVerified(repository)
    }

    @Test(expected = InvalidNoteException::class)
    fun `Intent save new note without title, exception`()= runBlocking {
        val newNote = note.copy(title = "")

        addNote(newNote)

        coVerify(exactly = 0){repository.insertNote(newNote)}
        confirmVerified(repository)

    }

    @Test(expected = InvalidNoteException::class)
    fun `Intent save new note without content, exception`()= runBlocking {
        val newNote = note.copy(content = "")

        addNote(newNote)

        coVerify(exactly = 0){repository.insertNote(newNote)}
        confirmVerified(repository)

    }
}