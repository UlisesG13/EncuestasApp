package com.ulisesg.encuestasapp.freature.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ulisesg.encuestasapp.freature.presentation.components.EncuestasCard
import com.ulisesg.encuestasapp.freature.presentation.viewmodels.EncuestasViewModel
import com.ulisesg.encuestasapp.freature.presentation.viewmodels.EncuestaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveysListScreen(
    navController: NavController,
    viewModel: EncuestasViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    var showJoinDialog by remember { mutableStateOf(false) }
    var pollIdToJoin by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Encuestas en Vivo") },
                actions = {
                    IconButton(onClick = { showJoinDialog = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Unirse a encuesta")
                    }
                    IconButton(onClick = { navController.navigate("create_poll") }) {
                        Icon(Icons.Default.Add, contentDescription = "Crear nueva")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_poll") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Encuesta")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is EncuestaUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EncuestaUiState.Success -> {
                    if (uiState.encuestas.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No hay encuestas en tu lista.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.height(16.dp))
                            Row {
                                Button(onClick = { showJoinDialog = true }) {
                                    Text("Unirse con ID")
                                }
                                Spacer(Modifier.width(8.dp))
                                OutlinedButton(onClick = { navController.navigate("create_poll") }) {
                                    Text("Crear Encuesta")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.encuestas) { encuesta ->
                                EncuestasCard(
                                    encuesta = encuesta,
                                    onVote = { optionIndex ->
                                        viewModel.vote(encuesta.id, optionIndex)
                                    }
                                )
                            }
                        }
                    }
                }
                is EncuestaUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error: ${uiState.message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.loadEncuestas() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }

    if (showJoinDialog) {
        AlertDialog(
            onDismissRequest = { showJoinDialog = false },
            title = { Text("Unirse a una encuesta") },
            text = {
                OutlinedTextField(
                    value = pollIdToJoin,
                    onValueChange = { pollIdToJoin = it },
                    label = { Text("ID de la encuesta") },
                    placeholder = { Text("Ej: F81A70") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (pollIdToJoin.isNotBlank()) {
                        viewModel.joinPoll(pollIdToJoin)
                        pollIdToJoin = ""
                        showJoinDialog = false
                    }
                }) {
                    Text("Unirse")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJoinDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
