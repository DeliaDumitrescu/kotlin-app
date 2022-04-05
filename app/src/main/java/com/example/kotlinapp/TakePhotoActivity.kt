package com.example.kotlinapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TakePhotoActivity : AppCompatActivity() {
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var imgView: ImageView
    private lateinit var takePhotoBtn: Button
    private lateinit var exportBtn: Button

    private fun setBmp(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        imgView.setImageBitmap(bitmap)
    }

    fun getBmpUri(imageView: ImageView): Uri? {
        if(imageView.drawable !is BitmapDrawable) {
            return null;
        }

        var bmp: Bitmap = (imageView.drawable as BitmapDrawable).bitmap
        var bmpUri: Uri? = null

        try {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + ".png")
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            bmpUri = FileProvider.getUriForFile(this, "com.codepath.fileprovider", file);
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        imgView = findViewById(R.id.imageView)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setBmp(result.data)
            }
        }

        takePhotoBtn = findViewById(R.id.button)
        takePhotoBtn.setOnClickListener {
            launcher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))

        }

        exportBtn = findViewById(R.id.button2)
        exportBtn.setOnClickListener{
            val bmpUri = getBmpUri(imgView);
            if (bmpUri != null) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.photo

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.inspiration -> {
                    startActivity(Intent(applicationContext, InspirationActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.photo -> return@OnNavigationItemSelectedListener true
            }
            false
        })
    }
}