package template

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import template.common.screens.HomeScreen
import template.navigation.Navigator
import template.navigation.ScreenDestinations
import template.storage.local.theme.ThemeLocalDataStore
import template.storage.local.theme.ThemeMode
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        val mockThemeDataStore = object : ThemeLocalDataStore {
            override val themeMode = flowOf(ThemeMode.LIGHT)
            override suspend fun setThemeMode(mode: ThemeMode) {}
        }

        stopKoin() // Ensure a clean state for the test
        startKoin {
            modules(module {
                single<ThemeLocalDataStore> { mockThemeDataStore }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testHomeScreenButton() {
        // Mock Navigator
        val mockNavigator = object : Navigator {
            var navigatedTo: ScreenDestinations? = null
            override fun navigate(route: ScreenDestinations) {
                navigatedTo = route
            }
            override fun goBack() {}
        }

        composeTestRule.setContent {
            KoinContext {
                HomeScreen(navigator = mockNavigator)
            }
        }

        // Verify button exists and perform click
        composeTestRule.onNodeWithText("Lets Start!").performClick()

        // Verify navigation occurred
        assert(mockNavigator.navigatedTo == ScreenDestinations.ViewScreen)
    }
}
