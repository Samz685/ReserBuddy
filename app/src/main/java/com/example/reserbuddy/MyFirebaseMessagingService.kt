package com.example.reserbuddy

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Manejar el mensaje push entrante aquí
        sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // Enviar el token de registro a su servidor de backend o almacenarlo localmente para usarlo más tarde

        // Si desea probar las notificaciones push en su dispositivo, puede imprimir el token de registro en la consola de Android Studio
        Log.d(TAG, "FCM registration token: $token")
    }


    private fun sendNotification(title: String?, message: String?) {
        // Mostrar la notificación push en el dispositivo
    }


}