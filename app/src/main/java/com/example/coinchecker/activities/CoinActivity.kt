package com.example.coinchecker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coinchecker.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    lateinit var binding: ActivityCoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutToolbar.toolBar.title = " "
    }
}