package com.example.reserbuddy

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

class BotonHelper {
    companion object {
        fun habilitarBotonAdd(button: Button, vararg editTexts: EditText) {
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // Verificar si todos los EditText están vacíos
                    button.isEnabled = editTexts.all { !it.text.isNullOrEmpty() }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }

            editTexts.forEach { it.addTextChangedListener(textWatcher) }
        }
    }
}
