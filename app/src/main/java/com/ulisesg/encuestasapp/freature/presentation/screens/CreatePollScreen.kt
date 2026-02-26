package com.ulisesg.encuestasapp.freature.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ulisesg.encuestasapp.freature.presentation.viewmodels.EncuestasViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePollScreen(
    navController: NavController,
    viewModel: EncuestasViewModel = hiltViewModel()
) {
    var question by remember { mutableStateOf("") }
    val options = remember { mutableStateListOf("", "") }
    var createdPollId by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collectLatest { event ->
            if (event is EncuestasViewModel.UiEvent.PollCreated) {
                createdPollId = event.poll.id
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Encuesta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (createdPollId == null) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Pregunta") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Opciones", style = MaterialTheme.typography.titleMedium)

                options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = option,
                            onValueChange = { options[index] = it },
                            label = { Text("Opción ${index + 1}") },
                            modifier = Modifier.weight(1f)
                        )
                        if (options.size > 2) {
                            IconButton(onClick = { options.removeAt(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                Button(
                    onClick = { options.add("") },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.textButtonColors()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir Opción")
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = {
                        if (question.isNotBlank() && options.all { it.isNotBlank() }) {
                            viewModel.createPoll(question, options.toList())
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = question.isNotBlank() && options.size >= 2 && options.all { it.isNotBlank() }
                ) {
                    Text("Generar Código de Encuesta")
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¡Encuesta Creada!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(text = "Comparte este código con otros:")
                    
                    Surface(
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = createdPollId!!,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(createdPollId!!))
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copiar")
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                    
                    Button(
                        onClick = {
                            question = ""
                            options.clear()
                            options.addAll(listOf("", ""))
                            createdPollId = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear otra encuesta")
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver mis encuestas")
                    }
                }
            }
        }
    }
}
