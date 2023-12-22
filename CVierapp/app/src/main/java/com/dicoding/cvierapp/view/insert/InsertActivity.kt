package com.dicoding.cvierapp.view.insert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.cvierapp.databinding.ActivityInsertBinding
import com.dicoding.cvierapp.view.ViewModelFactory
import com.dicoding.cvierapp.view.main.MainActivity
import com.dicoding.cvierapp.view.result.ResultActivity
import java.io.File
import java.io.InputStream

class InsertActivity : AppCompatActivity() {

    private lateinit var insertBinding: ActivityInsertBinding
    private lateinit var insertViewModel: InsertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        insertBinding = ActivityInsertBinding.inflate(layoutInflater)
        insertViewModel = getViewModel(this)
        setContentView(insertBinding.root)

        setAction()
        observeViewModel()
    }

    private fun setAction() {

        insertBinding.insertButton.setOnClickListener {
           getFileContent.launch("application/pdf")
        }

        insertBinding.insertBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        insertViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            insertBinding.apply {
                insertProgressbar.visibility = View.VISIBLE
                insertLayout.visibility = View.GONE
            }
        } else {
            insertBinding.apply {
                insertProgressbar.visibility = View.GONE
                insertLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun getSelectedJobIndex(): Int {
        val selectedRadioButtonId = insertBinding.insertJobRadiogroup.checkedRadioButtonId
        return insertBinding.insertJobRadiogroup.indexOfChild(
            insertBinding.root.findViewById(
                selectedRadioButtonId
            )
        )
    }

    private fun getViewModel(context: Context): InsertViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[InsertViewModel::class.java]
    }

    private val getFileContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val inputStream = contentResolver.openInputStream(uri)
            val file = createFileFromInputStream(inputStream)
            val jobIndex = getSelectedJobIndex()
            insertViewModel.uploadCV(file, jobIndex)
        }
    }

    private fun createFileFromInputStream(inputStream: InputStream?): File {
        val tempDir = cacheDir
        val tempFile = File.createTempFile("tempFile", null, tempDir)
        tempFile.outputStream().use { fileOut ->
            inputStream?.copyTo(fileOut)
        }
        return tempFile
    }

    private fun navigateToResultActivity() {
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun observeViewModel() {
        insertViewModel.cvResponse.observe(this) { result ->
            if (result.isSuccess) {
                navigateToResultActivity()
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Upload failed"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}