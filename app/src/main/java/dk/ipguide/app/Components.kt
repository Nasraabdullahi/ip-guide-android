package dk.ipguide.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.ipguide.app.ui.theme.*

@Composable
fun PvsTopHeader(
    title: String? = null,
    onBack: (() -> Unit)? = null
) {
    Surface(
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "←",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PvsNavy
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(36.dp))
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = PvsNavy,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "♔",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title ?: "Patent- og Varemærkestyrelsen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ⓘ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy
                )
            }
        }
    }
}

@Composable
fun PvsStepperBar(currentStep: Int = 1) {
    val steps = listOf(
        "1\nOplysninger",
        "2\nMærke",
        "3\nKlasser",
        "4\nKontakt",
        "5\nOversigt"
    )

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, stepText ->
                val stepNum = index + 1
                val isActive = stepNum == currentStep
                val isCompleted = stepNum < currentStep

                val parts = stepText.split("\n")
                val number = parts[0]
                val label = parts.getOrNull(1) ?: ""

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(58.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isActive -> PvsNavy
                                    isCompleted -> StatusGreen
                                    else -> Color(0xFFE2E8F0)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCompleted) "✓" else number,
                            color = if (isActive || isCompleted) Color.White else PvsTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = label,
                        fontSize = 9.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        color = if (isActive) PvsNavy else PvsTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(bottom = 12.dp)
                            .background(
                                if (stepNum < currentStep) StatusGreen else Color(0xFFE2E8F0)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun CertificateIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PvsBlueLight),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(140.dp)
                .height(150.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(4.dp)
                            .background(PvsBlueLight, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(4.dp)
                            .background(PvsBlueLight, RoundedCornerShape(2.dp))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .border(2.dp, PvsNavy, CircleShape)
                            .background(PvsBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "®",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PvsNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("★", fontSize = 10.sp, color = Color(0xFFFFB300))
                        Text("★", fontSize = 10.sp, color = Color(0xFFFFB300))
                        Text("★", fontSize = 10.sp, color = Color(0xFFFFB300))
                    }
                }
            }
        }
    }
}
