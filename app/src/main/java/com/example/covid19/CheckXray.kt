package com.example.covid19

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.*
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import kotlinx.android.synthetic.main.activity_check_xray.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CheckXray : AppCompatActivity() {

    private var interpreter: FirebaseModelInterpreter? = null
    private var labeler: FirebaseVisionImageLabeler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_xray)

        val remoteModel = FirebaseCustomRemoteModel.Builder("x_ray_classifier").build()
        val conditions = FirebaseModelDownloadConditions.Builder()
                .build()

        val remoteModelAutoML = FirebaseAutoMLRemoteModel.Builder("x_ray_classifier_202048144954").build()


        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener {
                    val options = FirebaseModelInterpreterOptions.Builder(remoteModel).build()
                    interpreter = FirebaseModelInterpreter.getInstance(options)
                    //clickPictureButton.visibility = View.VISIBLE
                }

        FirebaseModelManager.getInstance().download(remoteModelAutoML, conditions)
                .addOnCompleteListener {
                    val optionsBuilder = FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(remoteModelAutoML)
                    val options = optionsBuilder.setConfidenceThreshold(0.5f).build()
                    labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options)
                    clickPictureButton.visibility = View.VISIBLE
                }

        clickPictureButton.setOnClickListener {
            clickPicture()
        }

        pickPictureButton.setOnClickListener {
            pickPicture()
        }
    }

    //Request code to cross-match later (Can be any integer)
    private val REQUEST_IMAGE_CAPTURE = 1
    private val OPERATION_CHOOSE_PHOTO = 2

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

    private fun pickPicture() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
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

        Log.d("ActivityResult", "resultCode: $resultCode requestCode: $requestCode")

        when (requestCode) {

            REQUEST_IMAGE_CAPTURE -> {

                if (resultCode == Activity.RESULT_OK) {
                    try {

                        //Convert the jpg at the URI to a bitmap & set it to the imageView
                        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)

                        //Send the bitmap to the model for classification
                        //sendToModel(bitmap)
                        setImageView(bitmap, true)
                        sendToAutoMLModel(bitmap)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            OPERATION_CHOOSE_PHOTO -> {

                if (resultCode == Activity.RESULT_OK) {

                    Log.d("ActivityResult", "HERE")
                    Log.d("ActivityResult", "${data!!.data}")
                    hiddenImageView.setImageURI(data.data)
                    val bitmap = (hiddenImageView.drawable as BitmapDrawable).bitmap

                    setImageView(bitmap, false)
                    //sendToModel(bitmap)
                    sendToAutoMLModel(bitmap)

                } else {
                    Log.d("ActivityResult", "ERROR IN OPERATION_CHOOSE_PHOTO")
                }

            }

            else -> {
                Log.d("ActivityResult", "ERROR")
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun sendToModel(image: Bitmap) {

        val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
                .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 150, 150, 3))
                .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 1))
                .build()

        val bitmap = Bitmap.createScaledBitmap(image, 150, 150, true)
        imageView.setImageBitmap(bitmap)
        val batchNum = 0
        val input = Array(1) { Array(150) { Array(150) { FloatArray(3) } } }

        for (x in 0..149) {
            for (y in 0..149) {
                val pixel = bitmap.getPixel(x, y)

                input[batchNum][x][y][0] = (Color.red(pixel)) / 255.0f
                input[batchNum][x][y][1] = (Color.green(pixel)) / 255.0f
                input[batchNum][x][y][2] = (Color.blue(pixel)) / 255.0f

                Log.d("image: ", "R:${input[batchNum][x][y][0]}, G:${input[batchNum][x][y][1]}, B:${input[batchNum][x][y][2]}")
            }
        }

        val inputs = FirebaseModelInputs.Builder()
                .add(input)
                .build()


        interpreter!!.run(inputs, inputOutputOptions)
                .addOnSuccessListener { result ->
                    // ...

                    val output = result.getOutput<Array<FloatArray>>(0)
                    val probabilities = output[0]

                    for (i in probabilities) {
                        Log.d("Result: ", "$i")
                    }


                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }


    }

    private fun sendToAutoMLModel(receivedImage: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(receivedImage)

        labeler!!.processImage(image)
                .addOnSuccessListener { labels ->

                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        outcomeTextView.text = text
                        confidenceTextView.text = "${confidence*100}%"

                        Log.d("AUTOML", "Result: $text = $confidence")
                    }

                }
                .addOnFailureListener {
                    Log.d("AUTOML", "Result: ERROR")
                }
    }

    private fun setImageView(image: Bitmap, rotation: Boolean) {
        if (rotation)
            imageView.rotation = 90f
        else
            imageView.rotation = 0f
        val bitmap = Bitmap.createScaledBitmap(image, 800, 800, false)
        imageView.setImageBitmap(bitmap)
        outcomeLayout.visibility = View.VISIBLE
    }
}

