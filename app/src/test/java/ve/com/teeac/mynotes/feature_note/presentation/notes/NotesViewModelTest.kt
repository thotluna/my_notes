package ve.com.teeac.mynotes.feature_note.presentation.notes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.use_cases.*
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType
import ve.com.teeac.mynotes.feature_note.utils.MainCoroutineRule


@ExperimentalCoroutinesApi
class NotesViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var getListNotes: GetListNotes
    @MockK
    private lateinit var getNote: GetNote
    @MockK
    private lateinit var addNote: AddNote
    @MockK
    private lateinit var deleteNotes: DeleteNote

    private lateinit var useCases: NotesUseCases
    private lateinit var viewModel: NotesViewModel
    private lateinit var state: NotesState

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCases = NotesUseCases(
            getListNotes = getListNotes,
            getNote = getNote,
            addNote = addNote,
            deleteNotes = deleteNotes
        )

        coEvery {
            getListNotes(any())
        } coAnswers {
            flow { emit(listOf(Note(),Note())) }
        }

        viewModel = NotesViewModel(useCases)
        state= viewModel.state.value
    }

    @Test
    fun `State initial of NoteViewModel`() = runBlocking {

        assertThat(state.notesOrder::class).isEqualTo(NotesOrder.Date::class)
        assertThat(state.notesOrder.orderType).isEqualTo(OrderType.Descending)
        assertThat(state.notes.size).isEqualTo(2)
        assertThat(state.isOrderSectionVisible).isFalse()

    }

    @Test
    fun `Send Toggle order section event, Value is false`() = runBlocking {

        viewModel.onEvent(NotesEvent.ToggleOrderSection)
        assertThat(viewModel.state.value.isOrderSectionVisible).isTrue()
    }


    @Test
    fun `Send order event by title ascending, ordered`() = runBlocking {

        val notesOrder = NotesOrder.Title(OrderType.Ascending)

        viewModel.onEvent(NotesEvent.Order(notesOrder))
        state = viewModel.state.value

        assertThat(state.notesOrder::class).isEqualTo(NotesOrder.Title::class)
        assertThat(state.notesOrder.orderType).isEqualTo(notesOrder.orderType)

        coVerify(exactly = 2){getListNotes(any())}
        confirmVerified(getListNotes)
    }

    @Test
    fun `Send order event by title descending, ordered`() = runBlocking {
        val notesOrder = NotesOrder.Title(OrderType.Descending)

        viewModel.onEvent(NotesEvent.Order(notesOrder))
        state = viewModel.state.value

        assertThat(state.notesOrder::class).isEqualTo(NotesOrder.Title::class)
        assertThat(state.notesOrder.orderType).isEqualTo(notesOrder.orderType)

        coVerify(exactly = 2){getListNotes(any())}
        confirmVerified(getListNotes)
    }

    @Test
    fun `Send order event by date ascending, ordered`() = runBlocking {
        val notesOrder = NotesOrder.Date(OrderType.Ascending)

        viewModel.onEvent(NotesEvent.Order(notesOrder))
        state = viewModel.state.value

        assertThat(state.notesOrder::class).isEqualTo(NotesOrder.Date::class)
        assertThat(state.notesOrder.orderType).isEqualTo(notesOrder.orderType)

        coVerify(exactly = 2){getListNotes(any())}
        confirmVerified(getListNotes)
    }

    @Test
    fun `Send order event by date descending, don't ordered`() = runBlocking {
        val notesOrder = NotesOrder.Date(OrderType.Descending)

        viewModel.onEvent(NotesEvent.Order(notesOrder))
        state = viewModel.state.value

        assertThat(state.notesOrder::class).isEqualTo(NotesOrder.Date::class)
        assertThat(state.notesOrder.orderType).isEqualTo(notesOrder.orderType)

        coVerify(exactly = 1){getListNotes(any())}
        confirmVerified(getListNotes)
    }

    @Test
    fun `Send order event by color ascending, ordered`() = runBlocking {
        val notesOrder = NotesOrder.Color(OrderType.Ascending)

        viewModel.onEvent(NotesEvent.Order(notesOrder))
        state = viewModel.state.value

        assertThat(state.notesOrder::class).isEqualTo(NotesOrder.Color::class)
        assertThat(state.notesOrder.orderType).isEqualTo(notesOrder.orderType)

        coVerify(exactly = 2){getListNotes(any())}
        confirmVerified(getListNotes)
    }

    @Test
    fun `Send order event by color descending, ordered`() = runBlocking {
        val notesOrder = NotesOrder.Color(OrderType.Descending)

        viewModel.onEvent(NotesEvent.Order(notesOrder))
        state = viewModel.state.value

        assertThat(state.notesOrder::class).isEqualTo(NotesOrder.Color::class)
        assertThat(state.notesOrder.orderType).isEqualTo(notesOrder.orderType)

        coVerify(exactly = 2){getListNotes(any())}
        confirmVerified(getListNotes)
    }

    @Test
    fun deleteNoteByViewModel() = runBlocking{

        val note = Note()

        coEvery {
            deleteNotes(any())
        } returns Unit


        viewModel.onEvent(NotesEvent.DeleteNote(note))

        coVerify(exactly = 1){getListNotes(any())}
        coVerify(exactly = 1){deleteNotes(note)}
        confirmVerified(getListNotes)
        confirmVerified(deleteNotes)


    }

    @Test
    fun restoreDeleteNoteByViewModel() = runBlocking{

        val note = Note()
        coEvery {
            deleteNotes(note)
        } returns Unit

        coEvery {
            addNote(note)
        } returns Unit

        viewModel.onEvent(NotesEvent.DeleteNote(note))

        viewModel.onEvent(NotesEvent.RestoreNote)


        coVerify(exactly = 1){getListNotes(any())}
        coVerify(exactly = 1){deleteNotes(note)}
        coVerify(exactly = 1){addNote(note)}
        confirmVerified(getListNotes)
        confirmVerified(deleteNotes)
        confirmVerified(addNote)

    }

}