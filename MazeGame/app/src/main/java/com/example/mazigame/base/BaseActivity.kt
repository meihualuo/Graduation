package com.example.mazigame.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 自定义所有活动的父类，方便后期调整风格等统一属性
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun startActivity(actClass: Class<*>){
        startActivity(Intent(this,actClass))
    }
}