package ve.com.teeac.mynotes.feature_note.presentation.addeditnote.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    isHintVisible: Boolean = true,
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit
) {
    Box (
        modifier = modifier
    ){
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = textStyle,
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
        )
        if(isHintVisible){
            Text(
                text = hint,
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray,
                fontSize = textStyle.fontSize,
                fontFamily = textStyle.fontFamily
            )
        }
    }

}

@Preview(showBackground = true, heightDp = 35)
@Composable
fun HintPreview() {
    TransparentHintTextField(
        text = "",
        hint = "Please write your name",
        isHintVisible = true,
        onValueChange =  {  },
        onFocusChange= {},
        textStyle = MaterialTheme.typography.h5
    )
}
@Preview(showBackground = true, heightDp = 35)
@Composable
fun TextPreview() {
    TransparentHintTextField(
        text = "My Nae is Thoth",
        hint = "Please write your name",
        isHintVisible = false,
        onValueChange =  {  },
        onFocusChange= {},
        textStyle = MaterialTheme.typography.h5
    )
}