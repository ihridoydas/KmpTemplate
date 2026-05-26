package template.common.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import template.common.BaseComposeTest
import template.storage.local.theme.ThemeMode
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ThemeToggleButtonTest : BaseComposeTest() {

    @Test
    fun testToggleThemeClick() = runComposeUiTest {
        var toggledMode: ThemeMode? = null
        
        setContent {
            ThemeToggleButton(
                themeMode = ThemeMode.LIGHT,
                onToggle = { toggledMode = it }
            )
        }

        // Find the button by content description and click it
        onNodeWithContentDescription("Toggle Theme").performClick()

        // Verify that onToggle was called with the expected new mode
        // Light -> Dark
        assertEquals(ThemeMode.DARK, toggledMode)
    }

    @Test
    fun testToggleThemeClickFromDark() = runComposeUiTest {
        var toggledMode: ThemeMode? = null
        
        setContent {
            ThemeToggleButton(
                themeMode = ThemeMode.DARK,
                onToggle = { toggledMode = it }
            )
        }

        onNodeWithContentDescription("Toggle Theme").performClick()

        // Dark -> Light
        assertEquals(ThemeMode.LIGHT, toggledMode)
    }
}
