package com.example.praktikum10.model

data class Mahasiswa(
    val nim: String,
    val nama: String,
    val alamat: String,
    val jenisKelamin: String,
    val kelas: String,
    val angkatan: String
){
    constructor() : this("", "","", "", "", "") //membangun construktor untuk memberi nilai diawal
}

