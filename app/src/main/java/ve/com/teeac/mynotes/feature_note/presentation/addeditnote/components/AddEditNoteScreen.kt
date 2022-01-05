package ve.com.teeac.mynotes.feature_note.presentation.addeditnote.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ve.com.teeac.mynotes.feature_note.presentation.addeditnote.AddEditNoteViewModel

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {

}