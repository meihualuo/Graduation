package com.example.mazigame.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * 自定义所有活动的父类，方便后期调整风格等统一属性
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)  //竖屏
        this.setContentView(getLayoutRes())
    }

    abstract fun getLayoutRes():Int

    fun startActivity(actClass: Class<*>){
        startActivity(Intent(this,actClass))
    }


    open fun setLinearLayoutManager(recyclerView: RecyclerView): LinearLayoutManager? {
        val manager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = manager
        (recyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
        return manager
    }

}