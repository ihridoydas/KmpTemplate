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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import template.common.ui.MainAnimationNavHost
import template.storage.local.language.Language
import template.storage.local.language.LanguageDataStore
import template.storage.local.theme.ThemeLocalDataStore
import template.storage.local.theme.ThemeMode
import template.theme.TemplateTheme

@Composable
fun App(onLanguageChange: (String) -> Unit = {}, onThemeChange: (Boolean) -> Unit = {}) {
    KoinContext {
        val languageDataStore: LanguageDataStore = koinInject()
        val themeLocalDataStore: ThemeLocalDataStore = koinInject()

        // 1. Initialize LanguageManager
        SideEffect {
            template.common.util.LanguageManager
                .init(languageDataStore)
        }

        // 2. Observe the centralized states
        val languageState by template.common.util.LanguageManager.currentLanguage
            .collectAsState()
        val themeMode by themeLocalDataStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)

        // 3. Wait for the state to transition away from UNKNOWN before rendering
        if (languageState == Language.UNKNOWN) {
            return@KoinContext
        }

        val languageCode = remember(languageState) {
            when (languageState) {
                Language.ENGLISH -> "en"
                Language.JAPANESE -> "ja"
                Language.BENGALI -> "bn"
                Language.SYSTEM -> ""
                else -> ""
            }
        }

        val isDarkTheme = when (themeMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            else -> isSystemInDarkTheme()
        }

        LaunchedEffect(languageCode) {
            onLanguageChange(languageCode)
        }

        LaunchedEffect(isDarkTheme) {
            onThemeChange(isDarkTheme)
        }

        template.common.util.ProvideAppLocale(languageCode, isDarkTheme) {
            TemplateTheme(useDarkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    key(languageCode) {
                        MainAnimationNavHost()
                    }
                }
            }
        }
    }
}
