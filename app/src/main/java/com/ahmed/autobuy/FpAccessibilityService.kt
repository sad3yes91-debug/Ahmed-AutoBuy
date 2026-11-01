package com.ahmed.autobuy

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FpAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pkg = event?.packageName?.toString() ?: return

        if (pkg == "com.sslwireless.fastpaybusiness") {
            val time = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

            val prefs = getSharedPreferences(
                "AhmedAutoBuyLog",
                MODE_PRIVATE
            )

            val oldLog = prefs.getString("events", "") ?: ""
            val newLog = "تم اكتشاف FP Card Selling - $time\n$oldLog"

            prefs.edit()
                .putString("events", newLog.take(10000))
                .apply()
        }
    }

    override fun onInterrupt() = Unit
}
