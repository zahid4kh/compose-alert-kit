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

package compose.alertkit.toastify

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

/**
 * A state holder for managing the display of Toast messages within Compose.
 *
 * @param _message The message to be displayed in the Toast.
 * @param _duration The duration of the Toast (either Toast.LENGTH_SHORT or Toast.LENGTH_LONG).
 * @property message The current message to be displayed, or null if no message is pending.
 * @property duration The current duration for the Toast.
 * @property show Sets the message and duration to be shown in a Toast.
 * @property reset Resets the Toast state, clearing the current message.
 */
class ToastifyState {
    /**
     * The message to be displayed in the Toast. Null if no Toast should be displayed.
     */
    private var _message by mutableStateOf<String?>(null)

    /**
     * The duration of the Toast (either Toast.LENGTH_SHORT or Toast.LENGTH_LONG).
     */
    private var _duration by mutableIntStateOf(Toast.LENGTH_SHORT)

    /**
     * The current message to be displayed, or null if no message is pending.
     */
    val message: String? get() = _message

    /**
     * The current duration for the Toast.
     */
    val duration: Int get() = _duration

    /**
     * Sets the message and duration to be shown in a Toast.
     *
     * @param message The message to display.
     * @param duration The duration of the Toast (default is Toast.LENGTH_SHORT).
     */
    fun show(message: String, duration: Int = Toast.LENGTH_SHORT) {
        _message = message
        _duration = duration
    }

    /**
     * Resets the ToastifyState state, clearing the current message.
     *
     * This is called internally after a Toast has been shown.
     */
    internal fun reset() {
        _message = null
    }
}

/**
 * Remembers and creates a [ToastifyState] instance for managing Toast messages in Compose.
 *
 * This function provides a state-aware way to show Toasts within your composable functions.
 * It uses [LaunchedEffect] to observe changes to the `message` property of the [ToastifyState]
 * and displays a Toast when a non-null message is set.
 *
 * @return A [ToastifyState] instance.
 */
@Composable
fun rememberToastify(): ToastifyState {
    val context = LocalContext.current
    // Create and remember the ToastifyState instance
    val toastifyState = remember { ToastifyState() }

    // Get the current message from the ToastifyState
    val message = toastifyState.message

    // LaunchedEffect to show the Toast when the message changes
    LaunchedEffect(message) {
        if (message != null) {
            // Show the Toast using ToastifyManager
            ToastifyManager.showToast(context, message, toastifyState.duration)
            // Reset the state after showing the Toast
            toastifyState.reset()
        }
    }
    return toastifyState
}