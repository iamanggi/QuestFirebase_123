package com.example.praktikum10.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktikum10.R
import com.example.praktikum10.model.Mahasiswa
import com.example.praktikum10.ui.ViewModel.HomeUiState
import com.example.praktikum10.ui.ViewModel.HomeViewModel
import com.example.praktikum10.ui.ViewModel.PenyediaViewModel

@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
//
    var showDialog by remember { mutableStateOf(false) }
    var mahasiswaToDelete by remember { mutableStateOf<Mahasiswa?>(null) }

    Scaffold(
       topBar = {},

        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add Mahasiswa")
            }
        },
    ) {  innerPadding ->
        HomeStatus(
            homeUiState = viewModel.mhsUiState,
            retryAction = { viewModel.getMhs()},
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { viewModel.deleteMhs(it)
            }
        )

        if (showDialog && mahasiswaToDelete != null) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    mahasiswaToDelete?.let {
                        viewModel.getMhs()
                    }
                    showDialog = false
                    mahasiswaToDelete = null
                },
                onDeleteCancel = {
                    showDialog = false
                    mahasiswaToDelete = null
                }
            )
        }
    }
}

@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {},
    onDetailClick: (String) -> Unit
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf<Mahasiswa?>(null) }
    when(homeUiState){
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiState.Success -> {
            ListMahasiswa(
                listMhs = homeUiState.mahasiswa, modifier = modifier.fillMaxWidth(),
                onClick = {
                    onDetailClick(it)
                },
                onDelete = {
                    onDeleteClick(it)
                }
            )
            deleteConfirmationRequired?.let { data ->
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        onDeleteClick(data)
                        deleteConfirmationRequired = null
                    },
                    onDeleteCancel = {
                        deleteConfirmationRequired = null
                    })
            }
        }


        is HomeUiState.Error -> OnError(
            message = homeUiState.e.message ?: "Error",
            retryAction,
            modifier = modifier.fillMaxSize(),
            )
    }
}

@Composable
fun OnLoading(
    modifier: Modifier = Modifier
){
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = ""
    )
}

//home screen menampilkan pesan error dengan tombol coba lagi
@Composable
fun OnError(
    message: String,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.connection_error),
            contentDescription = ""
        )
        Text(
            text = message,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text("Coba lagi")
        }
    }
}


@Composable
fun ListMahasiswa(
    listMhs: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { },
    onDelete: (Mahasiswa) -> Unit = {}
){
    LazyColumn (modifier = modifier)
    {
        items(
            items = listMhs,
            itemContent = { mhs ->
                CardMhs(
                    mhs = mhs,
                    onClick = {onClick(mhs.nim)},
                    onDelete = {onDelete(mhs)}
                )
            }
        )
    }
}

@Composable
fun CardMhs(
    mhs: Mahasiswa,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    onDelete : (Mahasiswa) -> Unit = {}
) {
    Card (
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(modifier = Modifier.padding(12.dp)
        )
        {
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically)
            {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.nama,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Row (modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.nim,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.padding(4.dp))
                IconButton(onClick ={ onDelete(mhs)
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            }
            Row (modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.kelas,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(onDismissRequest = {},
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}