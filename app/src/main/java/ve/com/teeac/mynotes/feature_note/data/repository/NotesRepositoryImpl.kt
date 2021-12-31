package ve.com.teeac.mynotes.feature_note.data.repository

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mynotes.feature_note.data.data_source.NotesDao
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository

class NotesRepositoryImpl (
    private val dao: NotesDao
        ) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getAllNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNote(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insert(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.delete(note)
    }
}