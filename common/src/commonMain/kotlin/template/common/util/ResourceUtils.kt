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
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package template.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalProvidableLocaleList
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import org.jetbrains.compose.resources.ComposeEnvironment
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.LocalComposeEnvironment
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.ThemeQualifier
import org.jetbrains.compose.resources.getSystemResourceEnvironment

@Composable
fun ProvideAppLocale(languageCode: String, isDarkTheme: Boolean, content: @Composable () -> Unit) {
    println("ProvideAppLocale: languageCode = '$languageCode', isDarkTheme = $isDarkTheme")
    // 1. Force the Compose Resources environment to use the given language and theme
    val systemEnv = getSystemResourceEnvironment()
    val customEnv = remember(languageCode, isDarkTheme, systemEnv) {
        ResourceEnvironment(
            language = if (languageCode.isEmpty()) systemEnv.language else LanguageQualifier(languageCode),
            region = systemEnv.region,
            theme = if (isDarkTheme) ThemeQualifier.DARK else ThemeQualifier.LIGHT,
            density = systemEnv.density,
        )
    }

    val customComposeEnvironment = remember(customEnv) {
        object : ComposeEnvironment {
            @Composable
            override fun rememberEnvironment(): ResourceEnvironment {
                println(
                    "ProvideAppLocale: ComposeEnvironment.rememberEnvironment returning " +
                        customEnv.language.language,
                )
                return customEnv
            }
        }
    }

    // 2. Update the standard Compose LocaleList for other components
    val localeList = remember(languageCode) {
        if (languageCode.isEmpty()) LocaleList.current else LocaleList(Locale(languageCode))
    }

    CompositionLocalProvider(
        LocalComposeEnvironment provides customComposeEnvironment,
        LocalProvidableLocaleList provides localeList,
    ) {
        content()
    }
}
