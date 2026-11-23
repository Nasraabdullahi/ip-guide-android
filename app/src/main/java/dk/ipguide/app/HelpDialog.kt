package dk.ipguide.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.ipguide.app.ui.theme.PvsNavy
import dk.ipguide.app.ui.theme.PvsTextSecondary

@Composable
fun HelpDialog(
    title: String,
    description: String,
    example: String? = null,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PvsNavy)
            ) {
                Text("Forstået")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💡 ", fontSize = 18.sp)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PvsNavy
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (example != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF3FA)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Eksempel:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PvsNavy
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = example,
                                fontSize = 12.sp,
                                color = PvsTextSecondary
                            )
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
