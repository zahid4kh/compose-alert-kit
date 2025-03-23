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

package com.zahid.alertkit.snackify

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * A state holder for managing the display of Snackbar messages within Compose Material 3.
 * @property message The current message to be displayed, or null if no message is pending.
 * @property actionLabel The current action label to be displayed, or null if no action is pending.
 * @property duration The current duration for the Snackbar.
 * @property onAction The current action to be performed when the action label is clicked.
 * @property onDismiss The current action to be performed when the Snackbar is dismissed.
 * @property show Sets the message and other optional parameters to be shown in a Snackbar.
 * @property dismiss Dismisses the currently showing snackbar, if any.
 */
class SnackifyState {
    /**
     * The message to be displayed in the Snackbar. Null if no Snackbar should be displayed.
     */
    private var _message by mutableStateOf<String?>(null)

    /**
     * The action label to be displayed in the Snackbar. Null if no action should be displayed.
     */
    private var _actionLabel by mutableStateOf<String?>(null)

    /**
     * The duration of the Snackbar.
     */
    private var _duration by mutableStateOf(SnackbarDuration.Short)

    /**
     * The action to be performed when the action label is clicked.
     */
    private var _onAction by mutableStateOf<() -> Unit>({})

    /**
     * The action to be performed when the Snackbar is dismissed.
     */
    private var _onDismiss by mutableStateOf<() -> Unit>({})

    /**
     * Reference to the associated SnackbarHostState to allow direct dismissal
     */
    internal var hostState: SnackbarHostState? = null

    /**
     * The current message to be displayed, or null if no message is pending.
     */
    val message: String? get() = _message

    /**
     * The current action label to be displayed, or null if no action is pending.
     */
    val actionLabel: String? get() = _actionLabel

    /**
     * The current duration for the Snackbar.
     */
    val duration: SnackbarDuration get() = _duration

    /**
     * The current action to be performed when the action label is clicked.
     */
    val onAction: () -> Unit get() = _onAction

    /**
     * The current action to be performed when the Snackbar is dismissed.
     */
    val onDismiss: () -> Unit get() = _onDismiss

    /**
     * Sets the message and other optional parameters to be shown in a Snackbar.
     *
     * @param message The message to display.
     * @param actionLabel The action label to display (default is null).
     * @param duration The duration of the Snackbar (default is SnackbarDuration.Short).
     * @param onAction The action to perform when the action label is clicked (default is empty).
     * @param onDismiss The action to perform when the Snackbar is dismissed (default is empty).
     */
    fun show(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onAction: () -> Unit = {},
        onDismiss: () -> Unit = {}
    ) {
        _message = message
        _actionLabel = actionLabel
        _duration = duration
        _onAction = onAction
        _onDismiss = onDismiss
    }

    /**
     * Dismisses the currently showing snackbar, if any.
     *
     * @return true if a snackbar was dismissed, false otherwise
     */
    fun dismiss(): Boolean {
        return if (hostState?.currentSnackbarData != null) {
            hostState?.currentSnackbarData?.dismiss()
            true
        } else {
            false
        }
    }

    /**
     * Resets the Snackbar state, clearing the current message and actions.
     */
    internal fun reset() {
        _message = null
        _actionLabel = null
        _onAction = {}
        _onDismiss = {}
    }
}

/**
 * Creates and remembers both a SnackbarHostState and SnackifyState for easy integration
 * with Material 3 Scaffold.
 *
 * @return A Pair containing the SnackbarHostState and SnackifyState
 */
@Composable
fun rememberSnackify(): Pair<SnackbarHostState, SnackifyState> {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackifyState = remember { SnackifyState() }

    snackifyState.hostState = snackbarHostState

    val message = snackifyState.message

    LaunchedEffect(message) {
        if (message != null) {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = snackifyState.actionLabel,
                duration = snackifyState.duration,
                withDismissAction = true // explicit dismiss action
            )

            when (result) {
                SnackbarResult.ActionPerformed -> snackifyState.onAction()
                SnackbarResult.Dismissed -> snackifyState.onDismiss()
            }

            snackifyState.reset()
        }
    }

    return Pair(snackbarHostState, snackifyState)
}

/**
 * Creates and remembers a SnackifyState connected to an existing SnackbarHostState.
 *
 * @param snackbarHostState The existing SnackbarHostState to use
 * @return A SnackifyState instance
 */
@Composable
fun rememberSnackify(snackbarHostState: SnackbarHostState): SnackifyState {
    val snackifyState = remember { SnackifyState() }

    snackifyState.hostState = snackbarHostState

    val message = snackifyState.message

    LaunchedEffect(message) {
        if (message != null) {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = snackifyState.actionLabel,
                duration = snackifyState.duration,
                withDismissAction = true // explicit dismiss action
            )

            when (result) {
                SnackbarResult.ActionPerformed -> snackifyState.onAction()
                SnackbarResult.Dismissed -> snackifyState.onDismiss()
            }

            snackifyState.reset()
        }
    }
    return snackifyState
}