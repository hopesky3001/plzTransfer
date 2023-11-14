package com.omapp.plztransfer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omapp.plztransfer.databinding.ActivityHelperBinding

class HelperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHelperBack.setOnClickListener {
            finish()
        }
    }
}