package com.ahmed.autobuy

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }
        val title = TextView(this).apply {
            text = "Ahmed AutoBuy"
            textSize = 26f
        }
        val status = TextView(this).apply {
            text = "النسخة التجريبية 0.1\n\nالمرحلة الحالية: فحص واجهة FP Card Selling فقط.\nلن ينفذ التطبيق أي عملية شراء تلقائياً في هذه النسخة."
            textSize = 18f
            setPadding(0, 24, 0, 24)
        }
        layout.addView(title)
        layout.addView(status)
        setContentView(layout)
    }
}
