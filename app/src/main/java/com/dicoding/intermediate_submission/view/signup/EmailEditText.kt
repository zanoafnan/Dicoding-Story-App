package com.dicoding.intermediate_submission.view.signup

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class EmailEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    private var isValidEmail: Boolean = false

    init {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                setError()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setError() {
        if (!isValidEmail) {
             error = "Alamat email tidak valid"
        } else {
            error = null
        }
    }
}



