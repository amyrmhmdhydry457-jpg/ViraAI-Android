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
import android.widget.VideoView
import android.widget.MediaController

class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private lateinit var videoView: VideoView
    private var selectedVideoUri: Uri? = null

    private val pickVideoCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(15, 15, 25)
        window.navigationBarColor = Color.rgb(15, 15, 25)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(20, 25, 20, 20)
            setBackgroundColor(Color.rgb(15, 15, 25))
        }

        val title = TextView(this).apply {
            text = "ViraAI"
            textSize = 34f
            setTextColor(Color.rgb(150, 125, 255))
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "دستیار هوشمند تدوین ویدیو"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 15)
        }

        videoView = VideoView(this)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        val videoHeight = (220 * resources.displayMetrics.density).toInt()

        root.addView(title)
        root.addView(subtitle)

        root.addView(
            videoView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                videoHeight
            ).apply {
                setMargins(0, 10, 0, 10)
            }
        )

        root.addView(makeButton("🎬 انتخاب ویدیو") {
            selectVideo()
        })

        root.addView(makeButton("✂️ ابزارهای تدوین") {
            if (selectedVideoUri == null) {
                showMessage("ابتدا یک ویدیو انتخاب کن.")
            } else {
                showMessage(
                    "ویدیوی تو آماده است. ابزار برش را در مرحله بعد اضافه می‌کنیم."
                )
            }
        })

        root.addView(makeButton("✍️ زیرنویس خودکار") {
            if (selectedVideoUri == null) {
                showMessage("ابتدا یک ویدیو انتخاب کن.")
            } else {
                showMessage(
                    "قابلیت ساخت زیرنویس خودکار هنوز آماده نشده است."
                )
            }
        })

        root.addView(makeButton("🤖 تدوین هوشمند") {
            showMessage(
                "قابلیت تدوین هوشمند را در مراحل بعد اضافه می‌کنیم."
            )
        })

        statusText = TextView(this).apply {
            text = "هنوز ویدیویی انتخاب نکرده‌ای."
            textSize = 15f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(8, 18, 8, 8)
        }

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

        if (
            requestCode == pickVideoCode &&
            resultCode == RESULT_OK &&
            data?.data != null
        ) {
            val uri = data.data!!
            selectedVideoUri = uri

            try {
                videoView.setVideoURI(uri)

                videoView.setOnPreparedListener { player ->
                    player.isLooping = false
                    videoView.start()
                }

                statusText.text =
                    "✅ ویدیو انتخاب شد و آماده پخش است."

                showMessage("ویدیو انتخاب شد؛ در حال آماده‌سازی پخش.")

            } catch (e: Exception) {
                showMessage("پخش ویدیو با مشکل روبه‌رو شد.")
            }
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
                setMargins(0, 6, 0, 6)
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
