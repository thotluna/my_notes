package ve.com.teeac.mynotes.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ve.com.teeac.mynotes.feature_note.domain.model.Note
import ve.com.teeac.mynotes.feature_note.domain.use_cases.NotesUseCases
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor(
    private val useCases: NotesUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var getNotesJob: Job? = null

    private var recentlyDeletedNote: Note? = null

    init {
        this.getNoteList(NotesOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event){
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.Order -> {
                if(state.value.notesOrder::class == event.notesOrder::class &&
                    state.value.notesOrder.orderType == event.notesOrder.orderType
                        ){
                    return
                }
                getNoteList(event.notesOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    useCases.deleteNotes(event.note)
                    recentlyDeletedNote = event.note
                }
//                getNoteList(state.value.notesOrder)
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    useCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
//                getNoteList(state.value.notesOrder)
            }
        }

    }

    private fun getNoteList(notesOrder: NotesOrder) {
        getNotesJob?.cancel()
        getNotesJob = useCases.getListNotes(notesOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    notesOrder = notesOrder
                )
            }
            .launchIn(viewModelScope)
    }

}