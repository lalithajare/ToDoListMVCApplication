package com.example.todolistmvcapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.todolistmvcapplication.R
import com.example.todolistmvcapplication.utils.Utils

class LoginActivity : AppCompatActivity() {

    private lateinit var edtPassword: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        setViews()
    }

    private fun initViews() {
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
    }

    private fun setViews() {
        btnLogin.setOnClickListener {
            if (edtEmail.text.toString().trim().isNotEmpty())
                if (Utils.isEmailValid(edtEmail.text.toString().trim()))
                    if (edtPassword.text.toString().trim().isNotEmpty())
                        if (edtPassword.text.toString().trim().length > 5)
                            MainActivity.beginActivity(this)
                        else
                            Utils.showToast(getString(R.string.password_length_should_be_atleast_6))
                    else
                        Utils.showToast(getString(R.string.plz_enter_password))
                else
                    Utils.showToast(getString(R.string.plz_enter_valid_email))
            else
                Utils.showToast(getString(R.string.plz_enter_email))
        }
    }
}
