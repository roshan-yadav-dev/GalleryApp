package com.gallery.app.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gallery.app.core.database.entity.TrashEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class TrashDao_Impl implements TrashDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TrashEntity> __insertionAdapterOfTrashEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTrashById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTrashByUri;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExpiredTrash;

  private final SharedSQLiteStatement __preparedStmtOfClearTrash;

  public TrashDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrashEntity = new EntityInsertionAdapter<TrashEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trash` (`id`,`mediaUri`,`originalPath`,`displayName`,`sizeBytes`,`mimeType`,`trashedTimestamp`,`expiryTimestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrashEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getMediaUri());
        statement.bindString(3, entity.getOriginalPath());
        statement.bindString(4, entity.getDisplayName());
        statement.bindLong(5, entity.getSizeBytes());
        statement.bindString(6, entity.getMimeType());
        statement.bindLong(7, entity.getTrashedTimestamp());
        statement.bindLong(8, entity.getExpiryTimestamp());
      }
    };
    this.__preparedStmtOfDeleteTrashById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trash WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTrashByUri = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trash WHERE mediaUri = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteExpiredTrash = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trash WHERE expiryTimestamp <= ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearTrash = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trash";
        return _query;
      }
    };
  }

  @Override
  public Object insertTrash(final TrashEntity item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTrashEntity.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTrashById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTrashById.acquire();
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
          __preparedStmtOfDeleteTrashById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTrashByUri(final String uri, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTrashByUri.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, uri);
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
          __preparedStmtOfDeleteTrashByUri.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExpiredTrash(final long currentTime,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExpiredTrash.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, currentTime);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteExpiredTrash.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearTrash(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearTrash.acquire();
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
          __preparedStmtOfClearTrash.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TrashEntity>> getAllTrashItems() {
    final String _sql = "SELECT * FROM trash ORDER BY trashedTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trash"}, new Callable<List<TrashEntity>>() {
      @Override
      @NonNull
      public List<TrashEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaUri = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaUri");
          final int _cursorIndexOfOriginalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "originalPath");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfTrashedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "trashedTimestamp");
          final int _cursorIndexOfExpiryTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryTimestamp");
          final List<TrashEntity> _result = new ArrayList<TrashEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrashEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMediaUri;
            _tmpMediaUri = _cursor.getString(_cursorIndexOfMediaUri);
            final String _tmpOriginalPath;
            _tmpOriginalPath = _cursor.getString(_cursorIndexOfOriginalPath);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final long _tmpTrashedTimestamp;
            _tmpTrashedTimestamp = _cursor.getLong(_cursorIndexOfTrashedTimestamp);
            final long _tmpExpiryTimestamp;
            _tmpExpiryTimestamp = _cursor.getLong(_cursorIndexOfExpiryTimestamp);
            _item = new TrashEntity(_tmpId,_tmpMediaUri,_tmpOriginalPath,_tmpDisplayName,_tmpSizeBytes,_tmpMimeType,_tmpTrashedTimestamp,_tmpExpiryTimestamp);
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

  @Override
  public Object getExpiredTrash(final long currentTime,
      final Continuation<? super List<TrashEntity>> $completion) {
    final String _sql = "SELECT * FROM trash WHERE expiryTimestamp <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TrashEntity>>() {
      @Override
      @NonNull
      public List<TrashEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaUri = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaUri");
          final int _cursorIndexOfOriginalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "originalPath");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfTrashedTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "trashedTimestamp");
          final int _cursorIndexOfExpiryTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryTimestamp");
          final List<TrashEntity> _result = new ArrayList<TrashEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrashEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMediaUri;
            _tmpMediaUri = _cursor.getString(_cursorIndexOfMediaUri);
            final String _tmpOriginalPath;
            _tmpOriginalPath = _cursor.getString(_cursorIndexOfOriginalPath);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final long _tmpTrashedTimestamp;
            _tmpTrashedTimestamp = _cursor.getLong(_cursorIndexOfTrashedTimestamp);
            final long _tmpExpiryTimestamp;
            _tmpExpiryTimestamp = _cursor.getLong(_cursorIndexOfExpiryTimestamp);
            _item = new TrashEntity(_tmpId,_tmpMediaUri,_tmpOriginalPath,_tmpDisplayName,_tmpSizeBytes,_tmpMimeType,_tmpTrashedTimestamp,_tmpExpiryTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
