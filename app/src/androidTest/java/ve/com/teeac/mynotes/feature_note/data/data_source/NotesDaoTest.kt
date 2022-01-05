package ve.com.teeac.mynotes.feature_note.data.data_source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import org.junit.Assert.*
import org.junit.runner.RunWith
import ve.com.teeac.mynotes.di.AppModule

import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
@SmallTest
@RunWith(AndroidJUnit4::class)
class NotesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    lateinit var repository: NoteRepository

    private  var notes = listOf(
        Note(
            title = "test1",
            id= 1,
            content = "Test1",
            timestamp = System.currentTimeMillis(),
            color = 1
        ),
        Note(
            title = "test2",
            id= 2,
            content = "Test2",
            timestamp = System.currentTimeMillis(),
            color = 2
        ),
    )

    @Before
    fun setup() = runTest {
        hiltRule.inject()
        notes.forEach{ note ->
            repository.insertNote(note)
        }
    }


    @Test
    fun findById()= runTest {
        val noteByDb= repository.getNoteById(notes.first().id!!)
        assertEquals(notes.first(), noteByDb)
    }

    @Test
    fun findByIdNotExist()= runTest {
        val noteByDb= repository.getNoteById(100)
        assertEquals(null, noteByDb)
    }


    @Test
    fun getAllNotes() = runTest{
        val listNotes = repository.getNotes().first()

        assertEquals(notes.size, listNotes.size)
        assertEquals(notes.first(), listNotes.first())
    }

    @Test
    fun getAllNotesEmpty() = runTest{
        notes.forEach{ note ->
            repository.deleteNote(note)
        }
        val listNotes = repository.getNotes().first()

        assertEquals(0, listNotes.size)
    }

    @Test
    fun deleteNoteDb() = runTest{

        val listNotes = repository.getNotes().first()

        assertEquals(notes.size, listNotes.size)

        repository.deleteNote(listNotes.first())

        val newListNotes = repository.getNotes().first()

        assertEquals(notes.size - 1, newListNotes.size)
    }
}