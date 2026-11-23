package dk.ipguide.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dk.ipguide.app.ui.theme.IPGuideTheme

enum class Screen {
    HOME,
    STEP_1_OPLYSNINGER,
    STEP_2_KLASSER,
    STEP_3_ANSOGER,
    STEP_4_VALIDERING,
    STEP_5_OVERSIGT,
    IP_RIGHTS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IPGuideTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IpGuideScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun IpGuideScreen(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    val savedDrafts = remember { mutableStateListOf<ApplicationDraft>() }
    var activeDraft by remember { mutableStateOf<ApplicationDraft?>(null) }
    val context = LocalContext.current

    fun saveActiveDraft() {
        activeDraft?.let { draft ->
            val index = savedDrafts.indexOfFirst { it.id == draft.id }
            if (index >= 0) {
                savedDrafts[index] = draft
            } else {
                savedDrafts.add(draft)
            }
            Toast.makeText(context, "Kladde ${draft.id} gemt!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = modifier) {
        when (currentScreen) {
            Screen.HOME -> HomeScreen(
                savedDrafts = savedDrafts,
                onStartNewApplication = { category ->
                    val newDraft = ApplicationDraft(
                        category = category,
                        status = DraftStatus.UNDER_UDARBEJDELSE
                    )
                    savedDrafts.add(newDraft)
                    activeDraft = newDraft
                    currentScreen = Screen.STEP_1_OPLYSNINGER
                },
                onOpenDraft = { draft ->
                    activeDraft = draft
                    currentScreen = Screen.STEP_5_OVERSIGT
                },
                onShowIpRights = { currentScreen = Screen.IP_RIGHTS }
            )

            Screen.STEP_1_OPLYSNINGER -> activeDraft?.let { draft ->
                Step1OplysningerScreen(
                    draft = draft,
                    onBack = { currentScreen = Screen.HOME },
                    onNext = { currentScreen = Screen.STEP_2_KLASSER },
                    onSaveDraft = {
                        saveActiveDraft()
                        currentScreen = Screen.HOME
                    }
                )
            } ?: run { currentScreen = Screen.HOME }

            Screen.STEP_2_KLASSER -> activeDraft?.let { draft ->
                Step2KlasserScreen(
                    draft = draft,
                    onBack = { currentScreen = Screen.STEP_1_OPLYSNINGER },
                    onNext = { currentScreen = Screen.STEP_3_ANSOGER },
                    onSaveDraft = {
                        saveActiveDraft()
                        currentScreen = Screen.HOME
                    }
                )
            } ?: run { currentScreen = Screen.HOME }

            Screen.STEP_3_ANSOGER -> activeDraft?.let { draft ->
                Step3AnsogerScreen(
                    draft = draft,
                    onBack = { currentScreen = Screen.STEP_2_KLASSER },
                    onNext = { currentScreen = Screen.STEP_4_VALIDERING },
                    onSaveDraft = {
                        saveActiveDraft()
                        currentScreen = Screen.HOME
                    }
                )
            } ?: run { currentScreen = Screen.HOME }

            Screen.STEP_4_VALIDERING -> activeDraft?.let { draft ->
                ValideringScreen(
                    draft = draft,
                    onBack = { currentScreen = Screen.STEP_3_ANSOGER },
                    onGoToStep1 = { currentScreen = Screen.STEP_1_OPLYSNINGER },
                    onGoToStep2 = { currentScreen = Screen.STEP_2_KLASSER },
                    onGoToStep3 = { currentScreen = Screen.STEP_3_ANSOGER },
                    onNext = { currentScreen = Screen.STEP_5_OVERSIGT },
                    onSaveDraft = {
                        saveActiveDraft()
                        currentScreen = Screen.HOME
                    }
                )
            } ?: run { currentScreen = Screen.HOME }

            Screen.STEP_5_OVERSIGT -> activeDraft?.let { draft ->
                OversigtScreen(
                    draft = draft,
                    onBack = { currentScreen = Screen.STEP_4_VALIDERING },
                    onGoToStep1 = { currentScreen = Screen.STEP_1_OPLYSNINGER },
                    onGoToStep2 = { currentScreen = Screen.STEP_2_KLASSER },
                    onGoToStep3 = { currentScreen = Screen.STEP_3_ANSOGER },
                    onSaveDraft = {
                        saveActiveDraft()
                        currentScreen = Screen.HOME
                    }
                )
            } ?: run { currentScreen = Screen.HOME }

            Screen.IP_RIGHTS -> IpRightsScreen(
                onBack = { currentScreen = Screen.HOME }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IpGuidePreview() {
    IPGuideTheme {
        IpGuideScreen()
    }
}
