package com.green.lab3

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import java.io.FileOutputStream

/**
 * Helper class for various operations involving the Storage Access Framework.
 */
open class ScopedStorageHelper {
    /**
     * Start the system picker for opening documents from shared storage.
     *
     * @param startForResultFileOpen just create a registerForActivityResult and pass it here
     * @param mediaType MIME type of the file
     * @param initialUri uri of the initial directory
     */
    open fun pickFileToOpen(startForResultFileOpen: ActivityResultLauncher<Intent>,
                            mediaType: String = "*/*",
                            initialUri: String = Environment.DIRECTORY_DOCUMENTS) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mediaType);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri);
        try {
            startForResultFileOpen.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(javaClass.simpleName, e.stackTraceToString())
        }
    }

    /**
     * Reads a file from shared storage.
     *
     * @param contentResolver an instance of the Activity's contentResolver or its equivalent
     * @param uri URI pointing to the input file
     *
     * @return ByteArray with the entire content of the file
     */
    open fun readFile(contentResolver: ContentResolver?, uri: Uri): ByteArray? {
        var result: ByteArray? = null
        contentResolver?.let {
            val inputStream = it.openInputStream(uri)
            result = inputStream?.readBytes()
            inputStream?.close()
        }
        return result
    }

    /**
     * Start the system picker for writing documents in shared storage.
     *
     * @param startForResultFileSave just create a registerForActivityResult and pass it here
     * @param mediaType MIME type of the file, such as "application/txt"
     * @param filename name of the file, with extension
     * @param initialUri uri of the initial directory
     */
    open fun pickFileToSave(startForResultFileSave: ActivityResultLauncher<Intent>,
                            mediaType: String, filename: String,
                            initialUri: String = Environment.DIRECTORY_DOCUMENTS) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mediaType);
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri);
        try {
            startForResultFileSave.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(javaClass.simpleName, e.stackTraceToString())
        }
    }

    /**
     * Writes a file in shared storage.
     *
     * @param contentResolver an instance of the Activity's contentResolver or its equivalent
     * @param uri URI pointing to the output file
     * @param fileContents ByteArray with the entire content of the file
     */
    open fun writeToFile(contentResolver: ContentResolver?, uri: Uri, fileContents: ByteArray) {
        try {
            contentResolver?.let {
                val parcelFileDescriptor = it.openFileDescriptor(uri, "w")
                val fileOutputStream = FileOutputStream(parcelFileDescriptor?.fileDescriptor)
                fileOutputStream.write(fileContents)
                fileOutputStream.close()
                parcelFileDescriptor?.close()
            }
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.stackTraceToString())
        }
    }
}