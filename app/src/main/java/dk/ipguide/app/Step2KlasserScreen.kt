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
fun Step2KlasserScreen(
    draft: ApplicationDraft,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSaveDraft: () -> Unit
) {
    val selectedClasses = remember { mutableStateListOf<Int>().apply { addAll(draft.selectedClasses) } }
    var customText by remember { mutableStateOf(draft.customGoodsServices) }
    var showError by remember { mutableStateOf(false) }

    var helpTitle by remember { mutableStateOf<String?>(null) }
    var helpDesc by remember { mutableStateOf<String?>(null) }
    var helpExample by remember { mutableStateOf<String?>(null) }

    fun updateDraftState() {
        draft.selectedClasses.clear()
        draft.selectedClasses.addAll(selectedClasses)
        draft.customGoodsServices = customText
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

        PvsStepperBar(currentStep = 2)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏷️ ", fontSize = 20.sp)
                    Text(
                        text = "2. Varer & Tjenesteydelser",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PvsNavy
                    )
                }

                Text(
                    text = "ⓘ Hvad er Nice-klasser?",
                    fontSize = 11.sp,
                    color = PvsNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        helpTitle = "Nice-klassifikation"
                        helpDesc = "Et internationalt system med 45 klasser (34 for varer, 11 for tjenester). Du skal vælge de klasser, som dækker dine produkter eller ydelser."
                        helpExample = "Sælger du tøj online? Vælg Klasse 25 (Beklædning) og Klasse 35 (Online handel)."
                    }
                )
            }

            Text(
                text = "Vælg de relevante vare- og tjenesteydelsesklasser (Nice-klasser). Du skal vælge mindst én klasse.",
                style = MaterialTheme.typography.bodyMedium,
                color = PvsTextSecondary
            )

            if (showError && selectedClasses.isEmpty()) {
                Surface(
                    color = StatusRedBg,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ Fejl: Du skal vælge mindst én vareklasse for at gå videre.",
                        color = StatusRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Class selection checkboxes (US04)
            defaultNiceClasses.forEach { niceClass ->
                val isChecked = selectedClasses.contains(niceClass.classNumber)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isChecked) {
                                selectedClasses.remove(niceClass.classNumber)
                            } else {
                                selectedClasses.add(niceClass.classNumber)
                            }
                            showError = false
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isChecked) PvsBlueLight else Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    if (!selectedClasses.contains(niceClass.classNumber)) {
                                        selectedClasses.add(niceClass.classNumber)
                                    }
                                } else {
                                    selectedClasses.remove(niceClass.classNumber)
                                }
                                showError = false
                            },
                            colors = CheckboxDefaults.colors(checkedColor = PvsNavy)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = niceClass.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PvsNavy
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = niceClass.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = PvsTextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Custom Goods & Services details field
            Text(
                text = "Specifikke varer eller tjenesteydelser (valgfrit)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PvsTextPrimary
            )
            OutlinedTextField(
                value = customText,
                onValueChange = { customText = it },
                placeholder = { Text("F.eks. 'Salg af børnetøj på internettet, IT-konsulentbistand'") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
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
                    onClick = {
                        updateDraftState()
                        onBack()
                    },
                    modifier = Modifier.weight(0.8f).height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Tilbage", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = {
                        updateDraftState()
                        onSaveDraft()
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PvsNavy)
                ) {
                    Text("Gem kladde", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        if (selectedClasses.isEmpty()) {
                            showError = true
                        } else {
                            updateDraftState()
                            onNext()
                        }
                    },
                    modifier = Modifier.weight(1.2f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PvsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("Næste: Ansøger ➔", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
