package ve.com.teeac.mynotes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ve.com.teeac.mynotes.ui.theme.*

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val color: Int? = null,

){
    companion object {
        val noteColor = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String):Exception(message){
    companion object{
        const val EMPTY_TITLE = "The Title of the note can't be empty"
        const val EMPTY_CONTENT = "The Content of the note can't be empty"
    }
}

