package ve.com.teeac.mynotes.feature_note.presentation.addeditnote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mynotes.feature_note.data.repository.FakeNotesRepository
import ve.com.teeac.mynotes.feature_note.domain.model.InvalidNoteException
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.use_cases.AddNote
import ve.com.teeac.mynotes.feature_note.domain.use_cases.GetNote
import ve.com.teeac.mynotes.feature_note.utils.MainCoroutineRule
import ve.com.teeac.mynotes.feature_note.utils.StateFocusFake

@ExperimentalCoroutinesApi
class AddEditNoteViewModelTest{

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeNotesRepository
    @MockK
    private lateinit var addCases: AddNote
    private val noteSlot = slot<Note>()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var getNoteCase: GetNote
    private lateinit var viewModel: AddEditNoteViewModel

    @Before
    fun setup() {
        repository = FakeNotesRepository()
        MockKAnnotations.init(this, relaxUnitFun = true)
        getNoteCase = GetNote(repository)

        coEvery {
            savedStateHandle.get<Int>("noteId")
        }coAnswers {
            null
        }


        viewModel = AddEditNoteViewModel(addCases, getNoteCase, savedStateHandle)
    }

    @Test
    fun initialTest() = runBlocking {
        val expectedState = NoteTextFieldState()
        val stateTitle = viewModel.stateTitle.value
        val stateContent = viewModel.stateContent.value
        val stateColor = viewModel.stateColor.value

        assertEquals(
            expectedState.copy(hint = "Please, write your title"),
            stateTitle
        )

        assertEquals(
            expectedState.copy(hint = "Please, write your Content"),
            stateContent
        )

        assertNotNull(stateColor)
    }

    @Test
    fun enteredTitleTest() = runBlocking {
        val title = "My Code is clean"
        val expectedState = NoteTextFieldState().copy(
            text = title,
            hint = "Please, write your title"
        )

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(value = title))

