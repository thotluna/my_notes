package ve.com.teeac.mynotes.feature_note.domain.use_cases

import ve.com.teeac.mynotes.feature_note.domain.model.InvalidNoteException
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.repository.NoteRepository

class AddNote (
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note){

        if(note.title.isEmpty()){
            throw InvalidNoteException("The Title of the note can't be empty")
        }
        if(note.content.isEmpty()){
            throw InvalidNoteException("The Content of the note can't be empty")
        }

        repository.insertNote(note)
    }

}
