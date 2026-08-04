# 📱 GalleryApp - Production-Grade Android Gallery

[![Android CI/CD Pipeline](https://github.com/roshan-yadav-dev/GalleryApp/actions/workflows/android.yml/badge.svg)](https.github.com/roshan-yadav-dev/GalleryApp/actions)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%20100%25-7F52FF?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2F%20Material3-4285F4?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![MinSDK](https://img.shields.io/badge/MinSDK-26%20(Android%208.0)-blue)](https://developer.android.com)
[![TargetSDK](https://img.shields.io/badge/TargetSDK-35%20(Android%2015)-green)](https://developer.android.com)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange)](https://developer.android.com/topic/architecture)

**GalleryApp** is a modern, high-performance, production-ready Android media management application built with **Kotlin**, **Jetpack Compose**, **Material 3**, and **Clean Architecture**. It provides a sleek, responsive experience for managing, viewing, editing, searching, and organizing photos, videos, and GIFs with strict compliance to Android Scoped Storage and security standards.

---

## ✨ Features & Capabilities

### 🎨 1. Adaptive Media Grid & Timeline
- **Fluid Layout**: Responsive grid supporting photos, videos, and animated GIFs with crisp cached thumbnails.
- **Batch Operations**: Multi-selection mode allowing users to delete, favorite, move, or share multiple media items simultaneously.
- **Real-Time Synchronization**: Instant UI updates powered by Android `MediaStore` observers.

### 🖼️ 2. Immersive Media Viewer & Playback
- **Interactive Gestures**: Smooth pinch-to-zoom, panning, swipe-to-dismiss navigation.
- **Hold-to-Reveal Filmstrip**: 2-second hold-to-reveal thumbnail filmstrip for rapid media scrub.
- **ExoPlayer Video Engine**: Integrated **Jetpack Media3 ExoPlayer** with lifecycle-aware background auto-pausing and gesture scrub controls.
- **EXIF Metadata Inspector**: View detailed metadata including resolution, file size, MIME type, date taken, and EXIF tags.

### ✂️ 3. Non-Destructive Photo & Video Editor
- **Photo Editing Suite**:
  - Real-time image adjustments: Brightness, Contrast, Saturation.
  - Rotation, flipping, and color matrix filter overlays.
  - Full Undo/Redo stack with lossless export to Android `MediaStore`.
- **Video Editing Suite**:
  - Video trimming and clipping via **Jetpack Media3 Transformer**.
  - Hardware-accelerated video re-encoding and quality export settings.

### 📂 4. Album Organization & Intelligent Search
- **Folder & Album View**: Automatic grouping by directories, camera shots, screenshots, and custom folders.
- **Smart Search**: Instant full-text search across filenames, media types, dates, tags, and album names.

### 🗑️ 5. Trash & Soft Delete Lifecycle
- **Recycle Bin**: Soft-delete mechanism preventing accidental file loss.
- **Automated Cleanup**: **AndroidX WorkManager** scheduled background worker automatically purges items older than 30 days.
- **Full Restoration**: Restore media items back to original storage paths effortlessly.

### ⚙️ 6. Personalization & Theme Support
- **Material Design 3**: Dynamic color adaptation based on wallpaper (Android 12+) and seamless Dark/Light theme switching.
- **Customization Options**: Configurable grid column counts and automated trash retention policies.

---

## 🛠️ Technology Stack & Libraries

### **Core Runtime & Language**
* **[Kotlin](https://kotlinlang.org/)**: 100% idiomatic Kotlin with Coroutines and `StateFlow`/`SharedFlow` for Unidirectional Data Flow (UDF).

### **User Interface & Design**
* **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: Modern declarative UI toolkit.
* **[Material Design 3](https://m3.material.io/)**: Dynamic color system, modern typography, and standard UI primitives.
* **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)**: Type-safe, single-activity screen routing.

### **Architecture & Dependency Injection**
* **Clean Architecture**: Decoupled layers (`UI` → `Domain` → `Data`).
* **MVVM Pattern**: ViewModels managing UI state immutably using `StateFlow`.
* **[Hilt (Dagger Hilt)](https://dagger.dev/hilt/)**: Compile-time dependency injection across ViewModels, Use Cases, Repositories, and Background Workers.

### **Data Storage & Persistence**
* **[Room Database](https://developer.android.com/training/data-storage/room)**: Local SQLite storage for Favorites and Trash metadata.
* **[DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)**: Asynchronous preference storage for user settings.
* **[MediaStore API & SAF](https://developer.android.com/training/data-storage)**: Native integration with Android Storage Access Framework and Scoped Storage permissions (Android 13/14 `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`).

### **Media Processing & Playback**
* **[Coil Compose](https://coil-kt.github.io/coil/)**: Image, GIF (`coil-gif`), and video thumbnail (`coil-video`) fetching and memory caching.
* **[Jetpack Media3 (ExoPlayer & Transformer)](https://developer.android.com/guide/topics/media/media3)**: High-performance video playback and hardware-accelerated video editing/transcoding pipeline.
* **[ExifInterfaces](https://developer.android.com/jetpack/androidx/releases/exifinterface)**: Extraction and modification of EXIF image headers.

### **Background Operations & Logging**
* **[WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)**: Scheduled background tasks for automated trash expiration cleanup.
* **[Timber](https://github.com/JakeWharton/timber)**: Lightweight extensible logging utility.

### **Testing & CI/CD**
* **Unit Testing**: JUnit 4, MockK, Coroutines Test (`kotlinx-coroutines-test`).
* **UI & Integration Testing**: Compose UI Test JUnit 4 framework, Espresso.
* **CI/CD**: GitHub Actions pipeline for automated unit test execution and debug APK packaging (`actions/setup-java@v4` with Temurin JDK 17).

---

## 🏗️ Architecture Overview

The project adheres strictly to **Clean Architecture** principles:

```mermaid
graph TD
    subgraph UI Layer
        ComposeScreens["Jetpack Compose Screens"]
        ViewModels["Hilt ViewModels"]
    end

    subgraph Domain Layer
        UseCases["Use Cases (GetMediaItems, MoveToTrash, etc.)"]
        DomainModels["Domain Models (MediaItem, Album, TrashItem)"]
        RepoInterfaces["Repository Interfaces"]
    end

    subgraph Data Layer
        RepoImpls["Repository Implementations"]
        MediaStoreDS["MediaStore Data Source"]
        RoomDS["Room Database (Favorites, Trash)"]
        DataStoreDS["DataStore Preference Manager"]
        SAFHelper["Storage Access Framework"]
    end

    ComposeScreens --> ViewModels
    ViewModels --> UseCases
    UseCases --> RepoInterfaces
    RepoImpls ..|> RepoInterfaces
    RepoImpls --> MediaStoreDS
    RepoImpls --> RoomDS
    RepoImpls --> DataStoreDS
    RepoImpls --> SAFHelper
```

---

## 📂 Project Package Structure

```
com.gallery.app/
├── core/
│   ├── common/        # Dispatchers, Result wrappers, Extensions
│   ├── data/          # Repository implementations & Data sources
│   ├── database/      # Room database entities, DAOs, and migrations
│   ├── datastore/     # Preference Datastore manager
│   ├── domain/        # Domain models, Repository interfaces, Use Cases
│   ├── editor/        # Photo and Video editing engine implementations
│   ├── permissions/   # Granular Scoped Storage permission handlers
│   ├── storage/       # SAF (Storage Access Framework) helper
│   ├── theme/         # Material 3 Color palettes, Typography, Shapes
│   ├── widgets/       # Shared UI components (TopBar, GridItems, Dialogs)
│   └── work/          # WorkManager background workers
├── di/                # Hilt Dependency Injection modules
├── feature/           # Screen ViewModels & Compose UI features
│   ├── albums/        # Album collection and detail views
│   ├── editor/        # Photo and Video editor screens
│   ├── favorites/     # Bookmarked media gallery
│   ├── gallery/       # Main timeline media grid
│   ├── search/        # Real-time search feature
│   ├── settings/      # App preference configuration
│   ├── trash/         # Soft-delete bin and restoration
│   └── viewer/        # Fullscreen media viewer & ExoPlayer
└── navigation/        # Jetpack Compose Navigation Graph & Routes
```

---

## 🚀 Building & Running locally

### Prerequisites
* **Android Studio**: Ladybug / Quail (2024.2+) or newer.
* **JDK**: JDK 17 or Java 21 configured.
* **Android SDK**: API Level 35 (Android 15) installed with Build Tools.
* **Minimum Device SDK**: Android 8.0 (API Level 26).

### Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/roshan-yadav-dev/GalleryApp.git
   cd GalleryApp
   ```

2. **Run Unit Tests**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

3. **Build Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```
   The generated APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).