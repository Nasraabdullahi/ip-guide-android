package dk.ipguide.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.ipguide.app.ui.theme.*

@Composable
fun OversigtScreen(
    draft: ApplicationDraft,
    onBack: () -> Unit,
    onGoToStep1: () -> Unit,
    onGoToStep2: () -> Unit,
    onGoToStep3: () -> Unit,
    onSaveDraft: () -> Unit
) {
    val context = LocalContext.current
    val validation = draft.validate()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PvsBackground)
    ) {
        PvsTopHeader(title = "Oversigt ${draft.id}", onBack = onBack)

        PvsStepperBar(currentStep = 5)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status Header Banner (US09)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (validation.isValid) StatusGreenBg else StatusYellowBg
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (validation.isValid) StatusGreen else StatusYellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (validation.isValid) "✓" else "⚠",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (validation.isValid) "Kladde klar til eksport" else "Kladde under udarbejdelse",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PvsNavy
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (validation.isValid) {
                                "Alle oplysninger er udfyldt og valideret. Du kan oprette en PDF-fil med din kladde."
                            } else {
                                "Gennemgå felterne nedenfor og ret de manglende oplysninger."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = PvsTextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Card 1: Varemærkeoplysninger (US03, US09)
            SummarySectionCard(
                title = "1. Varemærkeoplysninger",
                onEdit = onGoToStep1,
                content = {
                    Text("Varemærkenavn: ${draft.brandName.ifBlank { "[Mangler]" }}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Mærketype: ${draft.brandType}", fontSize = 12.sp, color = PvsTextSecondary)
                    Text("Beskrivelse: ${draft.description.ifBlank { "[Mangler]" }}", fontSize = 12.sp, color = PvsTextSecondary)
                    if (draft.hasLogoImage) {
                        Text("✓ Logo vedhæftet (Logo_MitMaerke.png)", fontSize = 11.sp, color = StatusGreen)
                    }
                }
            )

            // Card 2: Valgte Vareklasser (US04, US09)
            SummarySectionCard(
                title = "2. Valgte Vareklasser",
                onEdit = onGoToStep2,
                content = {
                    val classStr = if (draft.selectedClasses.isEmpty()) {
                        "[Ingen vareklasser valgt]"
                    } else {
                        draft.selectedClasses.joinToString(", ") { "Klasse $it" }
                    }
                    Text(classStr, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (draft.selectedClasses.isEmpty()) StatusRed else PvsNavy)
                    if (draft.customGoodsServices.isNotBlank()) {
                        Text("Detaljer: ${draft.customGoodsServices}", fontSize = 12.sp, color = PvsTextSecondary)
                    }
                }
            )

            // Card 3: Ansøgeroplysninger (US05, US09)
            SummarySectionCard(
                title = "3. Ansøgeroplysninger",
                onEdit = onGoToStep3,
                content = {
                    Text("Type: ${draft.applicantType.label}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = PvsNavy)
                    Text("Navn: ${draft.applicantName.ifBlank { "[Mangler]" }}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    if (draft.applicantType == ApplicantType.VIRKSOMHED) {
                        Text(
                            "CVR-nummer: ${if (draft.isCvrValid()) draft.cvrNumber else "[Ugyldigt CVR - kræver 8 cifre]"}",
                            fontSize = 12.sp,
                            color = if (draft.isCvrValid()) PvsTextSecondary else StatusRed
                        )
                    }
                    Text("Adresse: ${draft.applicantAddress.ifBlank { "[Mangler]" }}", fontSize = 12.sp, color = PvsTextSecondary)
                    Text("E-mail: ${draft.applicantEmail.ifBlank { "[Mangler]" }}", fontSize = 12.sp, color = if (draft.isEmailValid()) PvsTextSecondary else StatusRed)
                }
            )
        }

        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // PDF Export Button (US10)
                Button(
                    onClick = {
                        PdfExporter.generateAndSharePdf(context, draft)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PvsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("📥  Eksporter PDF", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onSaveDraft,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PvsNavy)
                ) {
                    Text("Gem kladde & afslut", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SummarySectionCard(
    title: String,
    onEdit: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy
                )

                Text(
                    text = "Ret",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onEdit() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            content()
        }
    }
}
