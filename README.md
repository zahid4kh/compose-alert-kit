# Compose Alert Kit

[![](https://jitpack.io/v/zahid4kh/compose-alert-kit.svg)](https://jitpack.io/#zahid4kh/compose-alert-kit)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.zahid4kh/compose-alert-kit)](https://central.sonatype.com/artifact/io.github.zahid4kh/compose-alert-kit)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A lightweight, state-driven library for Jetpack Compose that makes showing toast messages, snackbars, and dialogs simple and intuitive. It provides a clean, declarative API that integrates seamlessly with Compose's reactive paradigm.

## Table of Contents

- [Installation](#installation)
- [Features](#features)
- [Components](#components)
  - [Toastify](#toastify)
  - [Snackify](#snackify)
  - [Dialogs](#dialogs)
    - [FlashDialog](#flashdialog)
    - [SuccessDialog](#successdialog)
    - [ErrorDialog](#errordialog)
    - [WarningDialog](#warningdialog)
    - [ConfirmationDialog](#confirmationdialog)
    - [LoadingDialog](#loadingdialog)
    - [InputDialog](#inputdialog)
- [Common Pitfalls](#common-pitfalls)
- [License](#license)


# Installation

## Option 1: Using JitPack

### Step 1: Add the JitPack repository to your project's `settings.gradle.kts` file:

  ```kotlin
  dependencyResolutionManagement {
      repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
      repositories {
          mavenCentral()
          maven { url = uri("https://jitpack.io") }
      }
  }
  ```

### Step 2: Add the dependency to your project's **app** level `build.gradle.kts` file:

  ```kotlin
  implementation("com.github.zahid4kh:compose-alert-kit:1.0.0")
  ```

## Option 2: Using Maven Central

Add the dependency to your project's **app** level `build.gradle.kts` file:

  ```kotlin
  implementation("io.github.zahid4kh:compose-alert-kit:1.0.0")
  ```


# Features

- 🔄 **Composable API** - Fully integrated with Jetpack Compose's lifecycle
- 🧠 **State-driven** - Uses Compose state to trigger messages naturally
- 🚫 **No duplicates** - Automatically cancels previous messages when showing new ones
- 🔍 **Context-aware** - Automatically retrieves the current context
- 🧩 **Simple integration** - Just a single line of code to get started

# Components

## Toastify

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
  } ) {
      Text("Show Long Toast")
  }
  ```

## Snackify

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

## Dialogs

The library provides a set of ready-to-use dialog components for common use cases, all with a consistent API and styling.

### `FlashDialog`
  
  A dialog that automatically dismisses after a specified duration, perfect for brief notifications.
  
  ```kotlin
  var showFlashDialog by remember { mutableStateOf(false) }
  
  Button(onClick = { showFlashDialog = true }) {
      Text("Show Flash Dialog")
  }
  
  FlashDialog(
      visible = showFlashDialog,
      message = "This is a flash message",
      icon = { 
          Icon(Icons.Default.Info, contentDescription = "Info") 
      },
      duration = 1500,
      onDismiss = { showFlashDialog = false }
  )
  ```

### `SuccessDialog`

  A dialog for showing success messages that auto-dismisses after a short duration.
  
  ```kotlin
  var showSuccessDialog by remember { mutableStateOf(false) }
  
  Button(onClick = { showSuccessDialog = true }) {
      Text("Show Success Dialog")
  }
  
  SuccessDialog(
      visible = showSuccessDialog,
      message = "Operation completed successfully!",
      onDismiss = { showSuccessDialog = false }
  )
  ```

### `ErrorDialog`

A dialog for displaying error messages with a prominent error icon and dismiss button.

  ```kotlin
  var showErrorDialog by remember { mutableStateOf(false) }
  
  Button(onClick = { showErrorDialog = true }) {
    Text("Show Error Dialog")
  }
  
  ErrorDialog(
    visible = showErrorDialog,
    title = "Error Occurred",
    message = "Something went wrong while processing your request.",
    onDismiss = { showErrorDialog = false }
  )
  ```

### `WarningDialog`

A dialog for showing warning messages with appropriate icon and confirmation.
  
  ```kotlin
  var showWarningDialog by remember { mutableStateOf(false) }
  
  Button(onClick = { showWarningDialog = true }) {
      Text("Show Warning Dialog")
  }
  
  WarningDialog(
      visible = showWarningDialog,
      title = "Warning",
      message = "This action might have unexpected consequences.",
      onDismiss = { showWarningDialog = false }
  )
  ```

### `ConfirmationDialog`

A dialog for asking users to confirm important actions with confirm and cancel options.

  ```kotlin
  var showConfirmationDialog by remember { mutableStateOf(false) }
  val toastifyState = rememberToastify()
  
  Button(onClick = { showConfirmationDialog = true }) {
      Text("Show Confirmation Dialog")
  }
  
  ConfirmationDialog(
      visible = showConfirmationDialog,
      title = "Confirm Action",
      message = "Are you sure you want to proceed with this action?",
      onConfirm = {
          toastifyState.show("Action confirmed")
      },
      onDismiss = { showConfirmationDialog = false }
  )
  ```

### `LoadingDialog`

A dialog showing a progress indicator while an operation is in progress.

  ```kotlin
  var showLoadingDialog by remember { mutableStateOf(false) }
  var isLoading by remember { mutableStateOf(false) }
  
  Button(onClick = {
      if (!isLoading) {
          isLoading = true
          showLoadingDialog = true
          
          // Simulate process completion
          Handler(Looper.getMainLooper()).postDelayed({
              showLoadingDialog = false
              isLoading = false
          }, 3000)
      }
  }) {
      Text(if (!isLoading) "Show Loading Dialog" else "Loading...")
  }
  
  LoadingDialog(
      visible = showLoadingDialog,
      message = "Processing your request...",
      onDismiss = { 
          showLoadingDialog = false
          isLoading = false
      }
  )
  ```

### `InputDialog`

A dialog with a text input field for capturing user input.

  ```kotlin
  var showInputDialog by remember { mutableStateOf(false) }
  val toastifyState = rememberToastify()
  
  Button(onClick = { showInputDialog = true }) {
      Text("Show Input Dialog")
  }
  
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


