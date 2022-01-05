package ve.com.teeac.mynotes.feature_note.domain.use_cases

import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class GetNote @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int): Note?{
        return repository.getNoteById(noteId)
    }
}