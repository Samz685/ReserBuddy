package com.example.reserbuddy

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

object DialogUtils {

//    fun showSuccessDialog(context: Context, message: String) {
//        val dialog = Dialog(context)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_confirmacion_registro)
//
//        val animationView = dialog.findViewById<LottieAnimationView>(R.id.animation_view)
//        val messageText = dialog.findViewById<TextView>(R.id.message_text)
//
//        // Configurar la animación
//        animationView.setAnimation("success_animation.json")
//        animationView.playAnimation()
//
//        messageText.text = message
//
//        // Escuchar el evento de finalización de la animación
//        animationView.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(p0: Animator) {}
//
//            override fun onAnimationEnd(p0: Animator) {
//                // Redirigir al MainActivity al finalizar la animación
//                val intent = Intent(context, MainActivity::class.java)
//                context.startActivity(intent)
//                dialog.dismiss()
//            }
//
//            override fun onAnimationCancel(p0: Animator) {}
//            override fun onAnimationRepeat(p0: Animator) {}
//        })
//
//        dialog.show()
//    }
}


