package com.android.chefshare.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.chefshare.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // nút đăng nhập
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email == "admin" && password == "123456") {
                startActivity(Intent(this, MainActivity::class.java))
                finish() // kết thúc màn hình Login
            } else {
                binding.etPassword.error = "Email hoặc mật khẩu không đúng"
            }
        }

        //login bằng Google
        binding.btnGoogleLogin.setOnClickListener {
            // Tạm thời cho qua màn hình chính luôn
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
