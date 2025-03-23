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