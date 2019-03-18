package app.com.android.ihsteachers.main.dialogs

import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.TextView
import app.com.android.ihsteachers.R
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView

class EmailDialog(ui: AnkoContext<View>) {

    lateinit var dialog: DialogInterface
    lateinit var subjectText: TextInputEditText
    lateinit var messageText: TextInputEditText
    lateinit var cancelButton: TextView
    lateinit var okButton: TextView

    init {
        with(ui) {
            dialog = alert {

                customView {

                    verticalLayout {

                        padding = dip(16)

                        textInputLayout {
                            hint = context.getString(R.string.hint_subject)
                            subjectText = textInputEditText(R.style.EmailInputTextStyle) {
                                hintTextColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidLightGray)
                                lines = 1

                            }
                        }.lparams(width = matchParent, height = wrapContent)

                        textInputLayout {
                            hint = context.getString(R.string.hint_message)
                            topPadding = 10
                            messageText = textInputEditText(R.style.EmailInputTextStyle) {
                                hintTextColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidLightGray)
                                lines = 5

                            }
                        }.lparams(width = matchParent, height = wrapContent)

                    /*    themedEditText(R.style.MyEditTextStyle) {
                            textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorSuperDarkGray)
                            hint = context.getString(R.string.hint_subject)
                            hintTextColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidGray)
                            textSize = 16f
                            ems = 10
                            lines = 1

                        }.lparams(width = matchParent, height = wrapContent)

                        themedEditText(R.style.MyEditTextStyle) {
                            textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorSuperDarkGray)
                            hint = context.getString(R.string.hint_message)
                            hintTextColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidGray)
                            textSize = 16f
                            ems = 10
                            lines = 5
                            topPadding = 10


                        }.lparams(width = matchParent, height = wrapContent)  */

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            topPadding = 15
                            horizontalGravity = Gravity.END

                            cancelButton = textView {
                               text = context.getString(android.R.string.cancel)
                                textSize = 16f
                                textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorLightBlue)
                              /*  setOnClickListener {
                                    dialog.cancel()
                                }*/
                            }.lparams {marginEnd = 10}

                            okButton = textView {
                                text = context.getString(android.R.string.ok)
                            //    padding = dip(5)
                                textSize = 16f
                                textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorLightBlue)

                              /*  setOnClickListener {
                                    dialog.dismiss()
                                } */
                            }
                        }

                    }

                }
                /*     customView {
                         verticalLayout {



                         //    padding = dip(16)

                             textInputLayout {
                                 hint = context.getString(R.string.hint_subject)
                                 textColorHint = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidGray)
                                 subjectText = textInputEditText {
                                     textSize = 16f
                                     textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorSuperDarkGray)
                                     backgroundTint = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidGray)

                                 }
                             }.lparams(width = matchParent, topMargin = dip(7)){}

                             textInputLayout {
                                 hint = context.getString(R.string.hint_message)
                                 textColorHint = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidGray)
                                 messageText = textInputEditText {
                                     textSize = 16f
                                     textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorSuperDarkGray)
                                     backgroundTint = android.support.v4.content.ContextCompat.getColor(context, R.color.colorMidGray)
                                     lines = 5
                                 }
                             }.lparams(width = org.jetbrains.anko.matchParent, topMargin = dip(15)){}

                             horizontalLayout {

                                 cancelButton = android.widget.TextView {
                                     text = context.getString(android.R.string.cancel)
                                     padding = dip(5)
                                     textSize = 17f
                                     textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorSuperDarkGray)
                                 }

                                 okButton = android.widget.TextView {
                                     text = context.getString(android.R.string.ok)
                                     padding = dip(5)
                                     textSize = 17f
                                     textColor = android.support.v4.content.ContextCompat.getColor(context, R.color.colorSuperDarkGray)
                                 }

                             }.lparams(width = org.jetbrains.anko.matchParent, height = org.jetbrains.anko.wrapContent)

                         }
                     }*/
            }.show()
        }
    }

}

inline fun ViewManager.textInputEditText(theme: Int = 0, init: TextInputEditText.() -> Unit) =
        ankoView({ TextInputEditText(it) }, theme, init)
inline fun ViewManager.textInputLayout(theme: Int = 0, init: TextInputLayout.() -> Unit) =
        ankoView({ TextInputLayout(it) }, theme, init)