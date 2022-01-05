package ve.com.teeac.mynotes.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.mynotes.core.utils.TestTag
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType
import ve.com.teeac.mynotes.feature_note.presentation.notes.NotesState

@Composable
fun OrderSectionOld(
    modifier: Modifier = Modifier,
    notesOrder: NotesOrder = NotesOrder.Date(OrderType.Descending),
    onOrderChange: (NotesOrder) -> Unit
) {
    Column(
        modifier= modifier,
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ){
            DefaultRadioButton(
                text = "Title",
                selected = notesOrder is NotesOrder.Title,
                onSelect = {onOrderChange(NotesOrder.Title(notesOrder.orderType))},
                testTag = TestTag.TITLE_SELECT
            )
            Spacer(modifier = Modifier.width(16.dp))
            DefaultRadioButton(
                text = "Date",
                selected = notesOrder is NotesOrder.Date,
                onSelect = {onOrderChange(NotesOrder.Date(notesOrder.orderType))},
                testTag=TestTag.DATE_SELECT
            )
            Spacer(modifier = Modifier.width(16.dp))
            DefaultRadioButton(
                text = "Color",
                selected = notesOrder is NotesOrder.Color,
                onSelect = {onOrderChange(NotesOrder.Color(notesOrder.orderType))},
                testTag=TestTag.COLOR_SELECT
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ){
            DefaultRadioButton(
                text = "Ascending",
                selected = notesOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(
                        notesOrder.copy(
                            OrderType.Ascending
                        )
                    )
                },
                testTag = TestTag.ASCENDING_SELECT
            )
            Spacer(modifier = Modifier.width(16.dp))

            DefaultRadioButton(
                text = "Descending",
                selected = notesOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(
                        notesOrder.copy(
                            OrderType.Descending
                        )
                    )
                },
                testTag=TestTag.DESCENDING_SELECT
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderSectionPrev() {
    val privateState = remember{mutableStateOf(NotesState())}
    val state: State<NotesState> = privateState
    OrderSectionOld(
        notesOrder = state.value.notesOrder,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onOrderChange= {
            privateState.value = state.value.copy(notesOrder = it)
        }
    )
}