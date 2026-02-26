package com.ulisesg.encuestasapp.freature.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ulisesg.encuestasapp.freature.presentation.components.EmptyState
import com.ulisesg.encuestasapp.freature.presentation.components.ErrorMessage
import com.ulisesg.encuestasapp.freature.presentation.components.EncuestasCard
import com.ulisesg.encuestasapp.freature.presentation.components.JoinPollDialog
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
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "LivePoll",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-1).sp
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showJoinDialog = true },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Unirse")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("create_poll") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nueva Encuesta", fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState) {
                is EncuestaUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is EncuestaUiState.Success -> {
                    if (uiState.encuestas.isEmpty()) {
                        EmptyState(
                            onJoin = { showJoinDialog = true },
                            onCreate = { navController.navigate("create_poll") }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    "Tus Encuestas",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
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
                    ErrorMessage(
                        message = uiState.message,
                        onRetry = { 
                            navController.navigate("list") {
                                popUpTo("list") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }

    if (showJoinDialog) {
        JoinPollDialog(
            value = pollIdToJoin,
            onValueChange = { pollIdToJoin = it },
            onDismiss = { showJoinDialog = false },
            onConfirm = {
                viewModel.joinPoll(pollIdToJoin)
                pollIdToJoin = ""
                showJoinDialog = false
            }
        )
    }
}
