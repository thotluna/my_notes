package ve.com.teeac.mynotes.feature_note.presentation.notes.components

import com.google.common.truth.Truth.assertThat
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ve.com.teeac.mynotes.core.utils.TestTag
import ve.com.teeac.mynotes.feature_note.domain.utils.NotesOrder
import ve.com.teeac.mynotes.feature_note.domain.utils.OrderType
import ve.com.teeac.mynotes.feature_note.presentation.notes.NotesState
import ve.com.teeac.mynotes.ui.theme.MyNotesTheme

@RunWith(AndroidJUnit4::class)
class OrderSectionComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val _state =  mutableStateOf(NotesState())
    private val state: State<NotesState> = _state

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MyNotesTheme{
                OrderSection(
                    notesOrder = state.value.notesOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onOrderChange= {
                        _state.value = state.value.copy(notesOrder = it)
                    }
                )
            }
        }
    }

    @Test
    fun verifyTitles(){
        composeTestRule.onNodeWithText("Title").assertExists()
        composeTestRule.onNodeWithText("Date").assertExists()
        composeTestRule.onNodeWithText("Color").assertExists()

        composeTestRule.onNodeWithText("Ascending").assertExists()
        composeTestRule.onNodeWithText("Descending").assertExists()
    }

    @Test
    fun verifySelectDefault(){
        composeTestRule.onNodeWithTag(TestTag.TITLE_SELECT).assertIsNotSelected()
        composeTestRule.onNodeWithTag(TestTag.DATE_SELECT).assertIsSelected()
        composeTestRule.onNodeWithTag(TestTag.COLOR_SELECT).assertIsNotSelected()

        composeTestRule.onNodeWithTag(TestTag.ASCENDING_SELECT).assertIsNotSelected()
        composeTestRule.onNodeWithTag(TestTag.DESCENDING_SELECT).assertIsSelected()
    }

    @Test
    fun selectTitleAscending(){
        composeTestRule.onNodeWithTag(TestTag.TITLE_SELECT).performClick()
        composeTestRule.onNodeWithTag(TestTag.DATE_SELECT).assertIsNotSelected()
        composeTestRule.onNodeWithTag(TestTag.COLOR_SELECT).assertIsNotSelected()
        assertThat(state.value.notesOrder::class).isEqualTo(NotesOrder.Title::class)

        composeTestRule.onNodeWithTag(TestTag.ASCENDING_SELECT).performClick()
        composeTestRule.onNodeWithTag(TestTag.DESCENDING_SELECT).assertIsNotSelected()
        assertThat(state.value.notesOrder.orderType).isEqualTo(OrderType.Ascending)
    }

    @Test
    fun selectColorDescending(){
        composeTestRule.onNodeWithTag(TestTag.COLOR_SELECT).performClick()
        composeTestRule.onNodeWithTag(TestTag.TITLE_SELECT).assertIsNotSelected()
        composeTestRule.onNodeWithTag(TestTag.DATE_SELECT).assertIsNotSelected()
        assertThat(state.value.notesOrder::class).isEqualTo(NotesOrder.Color::class)

        composeTestRule.onNodeWithTag(TestTag.DESCENDING_SELECT).performClick()
        composeTestRule.onNodeWithTag(TestTag.ASCENDING_SELECT).assertIsNotSelected()
        assertThat(state.value.notesOrder.orderType).isEqualTo(OrderType.Descending)
    }

    @Test
    fun selectDateAscending(){
        composeTestRule.onNodeWithTag(TestTag.DATE_SELECT).performClick()
        composeTestRule.onNodeWithTag(TestTag.TITLE_SELECT).assertIsNotSelected()
        composeTestRule.onNodeWithTag(TestTag.COLOR_SELECT).assertIsNotSelected()
        assertThat(state.value.notesOrder::class).isEqualTo(NotesOrder.Date::class)

        composeTestRule.onNodeWithTag(TestTag.ASCENDING_SELECT).performClick()
        composeTestRule.onNodeWithTag(TestTag.DESCENDING_SELECT).assertIsNotSelected()
        assertThat(state.value.notesOrder.orderType).isEqualTo(OrderType.Ascending)
    }
}