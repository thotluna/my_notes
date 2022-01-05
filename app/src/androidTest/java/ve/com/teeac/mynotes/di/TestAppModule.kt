package ve.com.teeac.mynotes.di

import android.app.Application
import android.content.Context
import androidx.compose.ui.graphics.toArgb
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ve.com.teeac.mynotes.feature_note.data.data_source.NotesDatabase
import ve.com.teeac.mynotes.feature_note.data.repository.NotesRepositoryImpl
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository
import ve.com.teeac.mynotes.feature_note.domain.use_cases.*
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NotesDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            NotesDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(db: NotesDatabase): NoteRepository {
        return NotesRepositoryImpl(db.notesDao)
    }

    @Provides
    @Singleton
    fun provideAddUseCases(repository: NoteRepository): AddNote {
        return AddNote(repository)
    }

    @Provides
    @Singleton
    fun provideGetUseCases(repository: NoteRepository): GetNote {
        return GetNote(repository)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {

        (1..5).forEach{
            GlobalScope.launch{
                repository.insertNote(
                    Note(
                        title = "Title $it",
                        content = "Content to $5 card",
                        color = Note.noteColor.random().toArgb()
                    )
                )
            }
        }
        return NotesUseCases(
            getListNotes = GetListNotes(repository),
            getNote = GetNote(repository),
            addNote = AddNote(repository),
            deleteNotes = DeleteNote(repository)
        )
    }


}