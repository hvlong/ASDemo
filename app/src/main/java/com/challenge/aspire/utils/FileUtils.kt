package com.challenge.aspire.utils

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import com.challenge.aspire.BuildConfig.DEBUG
import com.challenge.aspire.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object FileUtils {

    private const val EXTENSION_PNG = ".png"
    private const val QUALITY_IMAGE = 93

    /**
     * Create output fileIdentity when take picture from camera
     *
     * @return fileIdentity
     */
    fun getOutputMediaFile(context: Context): File? {
        //        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
        //                Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
        val mediaStorageDir = context.getExternalFilesDir("")
        if (!mediaStorageDir!!.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                Date())
        return File(mediaStorageDir.path + File.separator +
                "IMG_" + timeStamp + ".jpg")
    }

    fun getFilePath(context: Context, uri: Uri): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
                        context.applicationContext, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id))
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver
                        .query(uri, projection, selection, selectionArgs, null)
                val column_index: Int
                if (cursor != null) {
                    column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index)
                    }
                }

            } catch (ignored: Exception) {
                cursor?.close()
            }

        } else if ("fileIdentity".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param source Bitmap
     * @param angle  angle
     * @return bitmap
     */
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
    }

    /**
     * @param context   Context
     * @param imageFile Uri
     * @return rotate
     */
    fun getRotationFromCamera(context: Context, imageFile: Uri): Int {
        var rotate = 0
        try {
            val exif = ExifInterface(imageFile.path)
            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rotate
    }

    private fun createFile(context: Context, bitmap: Bitmap, extension: String): File? {
        val mediaStorageDir = context.getExternalFilesDir("")
        if (!mediaStorageDir!!.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                Date())
        val destinationFilename = mediaStorageDir.path + File.separator +
                "IMG_" + timeStamp + extension

        //create a fileIdentity to write bitmap data
        val f = File(destinationFilename)
        try {
            f.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            val format: Bitmap.CompressFormat
            if (extension == EXTENSION_PNG) {
                format = Bitmap.CompressFormat.PNG
            } else {
                format = Bitmap.CompressFormat.JPEG
            }
            bitmap.compress(format, QUALITY_IMAGE /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return f
        } catch (e: IOException) {
            return null
        }

    }

    /**
     * @param context   context
     * @param imageName imageName
     * @return fileIdentity name
     */
    fun getNameFileDownload(context: Context, imageName: String): String {
        var imageName = imageName
        val type = imageName.substring(imageName.lastIndexOf("."))
        val name = imageName.replace(type, "")
        //Check fileIdentity exits

        if (checkFileExitsExternalStoragePublic(context, imageName)) {
            var i = 1
            imageName = "$name($i)$type"
            while (checkFileExitsExternalStoragePublic(context, imageName)) {
                i++
                imageName = "$name($i)$type"
            }
            return imageName
        }
        return imageName
    }

    /**
     * @param context  context
     * @param fileName fileName
     * @return true if fileIdentity existed, otherwise return false
     */
    private fun checkFileExitsExternalStoragePublic(context: Context, fileName: String): Boolean {
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS),
                context.getString(R.string.app_name) + "/" + fileName)
        return file.exists()
    }

    fun checkFileExitsExternalFilesDir(context: Context, fileName: String): Boolean {
        val file = File(context.getExternalFilesDir("").toString() + "/" + fileName)
        return file.exists()
    }

    @Throws(IOException::class)
    private fun copyFile(inputStream: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        while (inputStream.read(buffer) != -1) {
            out.write(buffer, 0, inputStream.read(buffer))
        }
    }

    /**
     * Copy fileIdentity to internal storage to external storage for sharing with other apps
     *
     * @param context        context
     * @param file           fileIdentity
     * @param fileNameOutput fileIdentity name output
     * @return fileIdentity to sharing
     */
    fun copyFileFromInternalToExternalStorage(context: Context, file: File,
            fileNameOutput: String): File? {
        val folderDownload = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)
        if (folderDownload.exists()) {
            val mediaStorageDir = File(folderDownload, context.getString(R.string.app_name))
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }

            if (mediaStorageDir.exists()) {
                val outFile = File(mediaStorageDir, fileNameOutput)
                var inputStream: InputStream? = null
                val out: OutputStream
                return try {
                    inputStream = FileInputStream(file)
                    out = FileOutputStream(outFile)
                    FileUtils.copyFile(inputStream, out)
                    inputStream.close()
                    out.flush()
                    out.close()
                    outFile
                } catch (e: Exception) {
                    null
                }

            }
        }
        return null
    }

    fun getPDFPath(context: Context, uri: Uri): String {

        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri, projection, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see .getPath
     */
    fun getFile(context: Context, uri: Uri?): File? {
        if (uri != null) {
            val path: String?
            path = getPath(context, uri)
            if (path != null && isLocal(path)) {
                return File(path)
            }
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @author paulburke
     */
    private fun isLocalStorageDocument(uri: Uri): Boolean {
        val AUTHORITY = "com.ianhanniballake.localstorage.documents"
        //        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
        return AUTHORITY == uri.authority
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br></br>
     * <br></br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     * @see .isLocal
     * @see .getFile
     */
    fun getPath(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri)
            } else if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)

                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:".toRegex(), "")
                    }
                    try {
                        val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                java.lang.Long.valueOf(id))
                        return getDataColumn(context, contentUri, null, null)
                    } catch (e: NumberFormatException) {
                        return null
                    }

                }

            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
            // ExternalStorageProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri,
                    null, null)

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * @return Whether the URI is a local one.
     */
    private fun isLocal(url: String?): Boolean {
        return !(url == null || url.startsWith("http://") || url.startsWith("https://"))
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?): String? {
        if (uri == null) {
            return null
        }
        var cursor: Cursor? = null
        val column = MediaStore.Files.FileColumns.DATA
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor)

                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


}