package com.example.praktikum10.repository

import com.example.praktikum10.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

//jika error maka lakukan implementasi member
class NetworkRepositoryMhs(
    //memanggil firestorenya
    private val firestore: FirebaseFirestore
) : RepositoryMhs {
    override suspend fun insertMhs(mahasiswa: Mahasiswa) {
        try{
            firestore.collection("Mahasiswa").add(mahasiswa).await()
        }
        catch (e: Exception){
            throw Exception ("Gagal menambahkan data mahasiswa: ${e.message}")
        }
    }

    override fun getAllMhs(): Flow<List<Mahasiswa>> = callbackFlow {  //memanggil data secara real-time dengan menggunakan callbacakFlow dan harus mendukung dengan flow
      // membuka collection dari firestore
        val mhsCollection  =  firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.ASCENDING)
            .addSnapshotListener {  //yang bisa membuat real-time
                value, error ->
                if(value != null){
                    val mhsList = value.documents.mapNotNull { //value yang didalam mapNotNull dimasukkan kedalam object bernama mahasiswa
                        // convert dari dokumen firestore ke data class
                        it.toObject(Mahasiswa::class.java)!!
                    }
                    //fungsi untuk mengirim collection ke data class
                    trySend(mhsList)
                }
            }
        awaitClose{
            //menutup collection dari firestore
            mhsCollection.remove()
        }
    }

    override fun getMhs(nim: String): Flow<Mahasiswa> = callbackFlow {
       val mhsDocument = firestore.collection("Mahasiswa")
           .document(nim)
           .addSnapshotListener{ value, error ->
               if (value != null){
                   val mhs = value.toObject(Mahasiswa::class.java)!!
                   trySend(mhs)
               }
           }
        awaitClose{
            mhsDocument.remove()
        }
    }

    override suspend fun deleteMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .delete()
                .await()
        }
        catch (e: Exception){
            throw Exception("Gagal menghapus data mahasiswa: ${e.message}")
        }
    }

    override suspend fun updateMhs(mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }
}