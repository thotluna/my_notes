package ve.com.teeac.mynotes.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.mynotes.feature_note.data.data_source.NotesDatabase
import ve.com.teeac.mynotes.feature_note.data.repository.NotesRepositoryImpl
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository
import ve.com.teeac.mynotes.feature_note.domain.use_cases.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(app: Application): NotesDatabase{
        return Room.databaseBuilder(
            app,
            NotesDatabase::class.java,
            NotesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(db: NotesDatabase): NoteRepository{
        return NotesRepositoryImpl(db.notesDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
        return NotesUseCases(
            getListNotes = GetListNotes(repository),
            getNote = GetNote(repository),
            addNote = AddNote(repository),
            deleteNotes = DeleteNote(repository)
        )
    }

}