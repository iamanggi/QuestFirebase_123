package com.example.praktikum10.ui.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.praktikum10.model.Mahasiswa
import com.example.praktikum10.repository.RepositoryMhs
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mhs: RepositoryMhs
) : ViewModel(){

    var mhsUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set
    init {
        getMhs()
    }

    fun getMhs(){
        viewModelScope.launch {
            mhs.getAllMhs().onStart {
                mhsUiState = HomeUiState.Loading
            }
                .catch {
                    mhsUiState = HomeUiState.Error(e = it)
                }
                .collect{
                    mhsUiState = if (it.isEmpty()){
                            HomeUiState.Error(Exception("Belum ada data mahasiswa"))
                    }
                    else{
                        HomeUiState.Success(it)
                    }
                }
        }
    }
}

//kebutuhan di home yaitu ketika loading, sukses, dan error
sealed class HomeUiState{
    object Loading : HomeUiState() //loading -> kenapa pake object karena merubah state saja tidak merubah data
    data class Success(val mahasiswa: List<Mahasiswa>): HomeUiState() //sukses
    data class Error(val e:Throwable): HomeUiState()  //error
}