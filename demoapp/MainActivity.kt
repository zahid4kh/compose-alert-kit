import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.alertkit.dialogs.DialogsDemo
import compose.alertkit.ui.theme.ComposeAlertKitLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAlertKitLibraryTheme {
                //ScaffoldDemo()

                DialogsDemo()
            }
        }
    }
}

@Composable
fun ScaffoldDemo() {
    val toastifyState = rememberToastify()
    val (snackbarHostState, snackifyState) = rememberSnackify()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                toastifyState.show("Toast message shown")
            }) {
                Text("Show Toast")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                snackifyState.show("Basic Snackbar shown")
            }) {
                Text("Show Basic Snackbar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    snackifyState.show(
                        message = "Snackbar with action",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Indefinite, // Stay visible until dismissed
                        onAction = {
                            toastifyState.show("Action performed!")
                        },
                        onDismiss = {
                            toastifyState.show("Snackbar dismissed")
                        }
                    )
                }) {
                    Text("Show Persistent Snackbar")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    // Manually dismiss the current snackbar
                    if (snackifyState.dismiss()) {
                        toastifyState.show("Snackbar dismissed manually")
                    } else {
                        toastifyState.show("No snackbar to dismiss")
                    }
                }) {
                    Text("Dismiss Snackbar")
                }
            }
        }
    }
}