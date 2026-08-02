# Production-Grade Android Gallery Application Architecture

## 1. System Architecture Overview

The application follows **Strict Clean Architecture** combined with **MVVM (Model-View-ViewModel)** and **Jetpack Compose**. The codebase is decoupled into distinct architectural layers:

```mermaid
graph TD
    subgraph UI Layer
        ComposeScreens["Jetpack Compose Screens"]
        ViewModels["Hilt ViewModels"]
    end

    subgraph Domain Layer
        UseCases["Use Cases (GetMediaItems, MoveToTrash, etc.)"]
        Models["Domain Models (MediaItem, Album, TrashItem)"]
        RepoInterfaces["Repository Interfaces"]
    end

    subgraph Data Layer
        RepoImpls["Repository Implementations"]
        MediaStoreDS["MediaStore Data Source"]
        RoomDS["Room Local Database (Favorites, Trash)"]
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

## 2. UML Class Diagram

```mermaid
classDiagram
    class MediaItem {
        +Long id
        +Uri uri
        +String path
        +String displayName
        +Long size
        +String mimeType
        +Long dateAdded
        +Long dateModified
        +Boolean isFavorite
        +Boolean isVideo
        +Boolean isGif
    }

    class MediaRepository {
        <<interface>>
        +getMediaItems(FilterOptions) Flow~List~MediaItem~~
        +getMediaMetadata(MediaItem) MediaMetadata
        +deleteMedia(MediaItem) Boolean
    }

    class MediaRepositoryImpl {
        -MediaStoreDataSource mediaStoreDS
        -FavoriteDao favoriteDao
        -TrashDao trashDao
        +getMediaItems(FilterOptions) Flow~List~MediaItem~~
    }

    class GetMediaItemsUseCase {
        -MediaRepository mediaRepository
        +invoke(FilterOptions) Flow~List~MediaItem~~
    }

    class GalleryViewModel {
        -GetMediaItemsUseCase getMediaItemsUseCase
        +StateFlow~GalleryUiState~ uiState
        +toggleItemSelection(Long)
        +deleteSelectedItems()
    }

    MediaRepositoryImpl ..|> MediaRepository
    GetMediaItemsUseCase --> MediaRepository
    GalleryViewModel --> GetMediaItemsUseCase
    MediaRepositoryImpl --> MediaItem
```

---

## 3. Sequence Diagram: Media Query & Flow Stream

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant GalleryScreen
    participant GalleryViewModel
    participant GetMediaItemsUseCase
    participant MediaRepositoryImpl
    participant MediaStoreObserver
    participant RoomDao

    User->>GalleryScreen: Open Gallery Screen
    GalleryScreen->>GalleryViewModel: Observe uiState Flow
    GalleryViewModel->>GetMediaItemsUseCase: invoke(FilterOptions)
    GetMediaItemsUseCase->>MediaRepositoryImpl: getMediaItems(FilterOptions)
    MediaRepositoryImpl->>MediaStoreObserver: observeMediaChanges()
    MediaRepositoryImpl->>RoomDao: getAllFavoriteUris()
    MediaRepositoryImpl-->>GalleryViewModel: Flow<List<MediaItem>>
    GalleryViewModel-->>GalleryScreen: Update GalleryUiState
    GalleryScreen-->>User: Render Adaptive Media Grid
```

---

## 4. Sequence Diagram: Soft Delete & Trash Lifecycle

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant MediaViewerScreen
    participant MediaViewerViewModel
    participant MoveToTrashUseCase
    participant TrashRepositoryImpl
    participant TrashDao
    participant TrashCleanupWorker

    User->>MediaViewerScreen: Tap Delete Button
    MediaViewerScreen->>MediaViewerViewModel: deleteCurrentMedia()
    MediaViewerViewModel->>MoveToTrashUseCase: invoke(MediaItem)
    MoveToTrashUseCase->>TrashRepositoryImpl: moveToTrash(MediaItem)
    TrashRepositoryImpl->>TrashDao: insertTrash(TrashEntity)
    TrashDao-->>User: Item Moved to Trash (Excluded from Gallery Flow)

    Note over TrashCleanupWorker: Runs daily via WorkManager
    TrashCleanupWorker->>TrashRepositoryImpl: cleanupExpiredTrash()
    TrashRepositoryImpl->>TrashDao: deleteExpiredTrash(currentTime > 30 days)
```

---

## 5. Architectural Principles & Best Practices
- **SOLID Principles**: Single Responsibility per UseCase, Interface Segregation for Repositories, Dependency Inversion with Hilt.
- **Unidirectional Data Flow (UDF)**: ViewModels expose immutable `StateFlow<UiState>` to Compose UI.
- **Asynchronous Coroutines & Flow**: Reactive data pipelines for MediaStore queries, Room updates, and DataStore preferences.
- **Scoped Storage & SAF**: Strict compliance with Android 13/14 visual media pickers and Storage Access Framework.
