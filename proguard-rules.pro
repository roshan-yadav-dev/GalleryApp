# Keep Room Entities and DAOs
-keep class com.gallery.app.core.database.entity.** { *; }
-keep interface com.gallery.app.core.database.dao.** { *; }

# Keep DataStore serializable preferences
-keep class com.gallery.app.core.datastore.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
}

# Keep Media3 ExoPlayer classes
-keep class androidx.media3.** { *; }

# Keep Coil image loading reflection
-keep class io.coil-kt.** { *; }

# Keep Timber logging
-keep class com.jakewharton.timber.** { *; }
