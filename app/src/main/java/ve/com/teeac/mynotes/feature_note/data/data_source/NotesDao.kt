package ve.com.teeac.mynotes.feature_note.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mynotes.feature_note.domain.model.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM Note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :noteId")
    suspend fun getNote(noteId: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)


}