package com.example.formtreeapp.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.formtreeapp.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.infoTextView3.movementMethod = LinkMovementMethod.getInstance()
        setContentView(binding.root)
    }
}