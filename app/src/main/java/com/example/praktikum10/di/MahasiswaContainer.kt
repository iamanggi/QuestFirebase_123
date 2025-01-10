package com.example.praktikum10.di

import android.content.Context
import com.example.praktikum10.repository.NetworkRepositoryMhs
import com.example.praktikum10.repository.RepositoryMhs
import com.google.firebase.firestore.FirebaseFirestore

interface InterfacesContainerApp {
    val repositoryMhs: RepositoryMhs
}

class MahasiswaContainer(private val context: Context) : InterfacesContainerApp {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance() //base function yang harus digunakan untuk menggunakan FirebaseFirestore
    override val repositoryMhs: RepositoryMhs by lazy {
        NetworkRepositoryMhs(firestore)
    }
}
