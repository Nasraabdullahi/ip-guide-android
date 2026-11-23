package dk.ipguide.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.ipguide.app.ui.theme.*

@Composable
fun Step1OplysningerScreen(
    draft: ApplicationDraft,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSaveDraft: () -> Unit
) {
    var brandName by remember { mutableStateOf(draft.brandName) }
    var brandType by remember { mutableStateOf(draft.brandType) }
    var hasLogo by remember { mutableStateOf(draft.hasLogoImage) }
    var description by remember { mutableStateOf(draft.description) }

    var helpTitle by remember { mutableStateOf<String?>(null) }
    var helpDesc by remember { mutableStateOf<String?>(null) }
    var helpExample by remember { mutableStateOf<String?>(null) }

    fun updateDraftState() {
        draft.brandName = brandName
        draft.brandType = brandType
        draft.hasLogoImage = hasLogo
        draft.description = description
    }

    if (helpTitle != null && helpDesc != null) {
        HelpDialog(
            title = helpTitle!!,
            description = helpDesc!!,
            example = helpExample,
            onDismiss = {
                helpTitle = null
                helpDesc = null
                helpExample = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PvsBackground)
    ) {
        PvsTopHeader(title = "Kladde ${draft.id}", onBack = onBack)

        PvsStepperBar(currentStep = 1)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📄 ", fontSize = 20.sp)
                Text(
                    text = "1. Varemærkeoplysninger",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy
                )
            }

            Text(
                text = "Indtast oplysninger om det varemærke, du ønsker at registrere. Obligatoriske felter er markeret med *.",
                style = MaterialTheme.typography.bodyMedium,
                color = PvsTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Field 1: Varemærkenavn
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Varemærkenavn *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PvsTextPrimary
                )

                Text(
                    text = "ⓘ Hvad er et varemærkenavn?",
                    fontSize = 11.sp,
                    color = PvsNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        helpTitle = "Varemærkenavn"
                        helpDesc = "Det navn eller kendetegn, som din virksomhed vil bruge til at skille sine varer eller tjenesteydelser ud fra andre."
                        helpExample = "F.eks. 'LEGO', 'Carlsberg' eller 'Lunar'."
                    }
                )
            }

            OutlinedTextField(
                value = brandName,
                onValueChange = { brandName = it },
                placeholder = { Text("F.eks. Mit Mærke") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Field 2: Type af mærke
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Type af mærke *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PvsTextPrimary
                )

                Text(
                    text = "ⓘ Hvad er mærketyper?",
                    fontSize = 11.sp,
                    color = PvsNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        helpTitle = "Mærketyper"
                        helpDesc = "Ordmærke beskytter kun teksten uanset skrifttype. Figurmærke beskytter et logo. Kombinationsmærke beskytter både logo og tekst sammen."
                        helpExample = "Ordmærke: 'NIKE' (kun ordet)\nFigurmærke: Swoosh-logoet uten tekst"
                    }
                )
            }

            val types = listOf(
                "Ordmærke (kun tekst)",
                "Figurmærke (kun logo/grafik)",
                "Kombinationsmærke (tekst + logo)"
            )

            var expanded by remember { mutableStateOf(false) }

            Box {
                OutlinedTextField(
                    value = brandType,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Text(
                            text = "▾",
                            fontSize = 16.sp,
                            color = PvsNavy,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    types.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                brandType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Image Upload Section (US03)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PvsBlueLight),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Logo / Billedfil",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = PvsNavy
                        )
                        Text(
                            text = if (hasLogo) "✓ Logo vedhæftet (Logo_MitMaerke.png)" else "Påkævet for figurmærker og kombinationsmærker",
                            fontSize = 11.sp,
                            color = if (hasLogo) StatusGreen else PvsTextSecondary
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            hasLogo = !hasLogo
                            draft.logoFileName = if (hasLogo) "Logo_MitMaerke.png" else null
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PvsNavy)
                    ) {
                        Text(if (hasLogo) "Fjern fil" else "📷 Vælg fil", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Field 3: Beskrivelse
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Beskrivelse af varemærket *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PvsTextPrimary
                )

                Text(
                    text = "ⓘ Hvordan beskrives det?",
                    fontSize = 11.sp,
                    color = PvsNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        helpTitle = "Beskrivelse"
                        helpDesc = "Beskriv kort dit varemærke, f.eks. hvad navnet/logoet dækker over, og hvor det vil blive brugt."
                        helpExample = "F.eks. 'Mærket består af ordet MitMærke skrevet med blå bogstaver og bruges til salg af software og tøj.'"
                    }
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = { if (it.length <= 500) description = it },
                placeholder = { Text("Beskriv dit varemærke kort (f.eks. hvad navnet/logoet dækker over, og hvordan det anvendes).") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${description.length} / 500 tegn",
                    fontSize = 11.sp,
                    color = PvsTextSecondary
                )
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        updateDraftState()
                        onSaveDraft()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PvsNavy)
                ) {
                    Text("Gem kladde", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = {
                        updateDraftState()
                        onNext()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PvsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("Næste: Klasser ➔", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
