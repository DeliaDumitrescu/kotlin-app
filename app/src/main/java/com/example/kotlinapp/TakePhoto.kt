package com.example.kotlinapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.app.Activity
import android.graphics.Bitmap
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class TakePhoto : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var btnOpenCamera: Button
    private lateinit var ivPhoto: ImageView

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        ivPhoto.setImageBitmap(bitmap)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.photo

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.photo -> return@OnNavigationItemSelectedListener true
            }
            false
        })

        btnOpenCamera = findViewById(R.id.button)
        ivPhoto = findViewById(R.id.imageView)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    handleCameraImage(result.data)
                }
            }

        btnOpenCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)

        }
    }
}