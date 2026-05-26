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
package template.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import template.storage.local.language.Language
import template.storage.local.language.LanguageDataStore

object LanguageManager {
    // Start with UNKNOWN to ensure the first real value (even SYSTEM) is seen as a change
    private val _currentLanguage = MutableStateFlow(Language.UNKNOWN)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private var dataStore: LanguageDataStore? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var isInitialized = false

    fun init(languageDataStore: LanguageDataStore) {
        if (isInitialized) return
        isInitialized = true
        dataStore = languageDataStore

        scope.launch {
            languageDataStore.getLanguage.collect { savedLanguage ->
                println("LanguageManager: DataStore emitted -> $savedLanguage")

                // If the DataStore has UNKNOWN (new install), we default to SYSTEM
                val finalLanguage = if (savedLanguage == Language.UNKNOWN) Language.SYSTEM else savedLanguage

                // Only sync if we haven't manually set something else yet
                if (_currentLanguage.value == Language.UNKNOWN) {
                    println("LanguageManager: Initial sync -> $finalLanguage")
                    _currentLanguage.value = finalLanguage
                }
            }
        }
    }

    /**
     * Resets the manager state for testing purposes.
     */
    fun resetForTesting() {
        isInitialized = false
        dataStore = null
        _currentLanguage.value = Language.UNKNOWN
    }

    fun setLanguage(language: Language) {
        println("LanguageManager: setLanguage -> $language")
        _currentLanguage.value = language

        scope.launch {
            println("LanguageManager: Persistence -> Saving $language")
            dataStore?.setLanguage(language)
        }
    }
}
