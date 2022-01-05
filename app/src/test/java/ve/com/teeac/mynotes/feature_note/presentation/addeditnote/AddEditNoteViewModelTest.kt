package ve.com.teeac.mynotes.feature_note.presentation.addeditnote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mynotes.feature_note.domain.model.InvalidNoteException
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.use_cases.*
import ve.com.teeac.mynotes.feature_note.utils.MainCoroutineRule
import ve.com.teeac.mynotes.feature_note.utils.StateFocusFake

@ExperimentalCoroutinesApi
class AddEditNoteViewModelTest{

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var getListNotes: GetListNotes
    @MockK
    private lateinit var getNote: GetNote
    @MockK
    private lateinit var addNote: AddNote
    @MockK
    private lateinit var deleteNotes: DeleteNote

    private lateinit var useCases: NotesUseCases

    private lateinit var viewModel: AddEditNoteViewModel

    private var note: Note? = null

    @Before
    fun setup() {
        MockKAnnotations.init(
            this,
            relaxUnitFun = true
        )
        useCases = NotesUseCases(
            getListNotes = getListNotes,
            getNote = getNote,
            addNote = addNote,
            deleteNotes = deleteNotes
        )
    }

    @Test
    fun `starting viewModel without note`() = runBlocking {

        loadViewModel(null)

        val expectedState = NoteTextFieldState()

        assertThat(viewModel.stateTitle.value)
            .isEqualTo(expectedState.copy(hint = "Please, write your title"))

        assertThat(viewModel.stateContent.value)
            .isEqualTo(expectedState.copy(hint = "Please, write your Content"))

        assertThat(viewModel.stateColor.value).isNotNull()

        coVerify(exactly = 0){useCases.getNote(any())}
        confirmVerified(getNote)

    }

    @Test
    fun `starting viewModel with note`() = runBlocking {

        val noteId = 1

        loadViewModel(noteId)

        val expectedState = NoteTextFieldState()

        assertThat(viewModel.stateTitle.value)
            .isEqualTo(
                expectedState.copy(
                    text = note!!.title,
                    hint = "Please, write your title"
                ))

        assertThat(viewModel.stateContent.value)
            .isEqualTo(
                expectedState.copy(
                    text = note!!.content,
                    hint = "Please, write your Content"
                ))

        assertThat(viewModel.stateColor.value)
            .isEqualTo(note!!.color)

        coVerify(exactly = 1){useCases.getNote(noteId)}
        confirmVerified(getNote)

    }

    @Test
    fun `starting viewModel with node id failed`() = runBlocking {

        val noteId = -1

        loadViewModel(noteId)

        coVerify(exactly = 0){useCases.getNote(any())}
        confirmVerified(getNote)
    }

    @Test
    fun `write the title, change state title text`() = runBlocking {
        val title = "My Code is clean"
        val expectedState = NoteTextFieldState().copy(
            text = title,
            hint = "Please, write your title",
        )

        loadViewModel(null)
        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(value = title))

