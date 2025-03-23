# Compose Alert Kit

A lightweight, state-driven library for Jetpack Compose that makes showing toast messages and snackbars simple and intuitive. It provides a clean, declarative API that integrates seamlessly with Compose's reactive paradigm.

## Table of Contents

- [Features](#features)
- [Components](#components)
  - [Toastify](#toastify)
  - [Snackify](#snackify)
- [Common Pitfalls](#common-pitfalls)
- [License](#license)

## Features

- 🔄 **Composable API** - Fully integrated with Jetpack Compose's lifecycle
- 🧠 **State-driven** - Uses Compose state to trigger messages naturally
- 🚫 **No duplicates** - Automatically cancels previous messages when showing new ones
- 🔍 **Context-aware** - Automatically retrieves the current context
- 🧩 **Simple integration** - Just a single line of code to get started

## Components

### Toastify

Toastify provides a simple way to show Android Toast messages in a Compose-friendly way.

#### Basic Usage

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

#### Customizing Toast Duration

```kotlin
Button(onClick = {
    toastifyState.show("This is a long toast", Toast.LENGTH_LONG)
}) {
    Text("Show Long Toast")
}
```

### Snackify

Snackify provides a state-driven approach to showing Material 3 Snackbars.

#### Basic Usage

There are two ways to use Snackify, depending on your needs:

1. Create both SnackbarHostState and SnackifyState at once:

```kotlin
@Composable
fun MyScreen() {
    val (snackbarHostState, snackifyState) = rememberSnackify()
    
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Button(onClick = {
                snackifyState.show("This is a snackbar")
            }) {
                Text("Show Snackbar")
            }
        }
    }
}
```

2. Use an existing SnackbarHostState:

```kotlin
@Composable
fun MyApp() {
    // Create a shared SnackbarHostState for the entire app
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        // Content
    }
}

@Composable
fun SomeScreen(snackbarHostState: SnackbarHostState) {
    // Create a SnackifyState connected to the existing host state
    val snackifyState = rememberSnackify(snackbarHostState)
    
    Button(onClick = {
        snackifyState.show("Screen-specific message")
    }) {
        Text("Show Snackbar")
    }
}
```

#### Advanced Usage

You can customize snackbars with actions, duration, and callbacks:

```kotlin
snackifyState.show(
    message = "Item deleted",
    actionLabel = "Undo",
    duration = SnackbarDuration.Long,
    onAction = { /* Restore the item */ },
    onDismiss = { /* Clean up */ }
)
```

#### Manual Dismissal

```kotlin
Button(onClick = {
    snackifyState.dismiss()
}) {
    Text("Dismiss Snackbar")
}
```

## Common Pitfalls

### ❌ Direct Instantiation

**Don't do this:**
```kotlin
// This won't work! The toast will never show.
ToastifyState().show("This won't be visible")
```

**Why it fails:** The state needs to be remembered by Compose and connected to a LaunchedEffect that actually shows the toast/snackbar. Direct instantiation bypasses this mechanism.

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
            toastifyState.show("App started")
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


