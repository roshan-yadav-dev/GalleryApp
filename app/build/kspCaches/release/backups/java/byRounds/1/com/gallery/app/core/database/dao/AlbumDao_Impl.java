package com.gallery.app.core.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gallery.app.core.database.entity.CustomAlbumEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AlbumDao_Impl implements AlbumDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CustomAlbumEntity> __insertionAdapterOfCustomAlbumEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCustomAlbum;

  public AlbumDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCustomAlbumEntity = new EntityInsertionAdapter<CustomAlbumEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `custom_albums` (`id`,`name`,`coverUri`,`createdDate`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomAlbumEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getCoverUri() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCoverUri());
        }
        statement.bindLong(4, entity.getCreatedDate());
      }
    };
    this.__preparedStmtOfDeleteCustomAlbum = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM custom_albums WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCustomAlbum(final CustomAlbumEntity album,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCustomAlbumEntity.insertAndReturnId(album);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCustomAlbum(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCustomAlbum.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCustomAlbum.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CustomAlbumEntity>> getAllCustomAlbums() {
    final String _sql = "SELECT * FROM custom_albums ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"custom_albums"}, new Callable<List<CustomAlbumEntity>>() {
      @Override
      @NonNull
      public List<CustomAlbumEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final List<CustomAlbumEntity> _result = new ArrayList<CustomAlbumEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomAlbumEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            _item = new CustomAlbumEntity(_tmpId,_tmpName,_tmpCoverUri,_tmpCreatedDate);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
