package com.example.praktikum10.ui.navigation

interface DestinasiNavigasi{
    val route: String
    val titleRes: String
}

object DestinasiHome : DestinasiNavigasi {   //object akan menjadi nama halaman/ menjadi pengenal halaman
    override val route: String = "home"
    override val titleRes: String = "Home"
}

object DestinasiInsert : DestinasiNavigasi {   //object akan menjadi nama halaman/ menjadi pengenal halaman
    override val route: String = "insert"
    override val titleRes: String = "Insert"
}