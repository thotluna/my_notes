package ve.com.teeac.mynotes.feature_note.presentation.notes

import com.google.common.truth.Truth.assertThat
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.test.*

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mynotes.core.utils.TestTag
import ve.com.teeac.mynotes.di.AppModule
import ve.com.teeac.mynotes.feature_note.presentation.MainActivity
import ve.com.teeac.mynotes.feature_note.presentation.utils.Screen
import ve.com.teeac.mynotes.ui.theme.MyNotesTheme

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()



    @ExperimentalAnimationApi
    @Before
    fun setUp()= runTest {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            MyNotesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)

                        }
                    }
                }
            }
        }
    }


    @Test
    fun validateTitle(){
        composeRule.onNodeWithText("Your note").assertIsDisplayed()
    }

    @Test
    fun toggleSectionOrder(){
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithText("Title").assertIsDisplayed()
        composeRule.onNodeWithText("Date").assertIsDisplayed()
        composeRule.onNodeWithText("Color").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithText("Title").assertDoesNotExist()
        composeRule.onNodeWithText("Date").assertDoesNotExist()
        composeRule.onNodeWithText("Color").assertDoesNotExist()
    }

    @Test
    fun displayedItemList(){
        composeRule.onNodeWithText("Title 1").assertIsDisplayed()
        composeRule.onNodeWithText("Title 2").assertIsDisplayed()
        composeRule.onNodeWithText("Title 3").assertIsDisplayed()
        composeRule.onNodeWithText("Title 4").assertIsDisplayed()
        composeRule.onNodeWithText("Title 5").assertIsDisplayed()
    }

    @Test
    fun deleteItemAndRestored(){

        composeRule.onNodeWithText("Title 2").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Delete note with title \"Title 2\"").performClick()
        composeRule.onNodeWithText("Title 2").assertDoesNotExist()
        composeRule.onNodeWithText("Note deleted").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").assertIsDisplayed()
        composeRule.onNodeWithText("Undo").performClick()
        composeRule.onNodeWithText("Title 2").assertIsDisplayed()
    }

    @Test
    fun buttonAddNewNoteExist(){
        composeRule.onNodeWithContentDescription("Add note").assertIsDisplayed()
    }
}