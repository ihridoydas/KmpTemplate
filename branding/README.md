# Project Branding (App Icons)

Use this directory to store your app icons for different environments.
The script `./gradlew applyAppIcons -Penv={debug|stg|prod}` will automatically distribute these icons to the correct platform folders.

## Folder Structure

### 1. `android/`
Put your full resource structure here.
- For **Adaptive Icons**, include:
  - `mipmap-anydpi-v26/ic_launcher.xml`
  - `mipmap-anydpi-v26/ic_launcher_round.xml`
  - `drawable/ic_launcher_background.xml` (or .png)
  - `drawable/ic_launcher_foreground.xml` (or .png)
- For legacy icons, include the various `mipmap-hdpi`, `mipmap-xhdpi`, etc., folders.

**Target Mapping:**
- `debug` copies to `app/src/debug/res/`
- `stg` copies to `app/src/staging/res/`
- `prod` copies to `app/src/main/res/`

### 2. `ios/`
Put the contents of your `AppIcon.appiconset` here (all sizes + `Contents.json`).
- Automatically copies to `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset`.

### 3. `desktop/`
Put your launcher icons here.
- File names: `icon.png`, `icon.ico` (Windows), `icon.icns` (Mac).
- Automatically copies to `common/src/jvmMain/resources/`.

### 4. `web/`
Put your web icons here.
- File names: `favicon.ico`, `icon.png`.
- Automatically copies to `common/src/wasmJsMain/resources/`.

---

## How to run
To apply **Debug** icons:
```bash
./gradlew applyAppIcons -Penv=debug
```

To apply **Production** icons:
```bash
./gradlew applyAppIcons -Penv=prod
```
