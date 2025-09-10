package com.example.appsicredi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SicrediApp()
        }
    }
}

@Composable
fun SicrediApp() {
    var screen by remember { mutableStateOf(Screen.Login) }
    var name by remember { mutableStateOf("") }
    var prefs by remember { mutableStateOf(UserPrefs()) }

    // Aplica claro/escuro simples baseado na preferência
    val colorScheme = if (prefs.darkMode) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(Modifier.fillMaxSize()) {
            when (screen) {
                Screen.Login -> LoginScreen(
                    name = name,
                    onNameChange = { name = it },
                    onContinue = { if (name.isNotBlank()) screen = Screen.Welcome }
                )
                Screen.Welcome -> WelcomeScreen(
                    name = name,
                    onGoToSettings = { screen = Screen.Settings },
                    onLogout = { name = ""; screen = Screen.Login }
                )
                Screen.Settings -> SettingsScreen(
                    prefs = prefs,
                    onPrefsChange = { prefs = it },
                    onBack = { screen = Screen.Welcome }
                )
            }
        }
    }
}

enum class Screen { Login, Welcome, Settings }

data class UserPrefs(
    val darkMode: Boolean = false,
    val notifications: Boolean = true
)

@Composable
fun LoginScreen(
    name: String,
    onNameChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            singleLine = true,
            label = { Text("Seu nome") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onContinue() }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onContinue, enabled = name.isNotBlank(), modifier = Modifier.fillMaxWidth()) {
            Text("Entrar")
        }
    }
}

@Composable
fun WelcomeScreen(
    name: String,
    onGoToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo(a), $name!", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onGoToSettings, modifier = Modifier.fillMaxWidth()) {
            Text("Configurações")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Sair")
        }
    }
}

@Composable
fun SettingsScreen(
    prefs: UserPrefs,
    onPrefsChange: (UserPrefs) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Configurações", style = MaterialTheme.typography.headlineSmall)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tema escuro", modifier = Modifier.weight(1f))
            Switch(
                checked = prefs.darkMode,
                onCheckedChange = { onPrefsChange(prefs.copy(darkMode = it)) }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Notificações", modifier = Modifier.weight(1f))
            Switch(
                checked = prefs.notifications,
                onCheckedChange = { onPrefsChange(prefs.copy(notifications = it)) }
            )
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun SicrediPreview() {
    SicrediApp()
}


