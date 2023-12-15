package com.dicoding.intermediate_submission.view.signup

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText : TextInputEditText {

    private val MIN_PASSWORD_LENGTH = 8

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
         addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 < MIN_PASSWORD_LENGTH) {
                    setError("Password harus terdiri dari minimal 8 karakter")
                } else {
                     error = null
                }
            }
        })
    }
}
