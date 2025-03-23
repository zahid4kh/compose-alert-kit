# compose-alert-kit

Toastify is a lightweight, composable toast message library for Jetpack Compose that makes showing toast messages simple and state-driven. It provides a clean, declarative API that integrates seamlessly with Compose's reactive paradigm.

## Features

- 🔄 **Composable API** - Fully integrated with Jetpack Compose's lifecycle
- 🧠 **State-driven** - Uses Compose state to trigger toast messages naturally
- 🚫 **No duplicates** - Automatically cancels previous toasts when showing new ones
- 🔍 **Context-aware** - Automatically retrieves the current context
- 🧩 **Simple integration** - Just a single line of code to get started


## Basic Usage

Using Toastify is straightforward:

```kotlin
@Composable
fun MyScreen() {
    // Create a toast state that manages showing toasts
    val toastifyState = rememberToastify()
    
    Button(onClick = {
        toastifyState.show("Button clicked!")
    }) {
        Text("Click Me")
    }
}
```

### Customizing Toast Duration

You can specify the duration of your toast message:

```kotlin
Button(onClick = {
    toastifyState.show("This is a long toast", Toast.LENGTH_LONG)
}) {
    Text("Show Long Toast")
}
```


## Common Pitfalls

### ❌ Direct Instantiation

**Don't do this:**
```kotlin
// This won't work! The toast will never show.
ToastifyState().show("This won't be visible")
```

**Why it fails:** The `ToastState` needs to be remembered by Compose and connected to a LaunchedEffect that actually shows the toast. Direct instantiation bypasses this mechanism.

**Do this instead:**
```kotlin
val toastifyState = rememberToastify()
toastifyState.show("This will work correctly")
```

### ❌ Wrong Scope

**Don't do this:**
```kotlin
@Composable
fun MyApp() {
    val toastifyState = rememberToastify()
    
    LaunchedEffect(Unit) {
        // This toast will only show once when the composition starts
        toastifyState.show("App started")
    }
}
```

**Do this instead:**
```kotlin
@Composable
fun MyApp() {
    val toastifyState = rememberToastify()
    
    var isFirstLaunch by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        if (isFirstLaunch) {
            toastState.show("App started")
            isFirstLaunch = false
        }
    }
}
```


## License

Copyright 2025 Zahid

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at


http://www.apache.org/licenses/LICENSE-2.0


Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
