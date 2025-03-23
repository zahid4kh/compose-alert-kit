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

import android.content.Context
import android.widget.Toast

/**
 * Manages the display of Toast messages, ensuring that only one Toast is shown at a time.
 */
object ToastifyManager {
    /**
     * The currently displayed Toast, or null if no Toast is being displayed.
     */
    private var currentToast: Toast? = null

    /**
     * Displays a Toast message to the user.
     *
     * If another Toast is currently being displayed, it will be cancelled before the new Toast is shown.
     *
     * @param context The application context.
     * @param message The message to display in the Toast.
     * @param duration The duration of the Toast (either Toast.LENGTH_SHORT or Toast.LENGTH_LONG).
     */
    fun showToast(context: Context, message: String, duration: Int) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context, message, duration).apply { show() }
    }
}