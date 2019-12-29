package com.example.teacherclient;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

class MyStaticValue {


    static final int STATE_OK = 0;
    static final String LOGIN_PATH = "http://122.51.186.91:8081/teacher/login";
    static final String UPDATE_INFO_PATH = "http://122.51.186.91:8081/teacher/updateInfo";
    static final String UPLOAD_AVATAR_PATH = "http://122.51.186.91:8081/file/uploadAvatar";
    static final String GET_AVATAR_PATH = "http://122.51.186.91:8081/file/getAvatar";
    static final String GET_INFO_PATH = "http://122.51.186.91:8081/teacher/getInfo";
    static final String CREATE_CLASSROOM = "http://122.51.186.91:8081/course/createClass";
    static final String GET_CLASS_INFO = "http://122.51.186.91:8081/course/getClass";
    static final String UPLOAD_FILE = "http://122.51.186.91:8081/file/uploadFile";
    static final String GET_FILE_INFO = "http://122.51.186.91:8081/file/getFileInfo";
    static final String DOWNLOAD_FILE = "http://122.51.186.91:8081/file/getFile";
    static final String SEND_ANNOUNCE = "http://122.51.186.91:8081/course/sendAnnounce";
    static final String GET_ANNOUNCE = "http://122.51.186.91:8081/course/getAnnounce";
    static final String SEND_HOMEWORK = "http://122.51.186.91:8081/course/publishHomework";
    static final String GET_HOMEWORK = "http://122.51.186.91:8081/course/getHomeworkInfo";
    static final String SEND_TEST = "http://122.51.186.91:8081/course/publishTest";
    static final String GET_TEST = "http://122.51.186.91:8081/course/getTestInfo";
    static final String CHOOSE_STUDENT = "http://122.51.186.91:8081/course/getRandomCall";
    static final String SEND_CALL = "http://122.51.186.91:8081/course/startCall";
    static final String NEW_DISCUSS = "http://122.51.186.91:8081/discuss/new";
    static final String GET_DISCUSS = "http://122.51.186.91:8081/discuss/getDiscuss";

    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }


    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
