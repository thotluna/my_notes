package ve.com.teeac.mynotes.feature_note.presentation.notes.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.mynotes.core.utils.TestTag
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType
import ve.com.teeac.mynotes.feature_note.presentation.notes.NotesState

@ExperimentalAnimationApi
@Composable
fun HeadNotes(
    title: String,
    contentDescriptionButton: String,
    isOrderSectionVisible: Boolean,
    modifier: Modifier = Modifier,
    notesOrder: NotesOrder = NotesOrder.Date(OrderType.Descending),
    onOrderChange: (NotesOrder) -> Unit,
    onClick: () -> Unit,
    testTag: String = title,
) {
    Column(
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement . SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleHeadNotes(text = title)
            ButtonOrderSection(
                contentDescription = contentDescriptionButton,
                onClick = onClick,
                testTag = TestTag.ORDER_SECTION
            )
        }
        AnimatedVisibility(
            visible = isOrderSectionVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                notesOrder = notesOrder,
                onOrderChange = onOrderChange
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun HeadNotesPrev() {
    val privateState = remember{ mutableStateOf(NotesState()) }
    val state: State<NotesState> = privateState

    HeadNotes(
        title = "Prueba",
        contentDescriptionButton = "Button section",
        isOrderSectionVisible = state.value.isOrderSectionVisible,
        notesOrder= state.value.notesOrder,
        onOrderChange={ privateState.value = state.value.copy(notesOrder = it)},
        onClick={
            privateState.value = state.value.copy(
                isOrderSectionVisible = !state.value.isOrderSectionVisible
            )},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
}
