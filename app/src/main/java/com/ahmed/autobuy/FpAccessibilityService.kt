
package com.ahmed.autobuy

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class FpAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pkg = event?.packageName?.toString() ?: return

        if (pkg == "com.sslwireless.fastpaybusiness") {
            Log.i(
                "AhmedAutoBuy",
                "FP Card Selling screen event: ${event.eventType}"
            )
        }
    }

    override fun onInterrupt() {
        // لا يوجد إجراء عند المقاطعة
    }
}
