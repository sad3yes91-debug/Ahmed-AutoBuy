val copyButton = Button(this)
            copyButton.text = "نسخ"
            copyButton.setOnClickListener {
                val clipboard =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText("Code", code)
                )

                Toast.makeText(this, "تم نسخ الكود", Toast.LENGTH_SHORT).show()
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
