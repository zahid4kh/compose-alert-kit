package compose.alertkit.dialogs

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * A dialog for showing success messages that auto-dismisses.
 * @param visible Whether the dialog is currently visible.
 * @param message The message to display in the dialog.
 * @param autoDismissDuration The duration in milliseconds to automatically dismiss the dialog.
 * @param onDismiss A callback that is invoked when the user dismisses the dialog.
 */
@Composable
fun SuccessDialog(
    visible: Boolean,
    message: String,
    autoDismissDuration: Long = 2000,
    onDismiss: () -> Unit
) {
    FlashDialog(
        visible = visible,
        message = message,
        duration = autoDismissDuration,
        icon = {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(40.dp)
            )
        },
        onDismiss = onDismiss
    )
}