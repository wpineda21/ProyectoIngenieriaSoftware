package com.mcmp2023.s.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mcmp2023.s.ProductApplication.Companion.USER_NAME
import com.mcmp2023.s.ProductApplication.Companion.USER_ROLE
import com.mcmp2023.s.ProductApplication.Companion.USER_TOKEN
import com.mcmp2023.s.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database
       val myRef = database.getReference("message")

        myRef.setValue("Hello, 3!")
    }
}