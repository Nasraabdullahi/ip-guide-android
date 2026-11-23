package dk.ipguide.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.ipguide.app.ui.theme.*

@Composable
fun Step3AnsogerScreen(
    draft: ApplicationDraft,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSaveDraft: () -> Unit
) {
    var applicantType by remember { mutableStateOf(draft.applicantType) }
    var name by remember { mutableStateOf(draft.applicantName) }
    var cvrNumber by remember { mutableStateOf(draft.cvrNumber) }
    var email by remember { mutableStateOf(draft.applicantEmail) }

    var cvrStatusMessage by remember { mutableStateOf<String?>(null) }
    var isCvrError by remember { mutableStateOf(false) }

    // Parse existing address
    var street by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isAutoCityFound by remember { mutableStateOf(false) }

    LaunchedEffect(draft.applicantAddress) {
        if (draft.applicantAddress.isNotBlank()) {
            val parts = draft.applicantAddress.split(", ")
            if (parts.size >= 2) {
                street = parts[0]
                val postParts = parts[1].split(" ")
                if (postParts.isNotEmpty()) {
                    postalCode = postParts[0]
                    city = postParts.drop(1).joinToString(" ")
                }
            } else {
                street = draft.applicantAddress
            }
        }
    }

    var showError by remember { mutableStateOf(false) }

    var helpTitle by remember { mutableStateOf<String?>(null) }
    var helpDesc by remember { mutableStateOf<String?>(null) }
    var helpExample by remember { mutableStateOf<String?>(null) }

    fun isEmailValid(emailStr: String): Boolean {
        val trimmed = emailStr.trim()
        if (trimmed.isBlank() || !trimmed.contains("@")) return false
        val parts = trimmed.split("@")
        if (parts.size != 2) return false
        val local = parts[0]
        val domain = parts[1]
        return local.isNotBlank() && domain.contains(".") && domain.substringAfterLast(".").length >= 2
    }

    fun updateDraftState() {
        draft.applicantType = applicantType
        draft.applicantName = name
        draft.cvrNumber = cvrNumber
        val fullAddress = if (street.isNotBlank() || postalCode.isNotBlank() || city.isNotBlank()) {
            "$street, $postalCode $city".trim().removePrefix(", ").removeSuffix(", ")
        } else {
            ""
        }
        draft.applicantAddress = fullAddress
        draft.applicantEmail = email
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

        PvsStepperBar(currentStep = 4) // Note: Step 4 visually is Kontakt

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "👤 ", fontSize = 20.sp)
                    Text(
                        text = "3. Ansøgeroplysninger",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PvsNavy
                    )
                }

                Text(
                    text = "ⓘ Hvem er ansøger?",
                    fontSize = 11.sp,
                    color = PvsNavy,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        helpTitle = "Ansøgeroplysninger"
                        helpDesc = "Det navn, CVR og den adresse, som varemærket skal registreres på. For virksomheder slås navnet og adressen automatisk op i CVR-registeret."
                        helpExample = "Privatperson: 'Maria Jensen'\nVirksomheder (f.eks. CVR 10150817): 'LEGO System A/S'"
                    }
                )
            }

            Text(
                text = "Vælg om ansøger er en privatperson eller en virksomhed.",
                style = MaterialTheme.typography.bodyMedium,
                color = PvsTextSecondary
            )

            // Applicant Type Choice Cards (Privatperson vs Virksomhed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = if (applicantType == ApplicantType.PRIVATPERSON) 2.dp else 1.dp,
                            color = if (applicantType == ApplicantType.PRIVATPERSON) PvsNavy else PvsCardBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            applicantType = ApplicantType.PRIVATPERSON
                            cvrStatusMessage = null
                            isCvrError = false
                            showError = false
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (applicantType == ApplicantType.PRIVATPERSON) PvsBlueLight else Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "👤", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "Privatperson",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = PvsNavy
                            )
                            Text("Personlig ejer", fontSize = 10.sp, color = PvsTextSecondary)
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = if (applicantType == ApplicantType.VIRKSOMHED) 2.dp else 1.dp,
                            color = if (applicantType == ApplicantType.VIRKSOMHED) PvsNavy else PvsCardBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            applicantType = ApplicantType.VIRKSOMHED
                            showError = false
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (applicantType == ApplicantType.VIRKSOMHED) PvsBlueLight else Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🏢", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "Virksomhed",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = PvsNavy
                            )
                            Text("Slås op i CVR", fontSize = 10.sp, color = PvsTextSecondary)
                        }
                    }
                }
            }

            if (showError) {
                Surface(
                    color = StatusRedBg,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ Udfyld venligst alle obligatoriske felter med et gyldigt CVR-nummer og e-mail format.",
                        color = StatusRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // FOR VIRKSOMHED: CVR-nummer
            if (applicantType == ApplicantType.VIRKSOMHED) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "CVR-nummer (8 cifre) *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PvsTextPrimary
                )
                OutlinedTextField(
                    value = cvrNumber,
                    onValueChange = { input ->
                        if (input.length <= 8 && input.all { it.isDigit() }) {
                            cvrNumber = input
                            showError = false

                            if (input.length == 8) {
                                val company = CvrRegister.lookup(input)
                                if (company != null) {
                                    name = company.companyName
                                    street = company.street
                                    postalCode = company.postalCode
                                    city = company.city
                                    isAutoCityFound = true
                                    cvrStatusMessage = "⚡ Virksomhed fundet i CVR: ${company.companyName}"
                                    isCvrError = false
                                } else {
                                    cvrStatusMessage = "❌ CVR-nummeret blev ikke fundet i CVR-registeret. Indtast et gyldigt CVR-nummer."
                                    isCvrError = true
                                }
                            } else {
                                cvrStatusMessage = null
                                isCvrError = false
                            }
                        }
                    },
                    placeholder = { Text("F.eks. 10150817 (LEGO) eller 38268370 (PVS)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isCvrError,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                if (cvrStatusMessage != null) {
                    Surface(
                        color = if (isCvrError) StatusRedBg else StatusGreenBg,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = cvrStatusMessage!!,
                            color = if (isCvrError) StatusRed else StatusGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    Text(
                        text = "ⓘ Skriv 8-cifret CVR for automatisk at hente virksomhedens navn og adresse",
                        fontSize = 11.sp,
                        color = PvsTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))


                Text(
                    text = "Virksomhedsnavn * (fra CVR)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PvsTextPrimary
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showError = false
                    },
                    placeholder = { Text("Indtast CVR ovenfor for automatisk udfyldelse") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            } else {
                // FOR PRIVATPERSON: Fulde Navn
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Fulde Navn *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PvsTextPrimary
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showError = false
                    },
                    placeholder = { Text("F.eks. Maria Jensen") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Vej og husnummer
            Text(
                text = "Vejnavn og nummer *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PvsTextPrimary
            )
            OutlinedTextField(
                value = street,
                onValueChange = {
                    street = it
                    showError = false
                },
                placeholder = { Text("F.eks. Hovedgaden 12, 1. th.") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Postnummer & By
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(0.4f)) {
                    Text(
                        text = "Postnr. *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PvsTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = postalCode,
                        onValueChange = { input ->
                            if (input.length <= 4) {
                                postalCode = input
                                showError = false
                                val foundCity = DanishPostalCodes.getCityName(input)
                                if (foundCity != null) {
                                    city = foundCity
                                    isAutoCityFound = true
                                } else {
                                    isAutoCityFound = false
                                }
                            }
                        },
                        placeholder = { Text("2100") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Column(modifier = Modifier.weight(0.6f)) {
                    Text(
                        text = "By *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PvsTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = city,
                        onValueChange = {
                            city = it
                            showError = false
                        },
                        placeholder = { Text("København Ø") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            if (isAutoCityFound && city.isNotBlank() && applicantType == ApplicantType.PRIVATPERSON) {
                Surface(
                    color = StatusGreenBg,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚡ By automatisk fundet: $city",
                        color = StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Field: E-mail
            Text(
                text = "E-mailadresse *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PvsTextPrimary
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    showError = false
                },
                placeholder = { Text("F.eks. maria@eksempel.dk") },
                isError = email.isNotBlank() && !isEmailValid(email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            if (email.isNotBlank() && !isEmailValid(email)) {
                Text(
                    text = "⚠️ Indtast et gyldigt e-mail format (f.eks. navn@domæne.dk)",
                    fontSize = 11.sp,
                    color = StatusRed,
                    fontWeight = FontWeight.SemiBold
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
                        val isCvrOk = applicantType == ApplicantType.PRIVATPERSON || CvrRegister.isValidCvr(cvrNumber)
                        if (name.isBlank() || street.isBlank() || postalCode.isBlank() || city.isBlank() || !isEmailValid(email) || !isCvrOk) {
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
                    Text("Validering ➔", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
