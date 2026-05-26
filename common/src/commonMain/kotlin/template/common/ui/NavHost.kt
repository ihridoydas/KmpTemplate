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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import template.common.screens.HomeScreen
import template.common.screens.ViewScreen
import template.navigation.Navigator
import template.navigation.ScreenDestinations
import template.theme.components.SpatialWrapper

@Composable
fun MainAnimationNavHost() {
    val backStack = remember { mutableStateListOf<ScreenDestinations>(ScreenDestinations.HomeScreen) }

    SpatialWrapper {
        Box(modifier = Modifier.fillMaxSize()) {
            NavDisplay(
                backStack = backStack,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeAt(backStack.size - 1)
                    }
                },
            ) { key ->
                when (key) {
                    ScreenDestinations.HomeScreen -> NavEntry(key) {
                        HomeScreen(
                            navigator = object : Navigator {
                                override fun navigate(route: ScreenDestinations) {
                                    backStack.add(route)
                                }

                                override fun goBack() {
                                    if (backStack.size > 1) {
                                        backStack.removeAt(backStack.size - 1)
                                    }
                                }
                            },
                        )
                    }

                    ScreenDestinations.ViewScreen -> NavEntry(key) {
                        ViewScreen(
                            onBackPress = {
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.size - 1)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
