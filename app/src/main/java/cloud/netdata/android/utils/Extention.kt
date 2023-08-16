package cloud.netdata.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import cloud.netdata.android.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatEditText.getVal(): String {
    return this.text.toString().trim()
}

fun AppCompatEditText.rtl(): Int {
    return View.TEXT_DIRECTION_RTL.also { this.textDirection = it }
}

fun AppCompatTextView.rtl(): Int {
    return View.TEXT_DIRECTION_RTL.also { this.textDirection = it }
}

fun AutoCompleteTextView.getVal(): String {
    return this.text.toString().trim()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Fragment?.setAdjustResize() {
    this?.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.setFocus() {
    this.setOnFocusChangeListener { _, event ->
        this.setSelection(this.text.length)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.otp(previousFocus: EditText?, nextFocus: EditText?, activity: Activity? = null) {

    this.setOnTouchListener { _, event ->
        this.onTouchEvent(event)
        this.setSelection(this.text.length)
        true
    }

    this@otp.setOnKeyListener { _, keyCode, event ->

        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && previousFocus != null && this.text.isEmpty()) {
            previousFocus.text = null
            previousFocus.requestFocus()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (count == 1 && nextFocus != null) {
                nextFocus.requestFocus()
            } else if (count == 1 && activity != null) {
                this@otp.isCursorVisible = true
                val view = activity.currentFocus
                if (view != null) {
                    val inputManager =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        view.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })


}

fun Activity.clear(vararg editText: EditText) {
    for (i in editText) {
        i.text.clear()
    }
    editText[0].requestFocus()
    if (this is BaseActivity) {
        (this as BaseActivity).showKeyboard()
    }
}