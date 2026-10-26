
package com.example.weather.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.data.model.CityWeatherItem
import com.example.weather.ui.viewmodel.WeatherUiState
import com.example.weather.ui.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: WeatherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val citySuggestions by viewModel.citySuggestions.collectAsState()
    var searchInput by rememberSaveable { mutableStateOf("") }
    var selectedCityForDetail by remember {
        mutableStateOf<CityWeatherItem?>(null)
    }
    val focusManager = LocalFocusManager.current

    if (selectedCityForDetail != null) {
        WeatherDetailScreen(
            cityItem = selectedCityForDetail!!,
            onBack = { selectedCityForDetail = null }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hava Durumu 🇹🇷", fontWeight = FontWeight.Bold)
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            if (isDarkTheme) "🌙" else "☀️",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onToggleTheme() }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Türkiye Genel Bakış",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aşağıdaki şehirlerden birine dokunarak detaylı hava tahminini görebilirsiniz.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                            alpha = 0.8f
                        )
                    )
                }
            }

            OutlinedTextField(
                value = searchInput,
                onValueChange = { newValue ->
                    searchInput = newValue
                    viewModel.onSearchQueryChanged(newValue)
                },
                label = { Text("Şehir ara") },
                placeholder = { Text("Örn. Ankara, Antalya...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchCity(searchInput)
                        focusManager.clearFocus()
                    }
                )
            )

            if (searchInput.trim().length >= 2 && citySuggestions.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column {
                        citySuggestions.forEach { city ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = city.name,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = listOfNotNull(
                                                city.region,
                                                city.country
                                            ).joinToString(", "),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                },
                                onClick = {
                                    searchInput = city.name
                                    viewModel.selectCity(city)
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is WeatherUiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is WeatherUiState.Empty -> {
                        Text(
                            text = "Aradığınız şehir bulunamadı.",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    is WeatherUiState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { viewModel.retry() }) {
                                Text("Tekrar Dene")
                            }
                        }
                    }

                    is WeatherUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.items) { item ->
                                WeatherCard(
                                    item = item,
                                    isSelected = false,
                                    onClick = {
                                        selectedCityForDetail = item
                                    }
                                )
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}
