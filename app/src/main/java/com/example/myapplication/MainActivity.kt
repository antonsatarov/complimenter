package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private val compliment: MutableList<String> by lazy { fromFile() }

    private val animations: Array<Animation> by lazy {
        arrayOf(
            AnimationUtils.loadAnimation(this, R.anim.scale_in),
            AnimationUtils.loadAnimation(this, R.anim.scale_out),
            AnimationUtils.loadAnimation(this, R.anim.bounce_bottom),
            AnimationUtils.loadAnimation(this, R.anim.bounce_top)
        )
    }

    private val fileOpenLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    it.data?.data?.let { uri ->
                        val lines = contentResolver.openInputStream(uri)?.reader()?.readLines()
                        if (lines != null) {
                            compliment.addAll(lines)
                            Toast.makeText(this, "file loaded", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click(view: View) {
        val text = findViewById<TextView>(R.id.textView)
        text.text = compliment.random()
        text.visibility = View.VISIBLE
        text.startAnimation(animations.random())
    }

    private fun fromFile(): MutableList<String> {
        return resources.openRawResource(R.raw.compliments).reader().readLines().toMutableList()
    }

    fun onLoadFile(view: View) {
        fileOpenLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            data = Uri.parse(Environment.DIRECTORY_DOWNLOADS)
            type = "text/plain"
        })
    }

}