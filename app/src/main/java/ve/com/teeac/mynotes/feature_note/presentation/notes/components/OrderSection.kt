package ve.com.teeac.mynotes.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ve.com.teeac.mynotes.core.utils.TestTag
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    notesOrder: NotesOrder = NotesOrder.Date(OrderType.Descending),
    onOrderChange: (NotesOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = notesOrder is NotesOrder.Title,
                onSelect = { onOrderChange(NotesOrder.Title(notesOrder.orderType)) },
                testTag = TestTag.TITLE_SELECT
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = notesOrder is NotesOrder.Date,
                onSelect = { onOrderChange(NotesOrder.Date(notesOrder.orderType)) },
                testTag = TestTag.DATE_SELECT
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Color",
                selected = notesOrder is NotesOrder.Color,
                onSelect = { onOrderChange(NotesOrder.Color(notesOrder.orderType)) },
                testTag = TestTag.COLOR_SELECT
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = notesOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(notesOrder.copy(OrderType.Ascending))
                },
                testTag = TestTag.ASCENDING_SELECT
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = notesOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(notesOrder.copy(OrderType.Descending))
                },
                testTag = TestTag.DESCENDING_SELECT
            )
        }
    }
}