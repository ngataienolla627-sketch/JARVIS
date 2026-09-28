package com.manifestquietly.jarvis

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val layout = LinearLayout(this)
    layout.orientation = LinearLayout.VERTICAL
    layout.setPadding(40, 80, 40, 40)

    val title = TextView(this)
    title.text = "J.A.R.V.I.S."
    title.textSize = 32f

    val status = TextView(this)
    status.text = "JARVIS ONLINE"
    status.textSize = 20f

    val button = Button(this)
    button.text = "🎤 SPEAK"

    button.setOnClickListener {
        status.text = "LISTENING..."
    }

    layout.addView(title)
    layout.addView(status)
    layout.addView(button)

    setContentView(layout)
}
```

}
