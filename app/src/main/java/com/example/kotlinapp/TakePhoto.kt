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

class TakePhoto : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var btnOpenCamera: Button
    private lateinit var btnShare: Button
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
        btnShare = findViewById(R.id.button2)
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

        btnShare.setOnClickListener{
            /*
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://codepath.com")
            startActivity(Intent.createChooser(shareIntent, "Share link using"))
            */

            val bmpUri = getLocalBitmapUri(ivPhoto);

            if (bmpUri != null) {

                // Construct a ShareIntent with link to image
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }

            /*
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/jpg"
            val photoFile = File(filesDir, "foo.jpg")
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile))
            startActivity(Intent.createChooser(shareIntent, "Share image using"))
            */
        }
    }

    fun getLocalBitmapUri(imageView: ImageView): Uri? {

        val drawable = imageView.drawable
        var bmp: Bitmap? = null
        bmp = if (drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            return null
        }

        var bmpUri: Uri? = null
        try {

            // Use methods on Context to access package-specific directories on external storage.

            // This way, you don't need to request external read/write permission.

            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()

            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            //bmpUri = Uri.fromFile(file)

            // In case this doesn't work in testing
            // https://guides.codepath.com/android/Sharing-Content-with-Intents
            // https://guides.codepath.com/android/Managing-Runtime-Permissions-with-PermissionsDispatcher
            bmpUri = FileProvider.getUriForFile(this, "com.codepath.fileprovider", file);
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }
}