package com.green.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlin.collections.map

class MainActivity : AppCompatActivity() {
    private lateinit var filesHelper: FilesHelper
    private lateinit var editText: EditText
    private lateinit var txtFileList: TextView
    private lateinit var btnListFiles: Button
    private var isListMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filesHelper = FilesHelper()
        editText = findViewById(R.id.editText)
        txtFileList = findViewById(R.id.txtFileList)
        btnListFiles = findViewById(R.id.btnListFiles)
        // Save button: Save as new file
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            showSaveDialog()
        }

        // Open button: Choose from existing files
        findViewById<Button>(R.id.btnOpen).setOnClickListener {
            showOpenDialog()
        }

        // List files button (Task 2)
        btnListFiles.setOnClickListener {
            if (isListMode) {
                // Switch to editor mode
                editText.visibility = View.VISIBLE
                txtFileList.visibility = View.GONE
                btnListFiles.text = "List All Files"
            } else {
                // Switch to list mode
                editText.visibility = View.GONE
                txtFileList.visibility = View.VISIBLE
                listPrivateFiles()
                btnListFiles.text = "Show Editor"
            }
            isListMode = !isListMode
        }
    }

    private fun showSaveDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Save as")
            .setMessage("Enter filename:")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val filename = input.text.toString().trim()
                if (filename.isNotEmpty()) {
                    val finalName = if (filename.endsWith(".txt")) filename else "$filename.txt"
                    saveFile(finalName)
                } else {
                    Toast.makeText(this, "Filename cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveFile(filename: String) {
        val file = filesHelper.preparePrivateFile(applicationContext, filename)
        val content = editText.text.toString().toByteArray()
        filesHelper.writeToPrivateFile(file, content)
        Toast.makeText(this, "Saved: $filename", Toast.LENGTH_SHORT).show()
    }

    private fun showOpenDialog() {
        val files = getPrivateTextFiles()
        val filenames = files.map { it.name }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Open File")
            .setItems(filenames) { _, which ->
                val selectedFile = files[which]
                openFile(selectedFile)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openFile(file: File) {
        val content = filesHelper.readPrivateFile(file)?.toString(Charsets.UTF_8)
        editText.setText(content)
    }

    // Task 2: List all text files in private directory
    private fun listPrivateFiles() {
        val files = getPrivateTextFiles()
        txtFileList.text = if (files.isNotEmpty()) {
            files.joinToString("\n") { it.name }
        } else {
            "No text files found in private directory."
        }
    }

    private fun getPrivateTextFiles(): List<File> {
        val filesDir = applicationContext.filesDir
        return filesDir.listFiles()?.filter { it.name.endsWith(".txt") } ?: emptyList()
    }
}