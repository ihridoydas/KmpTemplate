package template

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import template.common.screens.ViewScreen

class ViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testViewScreenContent() {
        var backPressed = false
        composeTestRule.setContent {
            ViewScreen(onBackPress = { backPressed = true })
        }

        // Verify text is displayed
        composeTestRule.onNodeWithText("Nice to meet you.!").assertExists()
        
        // Verify Top Bar title
        composeTestRule.onNodeWithText("Welcome").assertExists()

        // Perform back navigation
        composeTestRule.onNodeWithContentDescription("Nav Icon").performClick()

        // Verify back press occurred
        assert(backPressed)
    }
}
