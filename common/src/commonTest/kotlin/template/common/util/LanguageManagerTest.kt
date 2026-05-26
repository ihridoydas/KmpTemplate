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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import template.storage.local.language.Language
import template.storage.local.language.LanguageDataStore
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LanguageManagerTest {

    private val testDispatcher = StandardTestDispatcher()

    private class MockLanguageDataStore(initialLanguage: Language) : LanguageDataStore {
        private val _languageFlow = MutableStateFlow(initialLanguage)
        override val getLanguage: Flow<Language> = _languageFlow.asStateFlow()
        var savedLanguage: Language? = null
        override suspend fun setLanguage(language: Language) {
            savedLanguage = language
            _languageFlow.value = language
        }

        fun emitLanguage(language: Language) {
            _languageFlow.value = language
        }
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        LanguageManager.resetForTesting()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialSyncWithSystemDefault() = runTest(testDispatcher) {
        val mockDs = MockLanguageDataStore(Language.UNKNOWN)
        LanguageManager.init(mockDs)
        
        advanceUntilIdle()
        
        assertEquals(Language.SYSTEM, LanguageManager.currentLanguage.value)
    }

    @Test
    fun testInitialSyncWithSavedLanguage() = runTest(testDispatcher) {
        val mockDs = MockLanguageDataStore(Language.JAPANESE)
        LanguageManager.init(mockDs)
        
        advanceUntilIdle()
        
        assertEquals(Language.JAPANESE, LanguageManager.currentLanguage.value)
    }

    @Test
    fun testSetLanguagePersists() = runTest(testDispatcher) {
        val mockDs = MockLanguageDataStore(Language.ENGLISH)
        LanguageManager.init(mockDs)
        advanceUntilIdle()

        LanguageManager.setLanguage(Language.BENGALI)
        advanceUntilIdle()

        assertEquals(Language.BENGALI, LanguageManager.currentLanguage.value)
        assertEquals(Language.BENGALI, mockDs.savedLanguage)
    }

    @Test
    fun testDoubleInitialization() = runTest(testDispatcher) {
        val mockDs1 = MockLanguageDataStore(Language.ENGLISH)
        val mockDs2 = MockLanguageDataStore(Language.JAPANESE)

        LanguageManager.init(mockDs1)
        advanceUntilIdle()
        assertEquals(Language.ENGLISH, LanguageManager.currentLanguage.value)

        LanguageManager.init(mockDs2)
        advanceUntilIdle()
        // Should still be ENGLISH because init is idempotent
        assertEquals(Language.ENGLISH, LanguageManager.currentLanguage.value)
    }

    @Test
    fun testManualSetBeforeInitialSync() = runTest(testDispatcher) {
        val mockDs = MockLanguageDataStore(Language.ENGLISH)

        LanguageManager.setLanguage(Language.BENGALI)
        LanguageManager.init(mockDs)
        advanceUntilIdle()

        // Manual set should take precedence
        assertEquals(Language.BENGALI, LanguageManager.currentLanguage.value)
    }

    @Test
    fun testDataStoreUpdateAfterInitialSyncIsIgnored() = runTest(testDispatcher) {
        val mockDs = MockLanguageDataStore(Language.ENGLISH)
        LanguageManager.init(mockDs)
        advanceUntilIdle()
        assertEquals(Language.ENGLISH, LanguageManager.currentLanguage.value)

        // Based on implementation, once it's not UNKNOWN, it ignores DataStore updates
        mockDs.emitLanguage(Language.JAPANESE)
        advanceUntilIdle()

        assertEquals(Language.ENGLISH, LanguageManager.currentLanguage.value)
    }

    @Test
    fun testSetLanguageWithoutInit() = runTest(testDispatcher) {
        // We don't call LanguageManager.init()
        
        LanguageManager.setLanguage(Language.BENGALI)
        
        assertEquals(Language.BENGALI, LanguageManager.currentLanguage.value)
        // No crash should occur, even though dataStore is null
    }
}