        assertThat(viewModel.stateTitle.value).isEqualTo(expectedState)
    }

    @Test
    fun `rewrite the title on blank, don't change state title text`() = runBlocking {

        loadViewModel(1)

        assertThat(viewModel.stateTitle.value.text).isEqualTo(note!!.title)

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(value = ""))

        assertThat(viewModel.stateTitle.value.text).isEqualTo(note!!.title)

    }

    @Test
    fun `write the content, change state title text`() = runBlocking {
        val content = "This is a content the your code clean"
        val expectedState = NoteTextFieldState().copy(
            text = content,
            hint = "Please, write your Content"
        )

        loadViewModel(null)

        viewModel.onEvent(AddEditNoteEvent.EnteredContent(value = content))

        assertThat(viewModel.stateContent.value).isEqualTo(expectedState)
    }

    @Test
    fun `rewrite the content on blank, don't change state title text`() = runBlocking {

        loadViewModel(1)

        assertThat(viewModel.stateContent.value.text).isEqualTo(note!!.content)

        viewModel.onEvent(AddEditNoteEvent.EnteredContent(value = ""))

        assertThat(viewModel.stateContent.value.text).isEqualTo(note!!.content)
    }

    @Test
    fun `inFocus and outFocus Title blank, change state title isHintVisible`() = runBlocking {

        loadViewModel(null)

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.InFocus))

        assertThat(viewModel.stateTitle.value.isHintVisible).isFalse()

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.outFocus))

        assertThat(viewModel.stateTitle.value.isHintVisible).isTrue()

    }

    @Test
    fun `inFocus and outFocus title, don't change state title isHintVisible`() = runBlocking {

        loadViewModel(1)

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.InFocus))

        assertThat(viewModel.stateTitle.value.isHintVisible).isTrue()

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.outFocus))

        assertThat(viewModel.stateTitle.value.isHintVisible).isTrue()

    }

    @Test
    fun `inFocus and outFocus content blank, change state title isHintVisible`() = runBlocking {

        loadViewModel(null)

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.InFocus))

        assertThat(viewModel.stateContent.value.isHintVisible).isFalse()

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.outFocus))

        assertThat(viewModel.stateContent.value.isHintVisible).isTrue()

    }

    @Test
    fun `inFocus and outFocus content, change state title isHintVisible`() = runBlocking {

        loadViewModel(1)

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.InFocus))

        assertThat(viewModel.stateContent.value.isHintVisible).isTrue()

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.outFocus))

        assertThat(viewModel.stateContent.value.isHintVisible).isTrue()

    }

    @Test
    fun `Send new color, change state color`() = runBlocking {
        loadViewModel(1)

        viewModel.onEvent(AddEditNoteEvent.ChangeColor(Note.noteColor.random().toArgb()))

        assertThat(viewModel.stateColor.value).isNotEqualTo(note!!.color)
    }

    @Test
    fun `save new note, completed`() = runBlocking {

        val newNote = getNote()

        coEvery {
            useCases.addNote(any())
        }returns Unit

        loadViewModel()

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(newNote.title))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent(newNote.content))

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        assertThat(viewModel.eventFlow.first()).isEqualTo(UiEvent.SaveNote)

        coVerify(exactly = 1) { useCases.addNote(any()) }

        confirmVerified(addNote)

    }

    @Test
    fun `update note, completed`() = runBlocking {

        coEvery {
            useCases.addNote(any())
        }returns Unit

        loadViewModel(1)

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        assertThat(viewModel.eventFlow.first()).isEqualTo(UiEvent.SaveNote)

        coVerify(exactly = 1) { useCases.addNote(any()) }

        confirmVerified(addNote)
    }

    @Test
    fun `save new note title in blank, not completed`() = runBlocking {

        val newNote = getNote().copy(title = "")

        coEvery {
            useCases.addNote(any())
        }answers{
            throw InvalidNoteException(InvalidNoteException.EMPTY_TITLE)
        }

        loadViewModel()

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(newNote.title))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent(newNote.content))

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        assertThat(viewModel.eventFlow.first())
            .isEqualTo(UiEvent.ShowSnackBar(
                message = InvalidNoteException.EMPTY_TITLE))

        coVerify(exactly = 1) { useCases.addNote(any()) }

        confirmVerified(addNote)

    }

    @Test
    fun `save new note content in blank, not completed`() = runBlocking {

        val newNote = getNote().copy(content = "")

        coEvery {
            useCases.addNote(any())
        }answers{
            throw InvalidNoteException(InvalidNoteException.EMPTY_CONTENT)
        }

        loadViewModel()

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(newNote.title))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent(newNote.content))


        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        assertThat(viewModel.eventFlow.first())
            .isEqualTo(UiEvent.ShowSnackBar(
                message = InvalidNoteException.EMPTY_CONTENT))

        coVerify(exactly = 1) { useCases.addNote(any()) }

        confirmVerified(addNote)
    }

    private fun getNote(noteId: Int? = null) =  Note(
        id = noteId,
        title = "Saving test 1",
        content = "intent save first test",
        color = noteId?.let { Note.noteColor.random().toArgb() }
    )

    private fun loadViewModel(noteId: Int? = null){

        coEvery {
            savedStateHandle.get<Int>("noteId")
        }coAnswers {
            noteId
        }

        noteId?.let {

            note = getNote(noteId)

            coEvery {
                useCases.getNote(noteId)
            }coAnswers {
                note!!
            }
        }

        viewModel = AddEditNoteViewModel(useCases, savedStateHandle)
    }
}
