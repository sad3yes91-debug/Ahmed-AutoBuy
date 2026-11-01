val codeView = TextView(this)
            codeView.text = code
            codeView.textSize = 18f

            val dateView = TextView(this)
            dateView.text = date
            dateView.textSize = 13f

            val copyButton = Button(this)
            copyButton.text = "نسخ الكود"

            copyButton.setOnClickListener {
                val clipboard = getSystemService(
                    Context.CLIPBOARD_SERVICE
                ) as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText("Code", code)
                )

                Toast.makeText(
                    this@MainActivity,
                    "تم نسخ الكود",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val deleteButton = Button(this)
            deleteButton.text = "حذف الكود"

            deleteButton.setOnClickListener {
                deleteCode(i)
            }

            card.addView(codeView)
            card.addView(dateView)
            card.addView(copyButton)
            card.addView(deleteButton)

            logView.addView(card)
        }
    }

    private fun deleteCode(index: Int) {
        val codes = readCodes()

        if (index >= 0 && index < codes.length()) {
            codes.remove(index)

            prefs.edit()
                .putString("codes", codes.toString())
                .apply()

            refreshCodes()
        }
    }
}
