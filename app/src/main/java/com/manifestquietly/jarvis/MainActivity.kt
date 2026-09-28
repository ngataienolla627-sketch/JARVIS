package com.manifestquietly.jarvis

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

class MainActivity : ComponentActivity() {

```
private var speechRecognizer: SpeechRecognizer? = null
private var textToSpeech: TextToSpeech? = null

private val microphonePermission =
    registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startListening()
        }
    }

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    textToSpeech = TextToSpeech(this) {
        textToSpeech?.language = Locale.US
    }

    setContent {
        var status by remember {
            mutableStateOf("JARVIS ONLINE")
        }

        var spokenText by remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("J.A.R.V.I.S.")

            Text(
                text = status,
                modifier = Modifier.padding(30.dp)
            )

            Text(
                text = spokenText,
                modifier = Modifier.padding(20.dp)
            )

            Button(
                onClick = {
                    if (
                        checkSelfPermission(
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        microphonePermission.launch(
                            Manifest.permission.RECORD_AUDIO
                        )
                    } else {
                        startListening()
                    }
                }
            ) {
                Text("🎤 SPEAK")
            }
        }
    }
}

private fun startListening() {

    if (!SpeechRecognizer.isRecognitionAvailable(this)) {
        speak("Speech recognition is not available.")
        return
    }

    speechRecognizer?.destroy()

    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

    val intent = RecognizerIntent().apply {
        action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.US
        )
    }

    speechRecognizer?.setRecognitionListener(
        object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                speak("I didn't catch that.")
            }

            override fun onResults(results: Bundle?) {

                val matches =
                    results?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )

                val command =
                    matches?.firstOrNull() ?: return

                processCommand(command)
            }

            override fun onPartialResults(
                partialResults: Bundle?
            ) {}

            override fun onEvent(
                eventType: Int,
                params: Bundle?
            ) {}
        }
    )

    speechRecognizer?.startListening(intent)
}

private fun processCommand(command: String) {

    val lower = command.lowercase()

    when {

        lower.contains("hello jarvis") ||
        lower == "hello" -> {
            speak("At your service. How may I assist you?")
        }

        lower.contains("what time") ||
        lower.contains("time is it") -> {

            val time =
                java.text.SimpleDateFormat(
                    "h:mm a",
                    Locale.getDefault()
                ).format(
                    java.util.Date()
                )

            speak("The time is $time.")
        }

        lower.contains("what date") ||
        lower.contains("today's date") -> {

            val date =
                java.text.SimpleDateFormat(
                    "EEEE, MMMM d, yyyy",
                    Locale.getDefault()
                ).format(
                    java.util.Date()
                )

            speak("Today is $date.")
        }

        else -> {
            speak(
                "I heard you say $command. " +
                "That command is not programmed yet."
            )
        }
    }
}

private fun speak(message: String) {

    textToSpeech?.speak(
        message,
        TextToSpeech.QUEUE_FLUSH,
        null,
        "JARVIS"
    )
}

override fun onDestroy() {
    speechRecognizer?.destroy()
    textToSpeech?.stop()
    textToSpeech?.shutdown()
    super.onDestroy()
}
```

}
