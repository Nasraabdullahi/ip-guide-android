package dk.ipguide.app

import androidx.compose.foundation.background
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
fun NyAnsogningScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSaveDraft: () -> Unit
) {
    var brandName by remember { mutableStateOf("") }
    var brandType by remember { mutableStateOf("Ordmærke (kun tekst)") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PvsBackground)
    ) {
        PvsTopHeader(title = "Ny ansøgning", onBack = onBack)

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
                Text(
                    text = "📄 ",
                    fontSize = 20.sp
                )
                Text(
                    text = "Oplysninger om varemærket",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy
                )
            }

            Text(
                text = "Fortæl kort om det varemærke, du vil registrere.",
                style = MaterialTheme.typography.bodyMedium,
                color = PvsTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Field 1: Varemærkenavn
            Text(
                text = "Varemærkenavn *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PvsTextPrimary
            )
            OutlinedTextField(
                value = brandName,
                onValueChange = { brandName = it },
                placeholder = { Text("Mit Mærke") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Text(
                text = "ⓘ  Angiv det navn, du ønsker som varemærke.",
                fontSize = 11.sp,
                color = PvsTextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Field 2: Type af mærke
            Text(
                text = "Type af mærke *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PvsTextPrimary
            )
            OutlinedTextField(
                value = brandType,
                onValueChange = { brandType = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = { Text("▾", fontSize = 16.sp, color = PvsNavy) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Text(
                text = "ⓘ  Vælg den type, der passer til dit mærke (f.eks. ord, logo eller kombination).",
                fontSize = 11.sp,
                color = PvsTextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Field 3: Beskrivelse
            Text(
                text = "Beskrivelse *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PvsTextPrimary
            )
            OutlinedTextField(
                value = description,
                onValueChange = { if (it.length <= 500) description = it },
                placeholder = { Text("Beskriv dit varemærke kort, f.eks. hvad navnet dækker over, og hvordan det bruges.") },
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
                    text = "${description.length} / 500",
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
                    onClick = onSaveDraft,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PvsNavy)
                ) {
                    Text("Gem kladde", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PvsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("Næste ➔", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
