package com.example.covid19

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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


    private val REQUEST_IMAGE_CAPTURE = 1888

    private fun clickPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {

                val photoFile: File = try {
                    createImageFile()
                } catch (ex: IOException) {
                    //Error occurred while creating the file
                    Log.d("clickPictures", "IO-Exception")
                } as File

                photoFile.also {
                    val photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {

        //Create an image file
        val timeStamp: String? = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                Log.d("ActivityResult", currentPhotoPath)

                val bitmap = BitmapFactory.decodeFile(currentPhotoPath).also { bitmap -> imageView.setImageBitmap(bitmap) }
                sendToModel(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.d("Activity Result", "error")
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun sendToModel(image: Bitmap){
        //TODO: add model code and firebase code
    }
}