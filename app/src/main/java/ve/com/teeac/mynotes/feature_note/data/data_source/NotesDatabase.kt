package ve.com.teeac.mynotes.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import ve.com.teeac.mynotes.feature_note.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NotesDatabase: RoomDatabase() {
    abstract val notesDao: NotesDao

    companion object {
        const val DATABASE_NAME = "notes_data"
    }
}