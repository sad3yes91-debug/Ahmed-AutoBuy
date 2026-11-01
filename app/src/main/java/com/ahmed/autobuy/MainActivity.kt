
package com.ahmed.autobuy

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import android.text.TextWatcher
import android.text.Editable
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var typeGroup: RadioGroup
    private lateinit var codeInput: EditText
    private lateinit var searchInput: EditText
    private lateinit var countText: TextView
    private lateinit var codesLayout: LinearLayout

    // سجل جديد مستقل عن الأكواد القديمة
    private val prefs by lazy {
        getSharedPreferences("AhmedCodesV2", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(20, 20, 20, 20)

        val title = TextView(this)
        title.text = "Ahmed AutoBuy"
        title.textSize = 26f
        title.gravity = Gravity.CENTER
        title.setPadding(0, 10, 0, 10)

        val subtitle = TextView(this)
        subtitle.text = "إدارة الأكواد الرقمية - فئة 10$"
        subtitle.textSize = 17f
        subtitle.gravity = Gravity.CENTER
        subtitle.setPadding(0, 5, 0, 15)

        typeGroup = RadioGroup(this)
        typeGroup.orientation = RadioGroup.HORIZONTAL
        typeGroup.gravity = Gravity.CENTER

        val psRadio = RadioButton(this)
        psRadio.id = View.generateViewId()
        psRadio.text = "PlayStation"
        psRadio.textSize = 16f
        psRadio.isChecked = true

        val robloxRadio = RadioButton(this)
        robloxRadio.id = View.generateViewId()
        robloxRadio.text = "Roblox"
        robloxRadio.textSize = 16f

        typeGroup.addView(psRadio)
        typeGroup.addView(robloxRadio)

        codeInput = EditText(this)
        codeInput.hint = "أدخل كود 10$"
        codeInput.textSize = 16f

        val saveButton = Button(this)
        saveButton.text = "حفظ الكود"
        saveButton.setOnClickListener {
            saveCode()
        }

        countText = TextView(this)
        countText.textSize = 17f
        countText.setPadding(0, 15, 0, 15)
        countText.gravity = Gravity.CENTER

        searchInput = EditText(this)
        searchInput.hint = "بحث عن كود..."
        searchInput.textSize = 15f

        codesLayout = LinearLayout(this)
        codesLayout.orientation = LinearLayout.VERTICAL

        val scroll = ScrollView(this)
        scroll.addView(codesLayout)

        root.addView(title)
        root.addView(subtitle)
        root.addView(typeGroup)
        root.addView(codeInput)
        root.addView(saveButton)
        root.addView(countText)
        root.addView(searchInput)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int,
                before: Int, count: Int
            ) {
                refreshCodes()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        refreshCodes()
    }

    private fun selectedType(): String {
        return if (typeGroup.checkedRadioButtonId ==
            typeGroup.getChildAt(0).id
        ) {
            "PlayStation"
        } else {
            "Roblox"
        }
    }

    private fun readCodes(): JSONArray {
        return try {
            JSONArray(prefs.getString("codes", "[]"))
        } catch (e: Exception) {
            JSONArray()
        }
    }

    private fun saveCode() {
        val code = codeInput.text.toString().trim()

        if (code.isEmpty()) {
            Toast.makeText(
                this, "أدخل الكود أولاً",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val codes = readCodes()

        val item = JSONObject()
        item.put("code", code)
        item.put("type", selectedType())
        item.put("price", "10$")
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
        searchInput.text.clear()
        refreshCodes()

        Toast.makeText(
            this, "تم حفظ الكود بنجاح",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun refreshCodes() {
        codesLayout.removeAllViews()

        val codes = readCodes()
        var psCount = 0
        var robloxCount = 0

        val search = searchInput.text.toString().trim()

        for (i in 0 until codes.length()) {
            val item = codes.optJSONObject(i) ?: continue

            when (item.optString("type")) {
                "PlayStation" -> psCount++
                "Roblox" -> robloxCount++
            }
        }

        countText.text =
            "PlayStation 10$: $psCount\n" +
            "Roblox 10$: $robloxCount\n" +
            "المجموع: ${codes.length()}"

        for (i in codes.length() - 1 downTo 0) {
            val item = codes.optJSONObject(i) ?: continue

            val code = item.optString("code")
            val type = item.optString("type")
            val date = item.optString("date")

            if (search.isNotEmpty() &&
                !code.contains(search, ignoreCase = true) &&
                !type.contains(search, ignoreCase = true)
            ) {
                continue
            }

            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(12, 12, 12, 12)

            val info = TextView(this)
            info.text =
                "$type - 10$\nالكود: $code\nالتاريخ: $date"
            info.textSize = 15f

            val buttons = LinearLayout(this)
            buttons.orientation = LinearLayout.HORIZONTAL

            val copyButton = Button(this)
            copyButton.text = "نسخ"

            copyButton.setOnClickListener {
                val clipboard = getSystemService(
                    Context.CLIPBOARD_SERVICE
                ) as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText("Code", code)
                )

                Toast.makeText(
                    this, "تم نسخ الكود",
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

            card.addView(info)
            card.addView(buttons)
            codesLayout.addView(card)

            val divider = View(this)
            divider.setBackgroundColor(0xFFCCCCCC.toInt())

            codesLayout.addView(
                divider,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
                )
            )
        }
    }
}
