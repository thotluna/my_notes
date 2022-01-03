package ve.com.teeac.mynotes.feature_note

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite
import ve.com.teeac.mynotes.feature_note.domain.use_cases.AddNoteTest
import ve.com.teeac.mynotes.feature_note.domain.use_cases.GetListNotesTest
import ve.com.teeac.mynotes.feature_note.presentation.addeditnote.AddEditNoteViewModelTest
import ve.com.teeac.mynotes.feature_note.presentation.notes.NotesViewModelTest

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    AddNoteTest::class,
    GetListNotesTest::class,
    AddEditNoteViewModelTest::class,
    NotesViewModelTest::class
)
class MainSuite {
}