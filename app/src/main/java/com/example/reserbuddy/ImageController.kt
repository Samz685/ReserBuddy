package com.example.reserbuddy

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

object ImageController {

    fun selectPhotoFromGallery(activity: Activity, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }



    // Convierte una imagen en un arreglo de bytes
    fun convertImageToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray? {
        var inputStream: InputStream? = null
        var bytes: ByteArray? = null

        try {
            inputStream = contentResolver.openInputStream(uri)
            val buffer = ByteArrayOutputStream()

            val bufferSize = 1024
            val bufferData = ByteArray(bufferSize)
            var length: Int

            while (inputStream!!.read(bufferData).also { length = it } != -1) {
                buffer.write(bufferData, 0, length)
            }

            bytes = buffer.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return bytes
    }

    // Convierte un arreglo de bytes en un Bitmap
    fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


}
