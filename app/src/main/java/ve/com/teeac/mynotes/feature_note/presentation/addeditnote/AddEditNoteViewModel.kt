package ve.com.teeac.mynotes.feature_note.presentation.addeditnote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ve.com.teeac.mynotes.feature_note.domain.model.InvalidNoteException
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.use_cases.AddNote
import ve.com.teeac.mynotes.feature_note.domain.use_cases.GetNote
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val addUseCase: AddNote,
    private val getUseCase: GetNote,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _stateTitle = mutableStateOf(
        NoteTextFieldState(
            text = "",
            hint = "Please, write your title",
            isHintVisible = true
        )
    )
    val stateTitle: State<NoteTextFieldState> = _stateTitle

    private val _stateContent = mutableStateOf(
        NoteTextFieldState(
            text = "",
            hint = "Please, write your Content",
            isHintVisible = true
        )
    )
    val stateContent: State<NoteTextFieldState> = _stateContent

    private val _stateColor = mutableStateOf(Note.noteColor.random().toArgb())
    val stateColor: State<Int> = _stateColor

    private var currentNoteId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    init {

        savedStateHandle.get<Int>("noteId")?.let { id ->
            if(id != -1){
                viewModelScope.launch {
                    getUseCase(id)?.also { note ->
                        currentNoteId = note.id
                        _stateTitle.value = stateTitle.value.copy(text = note.title)
                        _stateContent.value = stateContent.value.copy(text = note.content)
                        _stateColor.value = note.color!!
                    }
                }
            }
        }


    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                if(event.value.isBlank()) return
                if(event.value == stateTitle.value.text) return
                _stateTitle.value = stateTitle.value.copy( text = event.value)
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                if(stateTitle.value.text.isNotBlank()) return
                _stateTitle.value = stateTitle.value.copy(
                    isHintVisible = !stateTitle.value.isHintVisible
                )

            }
            is AddEditNoteEvent.EnteredContent -> {
                if(event.value.isBlank()) return
                if(event.value == stateContent.value.text) return
                _stateContent.value = stateContent.value.copy( text = event.value)
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                if(stateContent.value.text.isNotBlank()) return
                _stateContent.value = stateContent.value.copy(
                    isHintVisible = !stateContent.value.isHintVisible
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                if(event.color == stateColor.value) return
                _stateColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                val note = Note(
                    id = currentNoteId,
                    title = stateTitle.value.text,
                    content = stateContent.value.text,
                    color = stateColor.value
                )

                viewModelScope.launch{
                    try {
                        addUseCase(note)
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch(e: InvalidNoteException){
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }

            }
        }

    }

}

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
    object SaveNote: UiEvent()
}
