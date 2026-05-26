# Testing Strategy in KmpTemplate

This project uses a unified testing strategy to ensure that both business logic and UI components work correctly across all supported platforms (Android, iOS, Desktop, and Web).

## 1. Shared Logic Testing (Unit Tests)

All shared logic (ViewModels, Managers, Repositories) should be tested in the `commonTest` source set.

- **Location**: `common/src/commonTest/kotlin/...`
- **Verification**: These tests use `kotlin.test` and `kotlinx-coroutines-test` to verify logic cross-platform.
- **Example**: `LanguageManagerTest.kt`

## 2. Compose Multiplatform UI Testing

We use the standard Compose Multiplatform test APIs to verify UI components. To support Android local execution (no emulator), we use **Robolectric**.

### BaseComposeTest
To handle platform-specific runners, all UI tests should inherit from `BaseComposeTest`.

- **Android**: Uses `RobolectricTestRunner` (SDK 34).
- **Other Platforms**: Uses standard Kotlin/Compose test execution.

### Writing a UI Test
```kotlin
class MyComponentTest : BaseComposeTest() {
    @Test
    fun testMyComponent() = runComposeUiTest {
        setContent {
            MyComponent()
        }
        onNodeWithText("Submit").performClick()
        // Verify assertions...
    }
}
```

## 3. Running Tests

You can run tests for specific platforms or all at once.

| Platform | Command |
| :--- | :--- |
| **All Platforms** | `./gradlew :common:allTests` |
| **Android (Local)** | `./gradlew :common:testDebugUnitTest` |
| **Desktop (JVM)** | `./gradlew :common:jvmTest` |
| **iOS Simulator** | `./gradlew :common:iosSimulatorArm64Test` |
| **Web (Wasm)** | `./gradlew :common:wasmJsBrowserTest` |

## 4. Key Configurations

- **Robolectric**: Configured in `common/build.gradle.kts` to use SDK 34 and a local `AndroidManifest.xml` for UI test hosting.
- **Debug Manifest**: Found at `common/src/main/AndroidManifest.xml` (used for test activity resolution).
