package ve.com.teeac.mynotes.feature_note.data.data_source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

import org.junit.Assert.*
import org.junit.runner.RunWith

import ve.com.teeac.mynotes.feature_note.domain.model.Note

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
@RunWith(AndroidJUnit4::class)
class NotesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    @Named("test_db")
    lateinit var database: NotesDatabase
    private lateinit var notesDao: NotesDao

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
    fun setup() = runBlockingTest {
        hiltRule.inject()
        notesDao = database.notesDao
        notes.forEach{ note ->
            notesDao.insert(note)
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun findById()= runBlockingTest {
        val noteByDb= notesDao.getNote(notes.first().id!!)
        assertEquals(notes.first(), noteByDb)
    }

    @Test
    fun findByIdNotExist()= runBlockingTest {
        val noteByDb= notesDao.getNote(100)
        assertEquals(null, noteByDb)
    }


    @Test
    fun getAllNotes() = runBlockingTest{
        val listNotes = notesDao.getAllNotes().first()

        assertEquals(notes.size, listNotes.size)
        assertEquals(notes.first(), listNotes.first())
    }

    @Test
    fun getAllNotesEmpty() = runBlockingTest{
        notes.forEach{ note ->
            notesDao.delete(note)
        }
        val listNotes = notesDao.getAllNotes().first()

        assertEquals(0, listNotes.size)
    }

    @Test
    fun deleteNoteDb() = runBlockingTest{

        val listNotes = notesDao.getAllNotes().first()

        assertEquals(notes.size, listNotes.size)

        notesDao.delete(listNotes.first())

        val newListNotes = notesDao.getAllNotes().first()

        assertEquals(notes.size - 1, newListNotes.size)
    }
}