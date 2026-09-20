/*
* MIT License
*
* Copyright (c) 2026 Hridoy Chandra Das
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/
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
                onToggle = { toggledMode = it },
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
                onToggle = { toggledMode = it },
            )
        }

        onNodeWithContentDescription("Toggle Theme").performClick()

        // Dark -> Light
        assertEquals(ThemeMode.LIGHT, toggledMode)
    }
}
