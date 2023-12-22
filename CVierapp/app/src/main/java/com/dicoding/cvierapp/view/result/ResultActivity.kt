package com.dicoding.cvierapp.view.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.cvierapp.R
import com.dicoding.cvierapp.databinding.ActivityResultBinding
import com.dicoding.cvierapp.view.ViewModelFactory
import com.dicoding.cvierapp.view.insert.InsertActivity
import com.dicoding.cvierapp.view.main.MainActivity

class ResultActivity : AppCompatActivity() {

   private lateinit var resultBinding: ActivityResultBinding
   private lateinit var resultViewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding = ActivityResultBinding.inflate(layoutInflater)
        resultViewModel = getViewModel(this)
        setContentView(resultBinding.root)

        setAction()
    }

    private fun setAction() {
        resultBinding.resultBackToMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        resultBinding.resultTestMoreButton.setOnClickListener {
            val intent = Intent(this, InsertActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        resultBinding.resultShareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.result_share))
            startActivity(Intent.createChooser(shareIntent, "Share Text Via"))
        }

        resultViewModel.fetchResultFromAPI()

        resultViewModel.percentageText.observe(this) { percentageText ->
            resultBinding.resultProgressbarText.text = percentageText
        }

        resultViewModel.progressBarValue.observe(this) { progressBarValue ->
            resultBinding.resultProgressbarStatus.progress = progressBarValue
        }

        resultViewModel.improveContent.observe(this) { improveContent ->
            resultBinding.resultCvImproveContent.text = improveContent
        }

        resultViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun getViewModel(context: Context): ResultViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[ResultViewModel::class.java]
    }

    private fun showLoading(isLoading:  Boolean) {
        if (isLoading) {
            resultBinding.resultLoadingProgressbar.visibility = View.VISIBLE
            resultBinding.resultLayout.visibility = View.GONE
        } else {
            resultBinding.resultLoadingProgressbar.visibility = View.GONE
            resultBinding.resultLayout.visibility = View.VISIBLE
        }
    }
}