package dk.ipguide.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun HomeScreen(
    savedDrafts: List<ApplicationDraft>,
    onStartNewApplication: (IpCategory) -> Unit,
    onOpenDraft: (ApplicationDraft) -> Unit,
    onShowIpRights: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(IpCategory.VAREMAERKE) }
    var showDraftsDialog by remember { mutableStateOf(false) }

    if (showDraftsDialog) {
        AlertDialog(
            onDismissRequest = { showDraftsDialog = false },
            confirmButton = {
                TextButton(onClick = { showDraftsDialog = false }) {
                    Text("Luk")
                }
            },
            title = {
                Text("Mine gemte kladder (${savedDrafts.size})", fontWeight = FontWeight.Bold, color = PvsNavy)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (savedDrafts.isEmpty()) {
                        Text("Ingen kladder fundet.")
                    } else {
                        savedDrafts.forEach { draft ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDraftsDialog = false
                                        onOpenDraft(draft)
                                    },
                                colors = CardDefaults.cardColors(containerColor = PvsBlueLight),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(draft.id, fontWeight = FontWeight.Bold, color = PvsNavy, fontSize = 13.sp)
                                        Text(
                                            text = draft.brandName.ifBlank { "[Uden navn]" },
                                            fontSize = 12.sp,
                                            color = PvsTextPrimary
                                        )
                                        Text("Status: ${draft.status.label}", fontSize = 11.sp, color = PvsTextSecondary)
                                    }

                                    Text("Åbn ➔", fontWeight = FontWeight.Bold, color = PvsNavy, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PvsBackground)
    ) {
        PvsTopHeader()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Patent- og Varemærkestyrelsen",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PvsTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Vælg din pakke",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = PvsNavy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Vælg den IP-rettighed, du ønsker at oprette en ansøgning for. Appen guider dig trin for trin.",
                style = MaterialTheme.typography.bodyMedium,
                color = PvsTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Package mulighed liste
            PackageOptionCard(
                icon = "®",
                title = "Varemærke Pakke",
                subtitle = "Firmanavn, logo & kendetegn",
                description = "Beskyt dit navn, logo og brand mod efterligning.",
                badgeText = "Mest populær",
                isSelected = selectedCategory == IpCategory.VAREMAERKE,
                onSelect = { selectedCategory = IpCategory.VAREMAERKE }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PackageOptionCard(
                icon = "💡",
                title = "Patent Pakke",
                subtitle = "Opfindelser & tekniske løsninger",
                description = "Beskyt din tekniske opfindelse og produkt i op til 20 år.",
                isSelected = selectedCategory == IpCategory.PATENT,
                onSelect = { selectedCategory = IpCategory.PATENT }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PackageOptionCard(
                icon = "🎨",
                title = "Design Pakke",
                subtitle = "Visuelt udseende & produktdesign",
                description = "Beskyt dit produkts unikke form, udseende og mønster.",
                isSelected = selectedCategory == IpCategory.DESIGN,
                onSelect = { selectedCategory = IpCategory.DESIGN }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val categoryName = when (selectedCategory) {
                    IpCategory.VAREMAERKE -> "Varemærke"
                    IpCategory.PATENT -> "Patent"
                    IpCategory.DESIGN -> "Design"
                }

                // US01: Start ny ansøgning
                Button(
                    onClick = { onStartNewApplication(selectedCategory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PvsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "➔  Start ${categoryName}ansøgning",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // US06: Gem og genoptag ansøgningskladde
                if (savedDrafts.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { showDraftsDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PvsNavy
                        )
                    ) {
                        Text(
                            text = "Fortsæt eksisterende kladde (${savedDrafts.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                TextButton(onClick = onShowIpRights) {
                    Text(
                        text = "ⓘ  Sammenlign rettigheder (IP Guide)",
                        fontSize = 13.sp,
                        color = PvsNavy,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun PackageOptionCard(
    icon: String,
    title: String,
    subtitle: String,
    description: String,
    badgeText: String? = null,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PvsNavy else PvsCardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PvsBlueLight else Color.White
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) PvsNavy else PvsBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 20.sp,
                    color = if (isSelected) Color.White else PvsNavy,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PvsNavy
                    )

                    if (badgeText != null) {
                        Surface(
                            color = PvsNavy,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = badgeText,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PvsTextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = PvsTextSecondary,
                    lineHeight = 15.sp
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PvsNavy,
                    unselectedColor = PvsTextSecondary
                )
            )
        }
    }
}
