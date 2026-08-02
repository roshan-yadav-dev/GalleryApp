package com.gallery.app.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.gallery.app.core.database.dao.AlbumDao;
import com.gallery.app.core.database.dao.AlbumDao_Impl;
import com.gallery.app.core.database.dao.FavoriteDao;
import com.gallery.app.core.database.dao.FavoriteDao_Impl;
import com.gallery.app.core.database.dao.TrashDao;
import com.gallery.app.core.database.dao.TrashDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GalleryDatabase_Impl extends GalleryDatabase {
  private volatile FavoriteDao _favoriteDao;

  private volatile TrashDao _trashDao;

  private volatile AlbumDao _albumDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`mediaUri` TEXT NOT NULL, `mediaId` INTEGER NOT NULL, `addedDate` INTEGER NOT NULL, PRIMARY KEY(`mediaUri`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `trash` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mediaUri` TEXT NOT NULL, `originalPath` TEXT NOT NULL, `displayName` TEXT NOT NULL, `sizeBytes` INTEGER NOT NULL, `mimeType` TEXT NOT NULL, `trashedTimestamp` INTEGER NOT NULL, `expiryTimestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `custom_albums` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `coverUri` TEXT, `createdDate` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0ff1dab018d10104d50f4bd0e4369ba1')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `favorites`");
        db.execSQL("DROP TABLE IF EXISTS `trash`");
        db.execSQL("DROP TABLE IF EXISTS `custom_albums`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFavorites = new HashMap<String, TableInfo.Column>(3);
        _columnsFavorites.put("mediaUri", new TableInfo.Column("mediaUri", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("mediaId", new TableInfo.Column("mediaId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("addedDate", new TableInfo.Column("addedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavorites = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavorites = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavorites = new TableInfo("favorites", _columnsFavorites, _foreignKeysFavorites, _indicesFavorites);
        final TableInfo _existingFavorites = TableInfo.read(db, "favorites");
        if (!_infoFavorites.equals(_existingFavorites)) {
          return new RoomOpenHelper.ValidationResult(false, "favorites(com.gallery.app.core.database.entity.FavoriteEntity).\n"
                  + " Expected:\n" + _infoFavorites + "\n"
                  + " Found:\n" + _existingFavorites);
        }
        final HashMap<String, TableInfo.Column> _columnsTrash = new HashMap<String, TableInfo.Column>(8);
        _columnsTrash.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrash.put("mediaUri", new TableInfo.Column("mediaUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrash.put("originalPath", new TableInfo.Column("originalPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrash.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrash.put("sizeBytes", new TableInfo.Column("sizeBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrash.put("mimeType", new TableInfo.Column("mimeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrash.put("trashedTimestamp", new TableInfo.Column("trashedTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrash.put("expiryTimestamp", new TableInfo.Column("expiryTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrash = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrash = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrash = new TableInfo("trash", _columnsTrash, _foreignKeysTrash, _indicesTrash);
        final TableInfo _existingTrash = TableInfo.read(db, "trash");
        if (!_infoTrash.equals(_existingTrash)) {
          return new RoomOpenHelper.ValidationResult(false, "trash(com.gallery.app.core.database.entity.TrashEntity).\n"
                  + " Expected:\n" + _infoTrash + "\n"
                  + " Found:\n" + _existingTrash);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomAlbums = new HashMap<String, TableInfo.Column>(4);
        _columnsCustomAlbums.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomAlbums.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomAlbums.put("coverUri", new TableInfo.Column("coverUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomAlbums.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomAlbums = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomAlbums = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCustomAlbums = new TableInfo("custom_albums", _columnsCustomAlbums, _foreignKeysCustomAlbums, _indicesCustomAlbums);
        final TableInfo _existingCustomAlbums = TableInfo.read(db, "custom_albums");
        if (!_infoCustomAlbums.equals(_existingCustomAlbums)) {
          return new RoomOpenHelper.ValidationResult(false, "custom_albums(com.gallery.app.core.database.entity.CustomAlbumEntity).\n"
                  + " Expected:\n" + _infoCustomAlbums + "\n"
                  + " Found:\n" + _existingCustomAlbums);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0ff1dab018d10104d50f4bd0e4369ba1", "46047fc4798df8536fdbdbbf7eaa08af");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorites","trash","custom_albums");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `favorites`");
      _db.execSQL("DELETE FROM `trash`");
      _db.execSQL("DELETE FROM `custom_albums`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FavoriteDao.class, FavoriteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrashDao.class, TrashDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AlbumDao.class, AlbumDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FavoriteDao favoriteDao() {
    if (_favoriteDao != null) {
      return _favoriteDao;
    } else {
      synchronized(this) {
        if(_favoriteDao == null) {
          _favoriteDao = new FavoriteDao_Impl(this);
        }
        return _favoriteDao;
      }
    }
  }

  @Override
  public TrashDao trashDao() {
    if (_trashDao != null) {
      return _trashDao;
    } else {
      synchronized(this) {
        if(_trashDao == null) {
          _trashDao = new TrashDao_Impl(this);
        }
        return _trashDao;
      }
    }
  }

  @Override
  public AlbumDao albumDao() {
    if (_albumDao != null) {
      return _albumDao;
    } else {
      synchronized(this) {
        if(_albumDao == null) {
          _albumDao = new AlbumDao_Impl(this);
        }
        return _albumDao;
      }
    }
  }
}
