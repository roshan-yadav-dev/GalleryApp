# Google Play Store Release Readiness Checklist

## 1. App Configuration & Quality
- [x] **Target SDK**: Configured for API 35 (Android 15 compatible).
- [x] **Min SDK**: Set to API 26 (Android 8.0+ support).
- [x] **Version Code & Name**: `versionCode = 1`, `versionName = "1.0.0"`.
- [x] **Package Name**: `com.gallery.app`.
- [x] **Edge-to-Edge & Responsive**: Fully responsive Compose UI for Phones, Tablets, and Foldables (Portrait/Landscape).
- [x] **Dark Mode & Dynamic Color**: Material You dynamic color scheme support.

## 2. Permissions & Data Privacy
- [x] **Media Permissions**: Declarations for `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`, `READ_MEDIA_VISUAL_USER_SELECTED` (Android 13+ & 14+), and legacy `READ_EXTERNAL_STORAGE` (Android 8–12).
- [x] **Permission Rationale**: Clear runtime permission rationale dialogs for user consent.
- [x] **Scoped Storage**: Full compliance with Android Scoped Storage and Storage Access Framework.

## 3. Performance & Stability
- [x] **ProGuard / R8 Shrinking**: Minification and resource shrinking enabled in release build.
- [x] **Asynchronous I/O**: Coroutine dispatchers for background MediaStore queries and Room DB transactions.
- [x] **Memory Management**: Coil image caching (memory/disk) and Media3 ExoPlayer resource release lifecycle handling.
- [x] **Background Tasks**: WorkManager periodic worker for automatic 30-day trash cleanup.

## 4. Play Console Deliverables
- [ ] **Release Keystore**: Generate production keystore (`gallery-release.jks`) and configure environment variables.
- [ ] **App Bundle (AAB)**: Run `./gradlew bundleRelease` to generate `.aab` bundle.
- [ ] **Store Listing Assets**:
  - High-res App Icon (512x512 PNG)
  - Feature Graphic (1024x500 PNG)
  - Phone Screenshots (min 2)
  - 7-inch & 10-inch Tablet Screenshots
- [ ] **Privacy Policy URL**: Host privacy policy URL documenting local media access.
