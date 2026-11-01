package com.ahmed.autobuy

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.ScrollView

class MainActivity : Activity() {

    private lateinit var logView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
        }

        val title = TextView(this).apply {
            text = "Ahmed AutoBuy"
            textSize = 26f
        }

        val status = TextView(this).apply {
            text = "سجل مراقبة FP Card Selling"
            textSize = 18f
            setPadding(0, 25, 0, 20)
        }

        val refresh = Button(this).apply {
            text = "تحديث السجل"
            setOnClickListener { loadLog() }
        }

        logView = TextView(this).apply {
            textSize = 16f
            text = "جاري تحميل السجل..."
        }

        val scroll = ScrollView(this).apply {
            addView(logView)
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(refresh)
        layout.addView(scroll)

        setContentView(layout)
        loadLog()
    }

    private fun loadLog() {
        val prefs = getSharedPreferences(
            "AhmedAutoBuyLog",
            MODE_PRIVATE
        )

        val events = prefs.getString("events", "") ?: ""

        logView.text = if (events.isBlank()) {
            "لا توجد أحداث مسجلة حتى الآن.\n\nافتح FP Card Selling ثم ارجع واضغط تحديث السجل."
        } else {
            events
        }
    }
}
