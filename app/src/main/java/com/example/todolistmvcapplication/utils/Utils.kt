package com.example.todolistmvcapplication.utils

import android.widget.Toast

object Utils {
    fun showToast(msg: String) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show()
    }
}