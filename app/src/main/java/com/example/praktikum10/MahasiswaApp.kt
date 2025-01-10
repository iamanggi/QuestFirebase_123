package com.example.praktikum10

import android.app.Application
import com.example.praktikum10.di.MahasiswaContainer

class MahasiswaApp : Application(){
    //fungsinya untuk menyimpan instance containerApp dari dependencies injection(di)
    lateinit var containerApp: MahasiswaContainer

    override fun onCreate() {
        super.onCreate()

        // membuat instance containerApp
        containerApp = MahasiswaContainer(this)
        // instance adalah object yang dibuat dari class
    }
}