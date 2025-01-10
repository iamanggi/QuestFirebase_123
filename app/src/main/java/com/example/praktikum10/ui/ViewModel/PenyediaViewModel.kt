package com.example.praktikum10.ui.ViewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.praktikum10.MahasiswaApp

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                mahasiswaApp().containerApp.repositoryMhs
            )
        }
        initializer {
            InsertViewModel(
                mahasiswaApp().containerApp.repositoryMhs
            )
        }
    }

    fun CreationExtras.mahasiswaApp(): MahasiswaApp =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as MahasiswaApp)
}