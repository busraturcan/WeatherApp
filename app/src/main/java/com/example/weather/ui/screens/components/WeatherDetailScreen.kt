package com.example.weather.ui.screens.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.data.model.CityWeatherItem

@Composable
fun WeatherDetailScreen(
    cityItem: CityWeatherItem,
    onBack: () -> Unit
) {
    // Telefonun altındaki fiziksel geri tuşuna veya kaydırma hareketine basıldığında listeye döner
    BackHandler {
        onBack()
    }

    // Şehrin hava koduna göre dinamik degrade arka plan
    val backgroundGradient = when (cityItem.weatherCode) {
        0 -> Brush.verticalGradient(listOf(Color(0xFFF3904F), Color(0xFF3B4371))) // Güneşli / Açık
        1, 2, 3 -> Brush.verticalGradient(listOf(Color(0xFF536976), Color(0xFF292E49))) // Parçalı Bulutlu
        61, 63, 65, 51, 53, 55 -> Brush.verticalGradient(listOf(Color(0xFF3A7BD5), Color(0xFF3A6073))) // Yağmurlu
        71, 73, 75 -> Brush.verticalGradient(listOf(Color(0xFF83A4D4), Color(0xFFB6FBFF))) // Karlı
        95, 96, 99 -> Brush.verticalGradient(listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))) // Fırtına
        else -> Brush.verticalGradient(listOf(Color(0xFF4B6CB7), Color(0xFF182848))) // Standart Bulutlu
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedButton(
                    onClick = onBack,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("← Geri", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = cityItem.cityName,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (cityItem.locationDetail.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cityItem.locationDetail,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = cityItem.temperature,
                fontSize = 68.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = cityItem.conditionDescription,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(36.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Günlük Detaylar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailItem(title = "En Yüksek", value = cityItem.maxTemp)
                        DetailItem(title = "En Düşük", value = cityItem.minTemp)
                        DetailItem(title = "Rüzgar Hızı", value = cityItem.windSpeed)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.75f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}