        assertEquals(
            expectedState,
            viewModel.stateTitle.value
        )
    }

    @Test
    fun enteredTitleTest_OnBlank() = runBlocking {
        val title = "My Code is clean"
        val expectedState = NoteTextFieldState().copy(
            text = title,
            hint = "Please, write your title"
        )

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(value = title))

        assertEquals(
            expectedState,
            viewModel.stateTitle.value
        )

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(value = ""))

        assertEquals(
            expectedState,
            viewModel.stateTitle.value
        )

    }

    @Test
    fun enteredContentTest() = runBlocking {
        val content = "This is a content the your code clean"
        val expectedState = NoteTextFieldState().copy(
            text = content,
            hint = "Please, write your Content"
        )

        viewModel.onEvent(AddEditNoteEvent.EnteredContent(value = content))

        assertEquals(
            expectedState,
            viewModel.stateContent.value
        )
    }

    @Test
    fun changeTitleFocusTest_titleBlank() = runBlocking {

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.InFocus))

        assertFalse(viewModel.stateTitle.value.isHintVisible)

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.outFocus))

        assertTrue(viewModel.stateTitle.value.isHintVisible)

    }

    @Test
    fun changeTitleFocusTest_titleNotBlank() = runBlocking {

        val title = "My clean code"

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(title))

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.InFocus))

        assertEquals(true, viewModel.stateTitle.value.isHintVisible)

        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(StateFocusFake.outFocus))

        assertTrue(viewModel.stateTitle.value.isHintVisible)

    }

    @Test
    fun enteredContentTest_OnBlank() = runBlocking {
        val content = "This is a content the your code clean"
        val expectedState = NoteTextFieldState().copy(
            text = content,
            hint = "Please, write your Content"
        )

        viewModel.onEvent(AddEditNoteEvent.EnteredContent(value = content))

        assertEquals(
            expectedState,
            viewModel.stateContent.value
        )

        viewModel.onEvent(AddEditNoteEvent.EnteredContent(value = ""))

        assertEquals(
            expectedState,
            viewModel.stateContent.value
        )
    }

    @Test
    fun changeTitleFocusTest_contentBlank() = runBlocking {

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.InFocus))

        assertFalse(viewModel.stateContent.value.isHintVisible)

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.outFocus))

        assertTrue(viewModel.stateContent.value.isHintVisible)

    }

    @Test
    fun changeTitleFocusTest_contentNotBlank() = runBlocking {

        val content = "My clean code"

        viewModel.onEvent(AddEditNoteEvent.EnteredContent(content))

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.InFocus))

        assertTrue(viewModel.stateContent.value.isHintVisible)

        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(StateFocusFake.outFocus))

        assertTrue(viewModel.stateContent.value.isHintVisible)

    }

    @Test
    fun changeColorTest() = runBlocking {
        var newColor = Note.noteColor.random().toArgb()
        viewModel.onEvent(AddEditNoteEvent.ChangeColor(newColor))
        assertEquals(newColor, viewModel.stateColor.value)
        newColor = Note.noteColor.random().toArgb()
        viewModel.onEvent(AddEditNoteEvent.ChangeColor(newColor))
        assertEquals(newColor, viewModel.stateColor.value)
    }

    @Test
    fun saveNote_newNote_complete() = runBlocking {

        val note = Note(
            title = "Saving test 1",
            content = "intent save first test",
            color = Note.noteColor.random().toArgb()
        )

        var timestamp: Long? = null

        coEvery {
            addCases(note = capture(noteSlot))
        }answers{
            println(noteSlot)
            timestamp = noteSlot.captured.timestamp
        }

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(note.title))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent(note.content))
        viewModel.onEvent(AddEditNoteEvent.ChangeColor(note.color!!))

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        coVerify(exactly = 1) { addCases(note.copy(timestamp = timestamp!!)) }

        confirmVerified(addCases)

    }

    @Test
    fun saveNote_existedNote_complete() = runBlocking {
        val note = Note(
            id = 1,
            title = "Saving test 1",
            content = "intent save first test",
            color = Note.noteColor.random().toArgb()
        )

        var timestamp: Long? = null
        coEvery {
            savedStateHandle.get<Int>("noteId")
        }coAnswers {
            1
        }

        coEvery {
            savedStateHandle.set<Int>("noteId", note.id)
        }just Runs


        coEvery {
            addCases(note = capture(noteSlot))
        }answers{

            timestamp = noteSlot.captured.timestamp
        }

        viewModel = AddEditNoteViewModel(addCases, getNoteCase, savedStateHandle)

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(note.title))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent(note.content))
        viewModel.onEvent(AddEditNoteEvent.ChangeColor(note.color!!))

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        val event = viewModel.eventFlow.first()

        assertEquals(
            UiEvent.SaveNote,
            event
        )

        coVerify(exactly = 1) { addCases(note.copy(timestamp = timestamp!!)) }

        confirmVerified(addCases)
    }

    @Test
    fun saveNote_newNote_titleBlank() = runBlocking {

        val note = Note(
            title = "",
            content = "intent save first test",
            color = Note.noteColor.random().toArgb()
        )

        coEvery {
            addCases(note = capture(noteSlot))
        }answers{
            throw InvalidNoteException(InvalidNoteException.EMPTY_TITLE)
        }

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(note.title))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent(note.content))
        viewModel.onEvent(AddEditNoteEvent.ChangeColor(note.color!!))

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        val event = viewModel.eventFlow.first()
        assertEquals(
                UiEvent.ShowSnackBar(message = InvalidNoteException.EMPTY_TITLE),
                event
            )

        coVerify(exactly = 1) { addCases(any()) }

        confirmVerified(addCases)

    }

    @Test
    fun saveNote_newNote_contentBlank() = runBlocking {
        val note = Note(
            title = "Saving test 1",
            content = "",
            color = Note.noteColor.random().toArgb()
        )

        coEvery {
            addCases(note = capture(noteSlot))
        }answers{
            throw InvalidNoteException(InvalidNoteException.EMPTY_CONTENT)
        }

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(note.title))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent(note.content))
        viewModel.onEvent(AddEditNoteEvent.ChangeColor(note.color!!))

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        val event = viewModel.eventFlow.first()
        assertEquals(
            UiEvent.ShowSnackBar(message = InvalidNoteException.EMPTY_CONTENT),
            event
        )

        coVerify(exactly = 1) { addCases(any()) }

        confirmVerified(addCases)

    }

}
