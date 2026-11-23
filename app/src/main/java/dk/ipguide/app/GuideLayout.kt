package dk.ipguide.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val GuideBlue = Color(0xFF123B68)
private val GuideBackground = Color(0xFFF7F9FC)
private val GuideText = Color(0xFF172B42)

@Composable
fun GuideLayout(
    title: String,
    actionText: String,
    onAction: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuideBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = GuideBlue
            )

            CompositionLocalProviderForGuideContent(content)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GuideBlue,
                contentColor = Color.White
            )
        ) {
            Text(actionText)
        }
    }
}

@Composable
private fun ColumnScope.CompositionLocalProviderForGuideContent(
    content: @Composable ColumnScope.() -> Unit
) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalContentColor provides GuideText
    ) {
        content()
    }
}