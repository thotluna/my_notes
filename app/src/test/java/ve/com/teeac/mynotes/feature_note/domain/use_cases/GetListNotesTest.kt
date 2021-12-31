package ve.com.teeac.mynotes.feature_note.domain.use_cases

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

import ve.com.teeac.mynotes.feature_note.data.repository.FakeNotesRepository
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType

class GetListNotesTest {

    private var repository = FakeNotesRepository()
    private  var getListNotes = GetListNotes(repository)
    private var listRepository = repository.listNotes

    @Test
    fun getListNotesOrderByTitleAscending() = runBlocking{
        val list = getListNotes(NotesOrder.Title(OrderType.Ascending)).first()

        assertEquals(listRepository.size, list.size)
        val sorted = listRepository.sortedBy{it.title}
        val firstNote: Note = sorted.first()
        assertEquals(firstNote, list.first())
    }

    @Test
    fun getListNotesOrderByTitleDescending() = runBlocking{
        val list = getListNotes(NotesOrder.Title(OrderType.Descending)).first()

        assertEquals(listRepository.size, list.size)

        val sorted = listRepository.sortedByDescending { note -> note.title }

        val firstNote: Note = sorted.first()

        assertEquals(firstNote, list.first())
    }

    @Test
    fun getListNotesOrderByDateAscending() = runBlocking{
        val list = getListNotes(NotesOrder.Date(OrderType.Ascending)).first()

        assertEquals(listRepository.size, list.size)
        val sorted = listRepository.sortedBy{it.timestamp}
        val firstNote: Note = sorted.first()
        assertEquals(firstNote, list.first())
    }

    @Test
    fun getListNotesOrderByDateDescending() = runBlocking{
        val list = getListNotes(NotesOrder.Date(OrderType.Descending)).first()

        assertEquals(listRepository.size, list.size)

        val sorted = listRepository.sortedByDescending { note -> note.timestamp }

        val firstNote: Note = sorted.first()

        assertEquals(firstNote, list.first())
    }

    @Test
    fun getListNotesOrderByColorAscending() = runBlocking{
        val list = getListNotes(NotesOrder.Color(OrderType.Ascending)).first()

        assertEquals(listRepository.size, list.size)
        val sorted = listRepository.sortedBy{it.color}
        val firstNote: Note = sorted.first()
        assertEquals(firstNote, list.first())
    }

    @Test
    fun getListNotesOrderByColorDescending() = runBlocking{
        val list = getListNotes(NotesOrder.Color(OrderType.Descending)).first()

        assertEquals(listRepository.size, list.size)

        val sorted = listRepository.sortedByDescending { note -> note.color }

        val firstNote: Note = sorted.first()

        assertEquals(firstNote, list.first())
    }
}