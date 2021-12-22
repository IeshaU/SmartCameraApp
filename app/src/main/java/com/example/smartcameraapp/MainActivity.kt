package com.example.smartcameraapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Build a button that launches the camera app
        findViewById<Button>(R.id.button).setOnClickListener {
            //TODO: Launch camera app

            //Create an intent that launches the camera and takes a picture

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (e: ActivityNotFoundException) {
                    // display error state to the user
                }
            }

        }
    // Obtain thumbnail
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap

            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)

            //Prepare bitmap for ML Kit API
            val imageForMLKit = InputImage.fromBitmap(imageBitmap, 0)

            //Create instance of ImageLabeler
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            //Process imageforMLKIT using labeler

            var outputText = ""
            labeler.process(imageForMLKit)
                .addOnSuccessListener { labels ->
                    Log.i("Iesha", "successfully processed image")
                    for (label in labels) {
                        val text = label.text
                        val confidence = (label.confidence*100).roundToInt()
                        val index = label.index
                        val textView = findViewById<TextView>(R.id.textView)
                        val labelList = null
                        outputText = "$text : $confidence %\n"
                        textView.text = outputText
                        Log.i("Iesha", "LOOP > [$text]:$confidence")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Iesha", "Error processing image")
                }

        }
    }

}