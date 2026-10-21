package com.viraai.editor

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.net.Uri

class MainActivity : Activity() {

    private val pickVideoCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(15, 17, 30)
        window.navigationBarColor = Color.rgb(15, 17, 30)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.gravity = Gravity.CENTER
        root.setPadding(28, 32, 28, 32)
        root.setBackgroundColor(Color.rgb(15, 17, 30))

        val title = TextView(this)
        title.text = "ViraAI"
        title.textSize = 36f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        val subtitle = TextView(this)
        subtitle.text = "دستیار هوشمند تدوین ویدیو"
        subtitle.textSize = 17f
        subtitle.setTextColor(Color.LTGRAY)
        subtitle.gravity = Gravity.CENTER
        subtitle.setPadding(0, 12, 0, 36)

        root.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            subtitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        addButton(root, "🎬 انتخاب ویدیو") {
            selectVideo()
        }

        addButton(root, "✂️ ابزارهای تدوین") {
            Toast.makeText(
                this,
                "ابزارهای تدوین در نسخه‌های بعدی اضافه می‌شوند.",
                Toast.LENGTH_LONG
            ).show()
        }

        addButton(root, "📝 زیرنویس خودکار") {
            Toast.makeText(
                this,
                "قابلیت زیرنویس در حال آماده‌سازی است.",
                Toast.LENGTH_LONG
            ).show()
        }

        addButton(root, "🤖 تدوین هوشمند") {
            Toast.makeText(
                this,
                "قابلیت هوش مصنوعی در مراحل بعدی اضافه می‌شود.",
                Toast.LENGTH_LONG
            ).show()
        }

        setContentView(root)
    }

    private fun addButton(
        root: LinearLayout,
        label: String,
        action: () -> Unit
    ) {
        val button = Button(this)
        button.text = label
        button.textSize = 16f
        button.isAllCaps = false

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 8, 0, 8)

        root.addView(button, params)
        button.setOnClickListener { action() }
    }

    private fun selectVideo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "video/*"

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
            val videoUri: Uri = data.data!!
            Toast.makeText(
                this,
                "ویدیو انتخاب شد؛ آمادهٔ مراحل بعدی تدوین است.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
