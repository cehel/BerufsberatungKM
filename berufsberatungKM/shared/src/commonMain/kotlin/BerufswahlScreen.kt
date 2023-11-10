import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

// SharedModule.kt
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BerufswahlScreen() {

    val viewModel = getViewModel(Unit, viewModelFactory { BerufswahlViewModel() })

    var inputText by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val recommendation by viewModel.recommendation.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Text Field
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // LocalSoftwareKeyboardController.current.hide()
                }
            ),
            label = { Text("Gib deine Interessen ein") }
        )

        // Button
        Button(
            onClick = {
                if (inputText.isNotBlank()) {
                    viewModel.askChatGPT(inputText)
                } else {
                    showErrorDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                )
            } else {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Finde Berufe für mich")
            }
        }

        // Loading indicator
        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Recommendation or error
        if (!isLoading && recommendation != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("$recommendation")
        } else if (!isLoading && error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Fehler: $error", color = Color.Red)
        }

        // Error dialog
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = {
                    Text("Error")
                },
                text = {
                    Text("Bitte gib deine Interessen ein, bevor du auf 'Finde Berufe für mich' klickst.")
                },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewChatGPTScreen() {
//    FeatherAndroidTasksTheme {
//        // You can mock your ViewModel and pass it here for preview
//        ChatGPTScreen()
//    }
//}


expect fun getPlatformName(): String
