
package com.ahmed.autobuy

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var codeInput: EditText
    private lateinit var countText: TextView
    private lateinit var codesLayout: LinearLayout

    private val prefs by lazy {
        getSharedPreferences("AhmedCodes", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(24, 24, 24, 24)

        val title = TextView(this)
        title.text = "Ahmed AutoBuy"
        title.textSize = 25f
        title.gravity = Gravity.CENTER

        countText = TextView(this)
        countText.textSize = 18f
        countText.setPadding(0, 20, 0, 20)

        codeInput = EditText(this)
        codeInput.hint = "أدخل كود PlayStation"

        val saveButton = Button(this)
        saveButton.text = "حفظ الكود"
        saveButton.setOnClickListener {
            saveCode()
        }

        codesLayout = LinearLayout(this)
        codesLayout.orientation = LinearLayout.VERTICAL

        val scroll = ScrollView(this)
        scroll.addView(codesLayout)

        root.addView(title)
        root.addView(countText)
        root.addView(codeInput)
        root.addView(saveButton)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
        refreshCodes()
    }

    private fun saveCode() {
        val code = codeInput.text.toString().trim()

        if (code.isEmpty()) {
            Toast.makeText(this, "أدخل الكود أولاً", Toast.LENGTH_SHORT).show()
            return
        }

        val codes = readCodes()

        val item = JSONObject()
        item.put("code", code)
        item.put(
            "date",
            SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())
        )

        codes.put(item)

        prefs.edit()
            .putString("codes", codes.toString())
            .apply()

        codeInput.text.clear()
        refreshCodes()

        Toast.makeText(this, "تم حفظ الكود", Toast.LENGTH_SHORT).show()
    }

    private fun readCodes(): JSONArray {
        return try {
            JSONArray(prefs.getString("codes", "[]"))
        } catch (e: Exception) {
            JSONArray()
        }
    }

    private fun refreshCodes() {
        codesLayout.removeAllViews()

        val codes = readCodes()
        countText.text = "عدد الأكواد المحفوظة: ${codes.length()}"

        for (i in codes.length() - 1 downTo 0) {

            val item = codes.optJSONObject(i) ?: continue
            val code = item.optString("code")
            val date = item.optString("date")

            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(12, 12, 12, 12)

            val codeText = TextView(this)
            codeText.text = "الكود: $code"
            codeText.textSize = 17f

            val dateText = TextView(this)
            dateText.text = "التاريخ: $date"
            dateText.textSize = 13f

            val buttons = LinearLayout(this)
            buttons.orientation = LinearLayout.HORIZONTAL

            val copyButton = Button(this)
            copyButton.text = "نسخ"

            copyButton.setOnClickListener {
                val clipboard =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText("Code", code)
                )

                Toast.makeText(
                    this,
                    "تم نسخ الكود",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val deleteButton = Button(this)
            deleteButton.text = "حذف"

            deleteButton.setOnClickListener {
                AlertDialog.Builder(this)
                    .setMessage("هل تريد حذف هذا الكود؟")
                    .setPositiveButton("حذف") { _, _ ->

                        val updated = readCodes()
                        updated.remove(i)

                        prefs.edit()
                            .putString("codes", updated.toString())
                            .apply()

                        refreshCodes()
                    }
                    .setNegativeButton("إلغاء", null)
                    .show()
            }

            buttons.addView(copyButton)
            buttons.addView(deleteButton)

            card.addView(codeText)
            card.addView(dateText)
            card.addView(buttons)

            codesLayout.addView(card)

            val divider = View(this)
            divider.setBackgroundColor(0xFFCCCCCC.toInt())

            val dividerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
            )

            codesLayout.addView(divider, dividerParams)
        }
    }
}
