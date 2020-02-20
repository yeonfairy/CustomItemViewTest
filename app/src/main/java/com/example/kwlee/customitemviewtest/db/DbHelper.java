//package com.example.kwlee.customitemviewtest.db;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.net.Uri;
//
//import com.example.kwlee.customitemviewtest.utils.ConstantsBase;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.security.Security;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//
//import com.example.kwlee.customitemviewtest.OmniNotes;
//import com.example.kwlee.customitemviewtest.UpgradeProcessor;
//import com.example.kwlee.customitemviewtest.exceptions.DatabaseException;
//import com.example.kwlee.customitemviewtest.models.Attachment;
//import com.example.kwlee.customitemviewtest.models.Note;
//
//import static com.example.kwlee.customitemviewtest.utils.ConstantsBase.MIME_TYPE_FILES;
//import static com.example.kwlee.customitemviewtest.utils.ConstantsBase.MIME_TYPE_IMAGE;
//import static com.example.kwlee.customitemviewtest.utils.ConstantsBase.PREF_PASSWORD;
//
//
//public class DbHelper extends SQLiteOpenHelper {
//
//  // Database name
//  private static final int DATABASE_VERSION = 560;
//  // Sql query file directory
//  private static final String SQL_DIR = "sql";
//
//  // Notes table name
//  public static final String TABLE_NOTES = "notes";
//  // Notes table columns
//  public static final String KEY_ID = "creation";
//  public static final String KEY_CREATION = "creation";
//  public static final String KEY_LAST_MODIFICATION = "last_modification";
//  public static final String KEY_TITLE = "title";
//  public static final String KEY_CONTENT = "content";
//  public static final String KEY_TRASHED = "trashed";
//
//  // Attachments table name
//  public static final String TABLE_ATTACHMENTS = "attachments";
//  // Attachments table columns
//  public static final String KEY_ATTACHMENT_ID = "attachment_id";
//  public static final String KEY_ATTACHMENT_URI = "uri";
//  public static final String KEY_ATTACHMENT_NAME = "name";
//  public static final String KEY_ATTACHMENT_SIZE = "size";
//  public static final String KEY_ATTACHMENT_LENGTH = "length";
//  public static final String KEY_ATTACHMENT_MIME_TYPE = "mime_type";
//  public static final String KEY_ATTACHMENT_NOTE_ID = "note_id";
//
//
//  // Queries
//  private static final String CREATE_QUERY = "create.sql";
//  private static final String UPGRADE_QUERY_PREFIX = "upgrade-";
//  private static final String UPGRADE_QUERY_SUFFIX = ".sql";
//
//
//  private final Context mContext;
//  private final SharedPreferences prefs;
//
//  private static DbHelper instance = null;
//  private SQLiteDatabase db;
//
//
//  public static synchronized DbHelper getInstance () {
//    return getInstance(OmniNotes.getAppContext());
//  }
//
//
//  public static synchronized DbHelper getInstance (Context context) {
//    if (instance == null) {
//      instance = new DbHelper(context);
//    }
//    return instance;
//  }
//
//
//  public static synchronized DbHelper getInstance (boolean forcedNewInstance) {
//    if (instance == null || forcedNewInstance) {
//      Context context = (instance == null || instance.mContext == null) ? OmniNotes.getAppContext() : instance.mContext;
//      instance = new DbHelper(context);
//    }
//    return instance;
//  }
//
//
//  private DbHelper(Context mContext) {
//    super(mContext, ConstantsBase.DATABASE_NAME, null, DATABASE_VERSION);
//    this.mContext = mContext;
//    this.prefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_MULTI_PROCESS);
//  }
//
//
//  public String getDatabaseName () {
//    return ConstantsBase.DATABASE_NAME;
//  }
//
//  public SQLiteDatabase getDatabase () {
//    return getDatabase(false);
//  }
//
//  public SQLiteDatabase getDatabase (boolean forceWritable) {
//    try {
//      return forceWritable ? getWritableDatabase() : getReadableDatabase();
//    } catch (IllegalStateException e) {
//      return this.db;
//    }
//  }
//
//  @Override
//  public void onOpen (SQLiteDatabase db) {
//    db.disableWriteAheadLogging();
//    super.onOpen(db);
//  }
//
//  @Override
//  public void onCreate (SQLiteDatabase db) {
//    try {
//      LogDelegate.i("Database creation");
//      execSqlFile(CREATE_QUERY, db);
//    } catch (IOException e) {
//      throw new DatabaseException("Database creation failed: " + e.getMessage(), e);
//    }
//  }
//
//
//  @Override
//  public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
//    this.db = db;
//    LogDelegate.i("Upgrading database version from " + oldVersion + " to " + newVersion);
//
//    try {
//
//      UpgradeProcessor.process(oldVersion, newVersion);
//
//      for (String sqlFile : AssetUtils.list(SQL_DIR, mContext.getAssets())) {
//        if (sqlFile.startsWith(UPGRADE_QUERY_PREFIX)) {
//          int fileVersion = Integer.parseInt(sqlFile.substring(UPGRADE_QUERY_PREFIX.length(),
//              sqlFile.length() - UPGRADE_QUERY_SUFFIX.length()));
//          if (fileVersion > oldVersion && fileVersion <= newVersion) {
//            execSqlFile(sqlFile, db);
//          }
//        }
//      }
//      LogDelegate.i("Database upgrade successful");
//
//    } catch (IOException | InvocationTargetException | IllegalAccessException e) {
//      throw new RuntimeException("Database upgrade failed", e);
//    }
//  }
//
//
//  public Note updateNote (Note note, boolean updateLastModification) {
//    db = getDatabase(true);
//
//    String content = Boolean.TRUE.equals(note.isLocked())
//        ? Security.encrypt(note.getContent(), prefs.getString(PREF_PASSWORD, ""))
//        : note.getContent();
//
//    // To ensure note and attachments insertions are atomic and boost performances transaction are used
//    db.beginTransaction();
//
//    ContentValues values = new ContentValues();
//    values.put(KEY_TITLE, note.getTitle());
//    values.put(KEY_CONTENT, content);
//    values.put(KEY_CREATION,
//        note.getCreation() != null ? note.getCreation() : Calendar.getInstance().getTimeInMillis());
//    long lastModification = note.getLastModification() != null && !updateLastModification
//        ? note.getLastModification()
//        : Calendar.getInstance().getTimeInMillis();
//    values.put(KEY_LAST_MODIFICATION, lastModification);
//    values.put(KEY_TRASHED, note.isTrashed());
//
//
//    db.insertWithOnConflict(TABLE_NOTES, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
//    LogDelegate.d("Updated note titled '" + note.getTitle() + "'");
//
//    // Updating attachments
//    List<Attachment> deletedAttachments = note.getAttachmentsListOld();
//    for (Attachment attachment : note.getAttachmentsList()) {
//      updateAttachment(note.get_id() != null ? note.get_id() : values.getAsLong(KEY_CREATION), attachment, db);
//      deletedAttachments.remove(attachment);
//    }
//    // Remove from database deleted attachments
//    for (Attachment attachmentDeleted : deletedAttachments) {
//      db.delete(TABLE_ATTACHMENTS, KEY_ATTACHMENT_ID + " = ?",
//          new String[]{String.valueOf(attachmentDeleted.getId())});
//    }
//
//    db.setTransactionSuccessful();
//    db.endTransaction();
//
//    // Fill the note with correct data before returning it
//    note.setCreation(note.getCreation() != null ? note.getCreation() : values.getAsLong(KEY_CREATION));
//    note.setLastModification(values.getAsLong(KEY_LAST_MODIFICATION));
//
//    return note;
//  }
//
//
//  private void execSqlFile (String sqlFile, SQLiteDatabase db) throws SQLException, IOException {
//    LogDelegate.i("  exec sql file: {}" + sqlFile);
//    for (String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR + "/" + sqlFile, mContext.getAssets())) {
//      LogDelegate.v("    sql: {}" + sqlInstruction);
//      try {
//        db.execSQL(sqlInstruction);
//      } catch (Exception e) {
//        LogDelegate.e("Error executing command: " + sqlInstruction, e);
//      }
//    }
//  }
//
//
//  /**
//   * Attachments update
//   */
//  public Attachment updateAttachment (Attachment attachment) {
//    return updateAttachment(-1, attachment, getDatabase(true));
//  }
//
//
//  /**
//   * New attachment insertion
//   */
//  public Attachment updateAttachment (long noteId, Attachment attachment, SQLiteDatabase db) {
//    ContentValues valuesAttachments = new ContentValues();
//    valuesAttachments.put(KEY_ATTACHMENT_ID, attachment.getId() != null ? attachment.getId() : Calendar
//        .getInstance().getTimeInMillis());
//    valuesAttachments.put(KEY_ATTACHMENT_NOTE_ID, noteId);
//    valuesAttachments.put(KEY_ATTACHMENT_URI, attachment.getUri().toString());
//    valuesAttachments.put(KEY_ATTACHMENT_MIME_TYPE, attachment.getMime_type());
//    valuesAttachments.put(KEY_ATTACHMENT_NAME, attachment.getName());
//    valuesAttachments.put(KEY_ATTACHMENT_SIZE, attachment.getSize());
//    valuesAttachments.put(KEY_ATTACHMENT_LENGTH, attachment.getLength());
//    db.insertWithOnConflict(TABLE_ATTACHMENTS, KEY_ATTACHMENT_ID, valuesAttachments, SQLiteDatabase.CONFLICT_REPLACE);
//    return attachment;
//  }
//
//
//  /**
//   * Getting single note
//   */
//  public Note getNote (long id) {
//    List<Note> notes = getNotes(" WHERE " + KEY_ID + " = " + id, true);
//    return notes.isEmpty() ? null : notes.get(0);
//  }
//
//
//  /**
//   * Getting All notes
//   *
//   * @param checkNavigation Tells if navigation status (notes, archived) must be kept in consideration or if all notes
//   * have to be retrieved
//   * @return Notes list
//   */
//  public List<Note> getAllNotes (Boolean checkNavigation) {
//    String whereCondition = "";
//    if (Boolean.TRUE.equals(checkNavigation)) {
//      int navigation = Navigation.getNavigation();
//      switch (navigation) {
//        case Navigation.NOTES:
//          return getNotesActive();
//        case Navigation.TRASH:
//          return getNotesTrashed();
//        default:
//          return getNotes(whereCondition, true);
//      }
//    } else {
//      return getNotes(whereCondition, true);
//    }
//
//  }
//
//
//  public List<Note> getNotesActive () {
//    String whereCondition = " WHERE " + KEY_ARCHIVED + " IS NOT 1 AND " + KEY_TRASHED + " IS NOT 1 ";
//    return getNotes(whereCondition, true);
//  }
//
//
//
//  /**
//   * Common method for notes retrieval. It accepts a query to perform and returns matching records.
//   */
//  public List<Note> getNotes (String whereCondition, boolean order) {
//    List<Note> noteList = new ArrayList<>();
//
//    String sortColumn = "";
//    String sortOrder = "";
//
//    // Getting sorting criteria from preferences. Reminder screen forces sorting.
//    if (Navigation.checkNavigation(Navigation.REMINDERS)) {
//      sortColumn = KEY_REMINDER;
//    } else {
//      sortColumn = prefs.getString(PREF_SORTING_COLUMN, KEY_TITLE);
//    }
//    if (order) {
//      sortOrder = KEY_TITLE.equals(sortColumn) || KEY_REMINDER.equals(sortColumn) ? " ASC " : " DESC ";
//    }
//
//    // In case of title sorting criteria it must be handled empty title by concatenating content
//    sortColumn = KEY_TITLE.equals(sortColumn) ? KEY_TITLE + "||" + KEY_CONTENT : sortColumn;
//
//    // In case of reminder sorting criteria the empty reminder notes must be moved on bottom of results
//    sortColumn = KEY_REMINDER.equals(sortColumn) ? "IFNULL(" + KEY_REMINDER + ", " +
//        "" + TIMESTAMP_UNIX_EPOCH + ")" : sortColumn;
//
//    // Generic query to be specialized with conditions passed as parameter
//    String query = "SELECT "
//        + KEY_CREATION + ","
//        + KEY_LAST_MODIFICATION + ","
//        + KEY_TITLE + ","
//        + KEY_CONTENT + ","
//        + KEY_TRASHED + ","
//        + " FROM " + TABLE_NOTES
//        + whereCondition
//        + (order ? " ORDER BY " + sortColumn + " COLLATE NOCASE " + sortOrder : "");
//
//    LogDelegate.v("Query: " + query);
//
//    try (Cursor cursor = getDatabase().rawQuery(query, null)) {
//
//      if (cursor.moveToFirst()) {
//        do {
//          int i = 0;
//          Note note = new Note();
//          note.setCreation(cursor.getLong(i++));
//          note.setLastModification(cursor.getLong(i++));
//          note.setTitle(cursor.getString(i++));
//          note.setContent(cursor.getString(i++));
//          note.setTrashed("1".equals(cursor.getString(i++)));
//
//          // Eventual decryption of content
//          if (Boolean.TRUE.equals(note.isLocked())) {
//            note.setContent(Security.decrypt(note.getContent(), prefs.getString(PREF_PASSWORD, "")));
//          }
//
//          // Add eventual attachments uri
//          note.setAttachmentsList(getNoteAttachments(note));
//
//          // Adding note to list
//          noteList.add(note);
//
//        } while (cursor.moveToNext());
//      }
//
//    }
//
//    LogDelegate.v("Query: Retrieval finished!");
//    return noteList;
//  }
//
//  /**
//   * Deleting single note
//   */
//  public boolean deleteNote (Note note) {
//    return deleteNote(note, false);
//  }
//
//
//  /**
//   * Deleting single note, eventually keeping attachments
//   */
//  public boolean deleteNote (Note note, boolean keepAttachments) {
//    return deleteNote(note.get_id(), keepAttachments);
//  }
//
//
//  /**
//   * Deleting single note by its ID
//   */
//  public boolean deleteNote (long noteId, boolean keepAttachments) {
//    SQLiteDatabase db = getDatabase(true);
//    db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(noteId)});
//    if (!keepAttachments) {
//      db.delete(TABLE_ATTACHMENTS, KEY_ATTACHMENT_NOTE_ID + " = ?", new String[]{String.valueOf(noteId)});
//    }
//    return true;
//  }
//
//  /**
//   * Gets notes matching pattern with title or content text
//   *
//   * @param pattern String to match with
//   * @return Notes list
//   */
//  public List<Note> getNotesByPattern (String pattern) {
//    String escapedPattern = escapeSql(pattern);
//    int navigation = Navigation.getNavigation();
//    String whereCondition = " WHERE "
//        + KEY_TRASHED + (navigation == Navigation.TRASH ? " IS 1" : " IS NOT 1")
//        + (navigation == Navigation.ARCHIVE ? " AND " + KEY_ARCHIVED + " IS 1" : "")
//        + (navigation == Navigation.CATEGORY ? " AND " + KEY_CATEGORY + " = " + Navigation.getCategory() : "")
//        + (navigation == Navigation.UNCATEGORIZED ? " AND (" + KEY_CATEGORY + " IS NULL OR " + KEY_CATEGORY_ID
//        + " == 0) " : "")
//        + (Navigation.checkNavigation(Navigation.REMINDERS) ? " AND " + KEY_REMINDER + " IS NOT NULL" : "")
//        + " AND ("
//        + " ( " + KEY_LOCKED + " IS NOT 1 AND (" + KEY_TITLE + " LIKE '%" + escapedPattern + "%' ESCAPE '\\' " + " OR "
//        +
//        KEY_CONTENT + " LIKE '%" + escapedPattern + "%' ESCAPE '\\' ))"
//        + " OR ( " + KEY_LOCKED + " = 1 AND " + KEY_TITLE + " LIKE '%" + escapedPattern + "%' ESCAPE '\\' )"
//        + ")";
//    return getNotes(whereCondition, true);
//  }
//
//  static String escapeSql (String pattern) {
//    return StringUtils.replace(pattern, "'", "''")
//                      .replace("%", "\\%")
//                      .replace("_", "\\_");
//  }
//
//
//
//  /**
//   * Retrieves all attachments related to specific note
//   */
//  public ArrayList<Attachment> getNoteAttachments (Note note) {
//    String whereCondition = " WHERE " + KEY_ATTACHMENT_NOTE_ID + " = " + note.get_id();
//    return getAttachments(whereCondition);
//  }
//
//
//  /**
//   * Retrieves all attachments
//   */
//  public ArrayList<Attachment> getAllAttachments () {
//    return getAttachments("");
//  }
//
//
//  /**
//   * Retrieves attachments using a condition passed as parameter
//   *
//   * @return List of attachments
//   */
//  public ArrayList<Attachment> getAttachments (String whereCondition) {
//
//    ArrayList<Attachment> attachmentsList = new ArrayList<>();
//    String sql = "SELECT "
//        + KEY_ATTACHMENT_ID + ","
//        + KEY_ATTACHMENT_URI + ","
//        + KEY_ATTACHMENT_NAME + ","
//        + KEY_ATTACHMENT_SIZE + ","
//        + KEY_ATTACHMENT_LENGTH + ","
//        + KEY_ATTACHMENT_MIME_TYPE
//        + " FROM " + TABLE_ATTACHMENTS
//        + whereCondition;
//    SQLiteDatabase db;
//    Cursor cursor = null;
//
//    try {
//
//      cursor = getDatabase().rawQuery(sql, null);
//
//      // Looping through all rows and adding to list
//      if (cursor.moveToFirst()) {
//        Attachment mAttachment;
//        do {
//          mAttachment = new Attachment(cursor.getLong(0),
//              Uri.parse(cursor.getString(1)), cursor.getString(2), cursor.getInt(3),
//              (long) cursor.getInt(4), cursor.getString(5));
//          attachmentsList.add(mAttachment);
//        } while (cursor.moveToNext());
//      }
//
//    } finally {
//      if (cursor != null) {
//        cursor.close();
//      }
//    }
//    return attachmentsList;
//  }
//
//
//
//
//    // Everything about attachments
//    int attachmentsAll = 0;
//    int images = 0;
//    int videos = 0;
//    int audioRecordings = 0;
//    int sketches = 0;
//    int files = 0;
//
//    List<Attachment> attachments = getAllAttachments();
//    for (Attachment attachment : attachments) {
//      if (MIME_TYPE_IMAGE.equals(attachment.getMime_type())) {
//        images++;
//      } else if (MIME_TYPE_VIDEO.equals(attachment.getMime_type())) {
//        videos++;
//      } else if (MIME_TYPE_AUDIO.equals(attachment.getMime_type())) {
//        audioRecordings++;
//      } else if (MIME_TYPE_SKETCH.equals(attachment.getMime_type())) {
//        sketches++;
//      } else if (MIME_TYPE_FILES.equals(attachment.getMime_type())) {
//        files++;
//      }
//    }
//    mStats.setAttachments(attachmentsAll);
//    mStats.setImages(images);
//    mStats.setVideos(videos);
//    mStats.setAudioRecordings(audioRecordings);
//    mStats.setSketches(sketches);
//    mStats.setFiles(files);
//
//    return mStats;
//  }
//}
//
//
//
//
