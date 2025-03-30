package com.green.lab3

import android.content.Context
import android.util.Log
import java.io.File

/**
 * Helper class for various operations involving the File API.
 */
open class FilesHelper {
    /**
     * Helper method for obtaining filepaths of the app's private files.
     *
     * @param context an instance of the Activity's applicationContext or its equivalent
     * @param filename name of the file, with extension
     * @param subdir optional name of a subdirectory inside the app's private folder
     *
     * @return File object pointing to the expected private file
     */
    open fun preparePrivateFile(context: Context, filename: String, subdir: String? = null): File {
        val directory = if (subdir != null) {
            context.getDir(subdir, Context.MODE_PRIVATE)
        } else {
            context.filesDir
        }
        return File(directory, filename)
    }

    /**
     * Reads a file from the app's private directory.
     *
     * @param file File object pointing to the private file
     *
     * @return ByteArray with the entire content of the file
     */
    open fun readPrivateFile(file: File): ByteArray? {
        val inputStream = file.inputStream()
        val result = inputStream.readBytes()
        inputStream.close()
        return result
    }

    /**
     * Writes a file inside the app's private directory.
     *
     * @param file File object pointing to the private file
     * @param fileContents ByteArray with the entire content of the file
     */
    open fun writeToPrivateFile(file: File, fileContents: ByteArray) {
        try {
            val fileOutputStream = file.outputStream()
            fileOutputStream.write(fileContents)
            fileOutputStream.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.stackTraceToString())
        }
    }

    /**
     * Reads a file from shared storage.
     *
     * @param file File object pointing to the input file
     *
     * @return ByteArray with the entire content of the file
     */
    open fun readPublicFile(file: File): ByteArray? {
        return try {
            file.readBytes()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.stackTraceToString())
            null
        }
    }

    /**
     * Writes a file inside shared storage.
     *
     * @param file File object pointing to the output file
     * @param fileContents ByteArray with the entire content of the file
     */
    open fun writeToPublicFile(file: File, fileContents: ByteArray) {
        try {
            file.parentFile?.mkdirs()
            file.writeBytes(fileContents)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.stackTraceToString())
        }
    }
}