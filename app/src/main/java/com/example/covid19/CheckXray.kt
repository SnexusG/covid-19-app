package com.example.covid19

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_check_xray.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CheckXray : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_xray)

        clickPicture.setOnClickListener {
            clickPicture()
        }
    }

    //Request code to cross-match later (Can be any integer)
    private val REQUEST_IMAGE_CAPTURE = 1888

    private fun clickPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {

                //Creating blank image file
                val photoFile: File = try {
                    createImageFile()
                } catch (ex: IOException) {
                    //Error occurred while creating the file
                    Log.d("clickPictures", ex.printStackTrace().toString())
                } as File

                photoFile.also {

                    //Getting photoURI to pass to the camera intent
                    val photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                    //Launch camera activity
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private lateinit var currentPhotoPath: String //Used later to get imageURI in any function needed

    @Throws(IOException::class)
    private fun createImageFile(): File {

        //Create a unique name using current date & time
        val timeStamp: String? = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        //Get the path to file directory
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
                "JPEG_${timeStamp}_",   // prefix
                ".jpg",                 // suffix
                storageDir                    // directory
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


    //This method is called when the user returns back to this activity after clicking a picture
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {

                //Convert the jpg at the URI to a bitmap & set it to the imageView
                val bitmap = BitmapFactory.decodeFile(currentPhotoPath).also { bitmap -> imageView.setImageBitmap(bitmap) }

                //Send the bitmap to the model for classification
                sendToModel(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.d("Activity Result", "error")
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun sendToModel(image: Bitmap) {
        //TODO: add model code and firebase code
    }
}