package ve.com.teeac.mynotes.feature_note.presentation.addeditnote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ve.com.teeac.mynotes.feature_note.domain.use_cases.AddNote
import ve.com.teeac.mynotes.feature_note.domain.use_cases.GetNote
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val addUseCase: AddNote,
    private val getUseCase: GetNote,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _stateTitle = mutableStateOf(NoteTextFieldState())
    val stateTitle: State<NoteTextFieldState> = _stateTitle

    private val _stateContent = mutableStateOf(NoteTextFieldState())
    val stateContent: State<NoteTextFieldState> = _stateContent

    private val _stateColor = mutableStateOf(NoteTextFieldState())
    val stateColor: State<NoteTextFieldState> = _stateColor

//    private val _eventFlow = MutableSharedFlow<UiEvent>()
//    val eventFlow = _eventFlow.asSharedFlow()

}