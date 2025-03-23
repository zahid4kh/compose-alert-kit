/**
 * Copyright 2025 Zahid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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