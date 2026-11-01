setOnClickListener {
                    val clipboard =
                        getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
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

            val deleteButton = Button(this).apply {
                text = "حذف"
                setOnClickListener {
                    AlertDialog.Builder(this@MainActivity)
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
            }

            buttons.addView(copyButton)
            buttons.addView(deleteButton)

            card.addView(codeText)
            card.addView(dateText)
            card.addView(buttons)

            codesLayout.addView(card)

            val line = View(this).apply {
                setBackgroundColor(0xFFCCCCCC.toInt())
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1
                )
            }
            codesLayout.addView(line)
        }
    }
}
