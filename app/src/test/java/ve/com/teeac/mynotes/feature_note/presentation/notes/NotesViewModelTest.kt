package ve.com.teeac.mynotes.feature_note.presentation.notes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mynotes.feature_note.data.repository.FakeNotesRepository
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

    private lateinit var repository: FakeNotesRepository
    private lateinit var useCases: NotesUseCases
    private lateinit var viewModel: NotesViewModel
    private lateinit var state: NotesState

    @Before
    fun setUp() {

        repository = FakeNotesRepository()
        useCases = NotesUseCases(
            getListNotes = GetListNotes(repository),
            getNote = GetNote(repository),
            addNote = AddNote(repository),
            deleteNotes = DeleteNote(repository)
        )

        viewModel = NotesViewModel(useCases)
        state= viewModel.state.value
    }

    @Test
    fun viewModelInitial() = runBlocking {

        assertEquals(NotesOrder.Date::class, state.notesOrder::class)
        assertEquals(OrderType.Descending, state.notesOrder.orderType)
        assertEquals(repository.listNotes.size, state.notes.size)
        assertFalse(state.isOrderSectionVisible)

    }

    @Test
    fun verifyToggleOrderSection() = runBlocking {

        assertFalse(state.isOrderSectionVisible)
        viewModel.onEvent(NotesEvent.ToggleOrderSection)
        state= viewModel.state.value
        assertTrue(state.isOrderSectionVisible)
        viewModel.onEvent(NotesEvent.ToggleOrderSection)
        state= viewModel.state.value
        assertFalse(state.isOrderSectionVisible)
    }

    @Test
    fun orderListByTitleAscending() = runBlocking {
        viewModel.onEvent(NotesEvent.Order(NotesOrder.Title(OrderType.Ascending)))
        state = viewModel.state.value

        val sorted = repository.listNotes.sortedBy { it.title }

        assertEquals(sorted.first().title, state.notes.first().title )
    }

    @Test
    fun orderListByTitleDescending() = runBlocking {
        viewModel.onEvent(NotesEvent.Order(NotesOrder.Title(OrderType.Descending)))
        state = viewModel.state.value

        val sorted = repository.listNotes.sortedByDescending { it.title }

        assertEquals(sorted.first().title, state.notes.first().title )
    }

    @Test
    fun orderListByDateAscending() = runBlocking {
        viewModel.onEvent(NotesEvent.Order(NotesOrder.Date(OrderType.Ascending)))
        state = viewModel.state.value

        val sorted = repository.listNotes.sortedBy { it.timestamp }

        assertEquals(sorted.first().title, state.notes.first().title )
    }

    @Test
    fun orderListByDateDescending() = runBlocking {
        viewModel.onEvent(NotesEvent.Order(NotesOrder.Date(OrderType.Descending)))
        state = viewModel.state.value

        val sorted = repository.listNotes.sortedByDescending { it.timestamp }

        assertEquals(sorted.first().title, state.notes.first().title )
    }

    @Test
    fun orderListByColorAscending() = runBlocking {
        viewModel.onEvent(NotesEvent.Order(NotesOrder.Color(OrderType.Ascending)))
        state = viewModel.state.value

        val sorted = repository.listNotes.sortedBy { it.color }

        assertEquals(sorted.first().title, state.notes.first().title )
    }

    @Test
    fun orderListByColorDescending() = runBlocking {
        viewModel.onEvent(NotesEvent.Order(NotesOrder.Color(OrderType.Descending)))
        state = viewModel.state.value

        val sorted = repository.listNotes.sortedByDescending { it.color }

        assertEquals(sorted.first().title, state.notes.first().title )
    }

    @Test
    fun deleteNoteByViewModel() = runBlocking{
        val itemCount = state.notes.size
        val note = state.notes.first()
        viewModel.onEvent(NotesEvent.DeleteNote(note))

        viewModel.onEvent(NotesEvent.Refresh)

        state = viewModel.state.value

        assertEquals(itemCount - 1, state.notes.size)
        assertNull(repository.getNoteById(note.id!!))

    }

    @Test
    fun deleteNoteNullByViewModel() = runBlocking{
        val itemCount = state.notes.size
        val note = Note()
        viewModel.onEvent(NotesEvent.DeleteNote(note))

        viewModel.onEvent(NotesEvent.Refresh)

        state = viewModel.state.value

        assertEquals(itemCount, state.notes.size)

    }

    @Test
    fun restoreDeleteNoteByViewModel() = runBlocking{
        val itemCount = state.notes.size
        val note = state.notes.first()
        viewModel.onEvent(NotesEvent.DeleteNote(note))

        viewModel.onEvent(NotesEvent.Refresh)

        state = viewModel.state.value

        assertEquals(itemCount - 1, state.notes.size)
        assertNull(repository.getNoteById(note.id!!))

        viewModel.onEvent(NotesEvent.RestoreNote)

        viewModel.onEvent(NotesEvent.Refresh)

        state = viewModel.state.value

        assertEquals(itemCount, state.notes.size)
        assertNotNull(repository.getNoteById(note.id!!))

    }

}