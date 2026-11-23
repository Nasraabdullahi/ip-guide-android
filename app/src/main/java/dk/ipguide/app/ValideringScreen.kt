package dk.ipguide.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.ipguide.app.ui.theme.*

@Composable
fun ValideringScreen(
    draft: ApplicationDraft,
    onBack: () -> Unit,
    onGoToStep1: () -> Unit,
    onGoToStep2: () -> Unit,
    onGoToStep3: () -> Unit,
    onNext: () -> Unit,
    onSaveDraft: () -> Unit
) {
    val validationResult = draft.validate()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PvsBackground)
    ) {
        PvsTopHeader(title = "Kladde ${draft.id}", onBack = onBack)

        PvsStepperBar(currentStep = 3) // Validation step

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🤖 ", fontSize = 20.sp)
                Text(
                    text = "Automatisk validering",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy
                )
            }

            Text(
                text = "Systemet har automatisk kontrolleret din ansøgningskladde i henhold til valideringsreglerne.",
                style = MaterialTheme.typography.bodyMedium,
                color = PvsTextSecondary
            )


            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (validationResult.isValid) StatusGreenBg else StatusYellowBg
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (validationResult.isValid) StatusGreen else StatusYellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (validationResult.isValid) "✓" else "⚠",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (validationResult.isValid) "Kladden opfylder alle krav" else "Mangler eller fejl fundet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PvsNavy
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (validationResult.isValid) {
                                "Din kladde har fået status: KLAR TIL EKSPORT"
                            } else {
                                "Ret venligst de markerede felter nedenfor for at kunne eksportere."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = PvsTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))


            ValidationCheckItem(
                title = "Varemærkenavn",
                isOk = draft.brandName.isNotBlank(),
                errorMessage = "Varemærkenavn mangler",
                onFixClick = onGoToStep1
            )

            ValidationCheckItem(
                title = "Beskrivelse af mærket",
                isOk = draft.description.isNotBlank() && draft.description.length >= 10,
                errorMessage = if (draft.description.isBlank()) "Beskrivelse mangler" else "Beskrivelse for kort (min. 10 tegn)",
                onFixClick = onGoToStep1
            )

            ValidationCheckItem(
                title = "Valgte vareklasser",
                isOk = draft.selectedClasses.isNotEmpty(),
                errorMessage = "Mindst én vareklasse skal vælges",
                onFixClick = onGoToStep2
            )

            ValidationCheckItem(
                title = if (draft.applicantType == ApplicantType.VIRKSOMHED) "Virksomhedsnavn & adresse" else "Ansøgers navn & adresse",
                isOk = draft.applicantName.isNotBlank() && draft.applicantAddress.isNotBlank(),
                errorMessage = "Ansøgeroplysninger skal udfyldes",
                onFixClick = onGoToStep3
            )

            if (draft.applicantType == ApplicantType.VIRKSOMHED) {
                ValidationCheckItem(
                    title = "CVR-nummer (8 cifre)",
                    isOk = draft.isCvrValid(),
                    errorMessage = if (draft.cvrNumber.isBlank()) "CVR-nummer mangler" else "CVR-nummer skal bestå af 8 cifre",
                    onFixClick = onGoToStep3
                )
            }

            ValidationCheckItem(
                title = "Gyldig e-mailadresse",
                isOk = draft.isEmailValid(),
                errorMessage = if (draft.applicantEmail.isBlank()) "E-mail mangler" else "Ugyldigt e-mail format",
                onFixClick = onGoToStep3
            )

            if (validationResult.warnings.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = PvsBlueLight),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("💡 Anbefalinger:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PvsNavy)
                        validationResult.warnings.forEach { warning ->
                            Text("• $warning", fontSize = 12.sp, color = PvsTextPrimary)
                        }
                    }
                }
            }
        }

        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onGoToStep1,
                    modifier = Modifier.weight(0.9f).height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ret felter", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = onSaveDraft,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PvsNavy)
                ) {
                    Text("Gem kladde", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }

                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1.1f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PvsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("Gennemse ➔", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ValidationCheckItem(
    title: String,
    isOk: Boolean,
    errorMessage: String,
    onFixClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isOk) StatusGreenBg else StatusRedBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isOk) "✓" else "✖",
                        color = if (isOk) StatusGreen else StatusRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = PvsTextPrimary
                    )
                    if (!isOk) {
                        Text(
                            text = errorMessage,
                            fontSize = 11.sp,
                            color = StatusRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (!isOk) {
                TextButton(onClick = onFixClick) {
                    Text("Ret", color = PvsNavy, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            } else {
                Text("✓ OK", color = StatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
