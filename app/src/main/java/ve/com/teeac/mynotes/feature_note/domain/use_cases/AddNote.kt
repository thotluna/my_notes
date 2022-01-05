package ve.com.teeac.mynotes.feature_note.domain.use_cases

import ve.com.teeac.mynotes.feature_note.domain.model.InvalidNoteException
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class AddNote @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note){

        if(note.title.isEmpty()){
            throw InvalidNoteException(InvalidNoteException.EMPTY_TITLE)
        }
        if(note.content.isEmpty()){
            throw InvalidNoteException(InvalidNoteException.EMPTY_CONTENT)
        }

        repository.insertNote(note)
    }

}
