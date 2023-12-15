package com.dicoding.intermediate_submission.view.storyadd

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.intermediate_submission.data.pref.UserPreference
import com.dicoding.intermediate_submission.data.pref.dataStore
import com.dicoding.intermediate_submission.databinding.ActivityStoryAddBinding
import com.dicoding.intermediate_submission.di.Injection
import com.dicoding.intermediate_submission.view.story.StoryActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class StoryAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryAddBinding

    private var imageFromCamera: Bitmap? = null
    private var imageFromGallery: Bitmap? = null

    private val viewModel: StoryAddViewModel by viewModels()

    private val context = this
    private val pref = UserPreference.getInstance(context.dataStore)

    private val takePicture: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                 val data: Intent? = result.data
                imageFromCamera = data?.getParcelableExtra("data")
                binding.previewImageView.setImageBitmap(imageFromCamera)
            }
        }

     private val pickFromGallery: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                 if (data != null && data.data != null) {
                    val selectedImageUri = data.data

                    val imageBitmap = loadBitmapFromUri(selectedImageUri)

                    imageFromGallery = imageBitmap

                    binding.previewImageView.setImageBitmap(imageBitmap)

                    viewModel.finishUpload()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraButton.setOnClickListener {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            viewModel.startUpload()
            takePicture.launch(intent)
            viewModel.finishUpload()
        }

        binding.galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            viewModel.startUpload()
            pickFromGallery.launch(intent)
        }

        binding.uploadButton.setOnClickListener {
            val description = binding.editTextArea.text.toString()
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())

            if (description.isNotEmpty() && (imageFromCamera != null || imageFromGallery != null)) {
                viewModel.startUpload()
                val imageFile = imageFromCamera?.let { bitmapToFile(it) } ?: imageFromGallery?.let { it1 ->
                    bitmapToFile(
                        it1
                    )
                }

                val lat = 0.0
                val lon = 0.0

                if (imageFile != null) {
                    uploadStory(requestBodyDescription, imageFile, lat, lon)
                }
            } else {
               }
        }

        viewModel.isUploading.observe(this) { isUploading ->
            if (isUploading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.finishUpload()
    }

    private fun uploadStory(description: RequestBody, imageFile: File, lat: Double, lon: Double) {


        val apiService = Injection.provideApiService(this, pref)

         val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)

        val call = apiService.uploadStory(description, photoPart, lat.toFloat(), lon.toFloat())

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@StoryAddActivity, StoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    try {
                        val errorResponse = response.errorBody()?.string()
                          } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        viewModel.finishUpload()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                viewModel.finishUpload()
            }
        })
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val file = File(cacheDir, "image.jpg")
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return file
    }


    private fun loadBitmapFromUri(uri: Uri?): Bitmap? {
        return try {
            if (uri != null) {
                val inputStream = contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}