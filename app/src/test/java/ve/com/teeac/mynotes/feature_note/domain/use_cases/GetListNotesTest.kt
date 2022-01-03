package ve.com.teeac.mynotes.feature_note.domain.use_cases

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

import ve.com.teeac.mynotes.feature_note.data.repository.FakeNotesRepository
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType

class GetListNotesTest {

    private var repository = FakeNotesRepository()
    private  var getListNotes = GetListNotes(repository)
    private var listRepository = repository.listNotes

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking{
        val notes = getListNotes(NotesOrder.Title(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by title descending, correct order`() = runBlocking{
        val notes = getListNotes(NotesOrder.Title(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking{
        val notes = getListNotes(NotesOrder.Date(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by date descending, correct order`() = runBlocking{
        val notes = getListNotes(NotesOrder.Date(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by color ascending, correct order`() = runBlocking{
        val notes = getListNotes(NotesOrder.Color(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i+1].color)
        }
    }

    @Test
    fun `Order notes by color descending, correct order`() = runBlocking{
        val notes = getListNotes(NotesOrder.Color(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i+1].color)
        }
    }


}