val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(12, 16, 12, 16)
            }

            val codeView = TextView(this).apply {
                text = code
                textSize = 18f
            }

            val dateView = TextView(this).apply {
                text = date
                textSize = 13f
            }

            val copy = Button(this).apply {
                text = "نسخ الكود"
                setOnClickListener {
                    val clipboard = getSystemService(CLIPBOARD_SERVICE)
                        as ClipboardManager
                    clipboard.setPrimaryClip(
                        ClipData.newPlainText("Code", code)
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "تم نسخ الكود",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val delete = Button(this).apply {
                text = "حذف"
                setOnClickListener {
                    deleteCode(i)
                }
            }

            card.addView(codeView)
            card.addView(dateView)
            card.addView(copy)
            card.addView(delete)
            logView.addView(card)
        }
    }

    private fun deleteCode(index: Int) {
        val codes = readCodes()
        if (index >= 0 && index < codes.length()) {
            codes.remove(index)
            prefs.edit().putString("codes", codes.toString()).apply()
            refreshCodes()
        }
    }
}
