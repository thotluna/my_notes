package ve.com.teeac.mynotes.feature_note.presentation.notes.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ButtonOrderSection(
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick:() -> Unit,
    testTag: String = ""
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(testTag)
    ) {
        Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = contentDescription
        )
    }

}

@Preview
@Composable
fun ButtonOrderSectionPreview() {
    ButtonOrderSection(
        contentDescription = "Button preview",
        onClick = {}
    )
}