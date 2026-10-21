package com.viraai.editor

import android.app.Activity
import android.os.Bundle
import android.net.Uri
import android.content.Intent
import android.provider.OpenableColumns
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private var selectedVideoUri: Uri? = null
    private val pickVideoCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(15, 15, 25)
        window.navigationBarColor = Color.rgb(15, 15, 25)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(24, 35, 24, 24)
            setBackgroundColor(Color.rgb(15, 15, 25))
        }

        val title = TextView(this).apply {
            text = "ViraAI"
            textSize = 34f
            setTextColor(Color.rgb(125, 100, 255))
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "دستیار هوشمند تدوین ویدیو"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        statusText = TextView(this).apply {
            text = "هنوز ویدیویی انتخاب نکرده‌ای."
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(12, 25, 12, 25)
        }

        root.addView(title)
        root.addView(subtitle)

        root.addView(makeButton("🎬 انتخاب ویدیو") {
            selectVideo()
        })

        root.addView(makeButton("✍️ زیرنویس خودکار") {
            if (selectedVideoUri == null) {
                showMessage("ابتدا یک ویدیو انتخاب کن.")
            } else {
                showMessage(
                    "ویدیو انتخاب شده؛ قابلیت زیرنویس خودکار را در مرحله بعد اضافه می‌کنیم."
                )
            }
        })

        root.addView(makeButton("🤖 ادیت هوشمند") {
            if (selectedVideoUri == null) {
                showMessage("ابتدا یک ویدیو انتخاب کن.")
            } else {
                showMessage(
                    "ویدیو آماده است؛ قابلیت ادیت هوشمند را در مرحله بعد اضافه می‌کنیم."
                )
            }
        })

        root.addView(makeButton("📁 پروژه‌های من") {
            showMessage(
                "قابلیت ذخیره و مدیریت پروژه‌ها را در مرحله بعد اضافه می‌کنیم."
            )
        })

        root.addView(
            statusText,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)
    }

    private fun selectVideo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
        }

        startActivityForResult(intent, pickVideoCode)
    }

    @Deprecated("Deprecated in Android API")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickVideoCode &&
            resultCode == RESULT_OK &&
            data?.data != null
        ) {
            selectedVideoUri = data.data!!

            val fileName = getFileName(selectedVideoUri!!)

            statusText.text =
                "✅ ویدیو انتخاب شد\n\nنام فایل:\n$fileName\n\nآماده برای مراحل بعدی!"

            showMessage("ویدیو با موفقیت انتخاب شد.")
        }
    }

    private fun makeButton(
        label: String,
        action: () -> Unit
    ): Button {
        return Button(this).apply {
            text = label
            textSize = 16f
            isAllCaps = false
            setOnClickListener { action() }

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 12, 0, 4)
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var name = "video"

        contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(
                    OpenableColumns.DISPLAY_NAME
                )

                if (index >= 0) {
                    name = cursor.getString(index) ?: "video"
                }
            }
        }

        return name
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
