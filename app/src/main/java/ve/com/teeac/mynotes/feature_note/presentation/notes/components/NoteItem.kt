package ve.com.teeac.mynotes.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import ve.com.teeac.mynotes.feature_note.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
    Card(modifier = modifier,
        cutCornerSize = cutCornerSize,
        color = note.color!!,
        cornerRadius = cornerRadius
    ){
        WrapContent {
            Title(note.title)
            Spacer(modifier = Modifier.height(8.dp))
            Content(note.content)
        }
        ButtonDelete(
            title = note.title,
            onDeleteClick = onDeleteClick,
            modifier = modifier
                .align(Alignment.BottomEnd)
        )

    }

}

@Preview(heightDp = 100)
@Composable
private fun NoteItemPreview() {
    val note = Note(
        title = "Test 1",
        content = "Content Test 1",
        color = Note.noteColor.random().toArgb()
    )
    NoteItem(
        note = note,
        onDeleteClick = {}

    )
}

@Composable
fun Card(
    color: Int,
    modifier: Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        CanvasCard(modifier = Modifier.matchParentSize(), cutCornerSize, color, cornerRadius)
        content()
    }
}

@Composable
private fun WrapContent(content: @Composable ColumnScope.()-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(end = 32.dp)
    ) {
        content()
    }
}

@Composable
private fun ButtonDelete(title: String, modifier: Modifier, onDeleteClick: () -> Unit) {
    IconButton(
        onClick = onDeleteClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete note with title \"$title\"",
            tint = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun Content(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface,
        maxLines = 10,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CanvasCard(
    modifier: Modifier,
    cutCornerSize: Dp,
    backgroundColor: Int,
    cornerRadius: Dp
) {
    Canvas(
        modifier = modifier
    ) {
        val clipPath = Path().apply {
            lineTo(size.width - cutCornerSize.toPx(), 0f)
            lineTo(size.width, cutCornerSize.toPx())
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        clipPath(clipPath) {
            drawRoundRect(
                color = Color(backgroundColor),
                size = size,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
            drawRoundRect(
                color = Color(
                    ColorUtils.blendARGB(backgroundColor, 0x000000, 0.2f)
                ),
                topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    }
}


