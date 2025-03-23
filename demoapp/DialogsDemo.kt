import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import compose.alertkit.rememberToastify

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogsDemo() {
    var showFlashDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showWarningDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showInputDialog by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    val toastifyState = rememberToastify()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dialog Components Demo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DialogTestSection(
                title = "Flash Dialog",
                description = "Auto-dismisses after a short duration",
                buttonText = "Show Flash Dialog"
            ) {
                showFlashDialog = true
            }

            DialogTestSection(
                title = "Success Dialog",
                description = "Shows success message with auto-dismiss",
                buttonText = "Show Success Dialog"
            ) {
                showSuccessDialog = true
            }

            DialogTestSection(
                title = "Error Dialog",
                description = "Shows error message with dismiss button",
                buttonText = "Show Error Dialog"
            ) {
                showErrorDialog = true
            }

            DialogTestSection(
                title = "Warning Dialog",
                description = "Shows warning message with confirmation",
                buttonText = "Show Warning Dialog"
            ) {
                showWarningDialog = true
            }

            DialogTestSection(
                title = "Confirmation Dialog",
                description = "Asks user to confirm an action",
                buttonText = "Show Confirmation Dialog"
            ) {
                showConfirmationDialog = true
            }

            DialogTestSection(
                title = "Loading Dialog",
                description = "Shows loading indicator (simulates 3s process)",
                buttonText = if (!isLoading) "Show Loading Dialog" else "Loading..."
            ) {
                if (!isLoading) {
                    isLoading = true
                    showLoadingDialog = true

                    // Simulate a process completing after 3 seconds
                    val mainThreadHandler = Handler(Looper.getMainLooper())
                    mainThreadHandler.postDelayed({
                        showLoadingDialog = false
                        isLoading = false
                        showSuccessDialog = true
                    }, 3000)
                }
            }

            DialogTestSection(
                title = "Input Dialog",
                description = "Captures text input from user",
                buttonText = "Show Input Dialog"
            ) {
                showInputDialog = true
            }
        }
    }

    // Dialog implementations

    FlashDialog(
        visible = showFlashDialog,
        message = "This is a flash message",
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        duration = 1500,
        onDismiss = { showFlashDialog = false }
    )

    SuccessDialog(
        visible = showSuccessDialog,
        message = "Operation completed successfully!",
        onDismiss = { showSuccessDialog = false }
    )

    ErrorDialog(
        visible = showErrorDialog,
        title = "Error Occurred",
        message = "Something went wrong while processing your request. Please try again later.",
        onDismiss = { showErrorDialog = false }
    )

    WarningDialog(
        visible = showWarningDialog,
        title = "Warning",
        message = "This action might have unexpected consequences. Please proceed with caution.",
        onDismiss = { showWarningDialog = false }
    )

    ConfirmationDialog(
        visible = showConfirmationDialog,
        title = "Confirm Action",
        message = "Are you sure you want to proceed with this action?",
        onConfirm = {
            toastifyState.show("Action confirmed")
        },
        onDismiss = { showConfirmationDialog = false }
    )

    LoadingDialog(
        visible = showLoadingDialog,
        message = "Processing your request...",
        onDismiss = {
            showLoadingDialog = false
            isLoading = false
        }
    )

    InputDialog(
        visible = showInputDialog,
        title = "Enter Information",
        placeholder = "Type something here",
        keyboardType = KeyboardType.Text,
        onConfirm = { input ->
            toastifyState.show("You entered: $input")
        },
        onDismiss = { showInputDialog = false }
    )
}

@Composable
private fun DialogTestSection(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(buttonText)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}