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
package template.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import template.common.di.initKoin
import template.common.util.PlatformUtils

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    println("Web App: main() starting...")
    try {
        println("Web App: Initializing Koin...")
        initKoin()
        println("Web App: Koin initialized.")
    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
        println("Web App: Koin initialization failed: ${e.message}")
    }

    val canvas = document.getElementById("ComposeTarget")
    if (canvas == null) {
        println("Web App ERROR: 'ComposeTarget' element not found in index.html!")
    } else {
        println("Web App: Found 'ComposeTarget' element.")
    }

    ComposeViewport(viewportContainerId = "ComposeTarget") {
        println("Web App: Rendering App...")
        App(
            onLanguageChange = { code ->
                PlatformUtils.changeLanguage(code)
            },
            onThemeChange = { isDark ->
                PlatformUtils.changeTheme(isDark)
            },
        )
    }
}
