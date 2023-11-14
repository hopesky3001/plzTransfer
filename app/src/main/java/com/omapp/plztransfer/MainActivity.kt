package com.omapp.plztransfer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omapp.plztransfer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //성적 입력으로 이동
        binding.TFInsert.setOnClickListener {
            val intent = Intent(this, AddTFActivity::class.java)
            startActivity(intent)
        }

        //성적 확인으로 이동
        binding.TFCheck.setOnClickListener {
            val intent =Intent(this, CheckTFActivity::class.java)
            startActivity(intent)
        }

        binding.TFHelp.setOnClickListener {
            val intent =Intent(this, HelperActivity::class.java)
            startActivity(intent)
        }
    }
}