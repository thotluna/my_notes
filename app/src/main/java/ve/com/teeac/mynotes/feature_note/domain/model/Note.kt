package ve.com.teeac.mynotes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ve.com.teeac.mynotes.ui.theme.*

@Entity
data class Note(
    @PrimaryKey
    val id: Int?,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int

){
    companion object {
        val noteColor = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}
