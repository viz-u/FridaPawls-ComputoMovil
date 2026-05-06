package mx.edu.itesca.fridapawls_2026.network

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CloudinaryService {

    private val client = OkHttpClient()

    private val cloudName = "dsuqoy6uk"
    private val uploadPreset = "fridapawls_upload"

    // 🔥 FUNCIÓN SUSPEND (IMPORTANTE)
    suspend fun uploadImage(
        context: Context,
        uri: Uri
    ): String = withContext(Dispatchers.IO) {

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("No se pudo abrir la imagen")

        val bytes = inputStream.readBytes()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpg",
                bytes.toRequestBody("image/*".toMediaTypeOrNull())
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        val json = response.body?.string()
            ?: throw Exception("Respuesta vacía")

        val url = JSONObject(json).getString("secure_url")

        return@withContext url
    }
}