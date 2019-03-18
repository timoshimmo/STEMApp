package app.com.android.ihsteachers.login.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.jetbrains.anko.toast
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val inputEmail: EditText = findViewById(R.id.txtForgottenPasswordInput)
        val btnSend: Button = findViewById(R.id.btnForgottenPasswordSend)

        progressBar = findViewById(R.id.progressBarForgotPass)

        btnSend.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            attemptForgotPassword(inputEmail)
        }

    }

    private fun editForgotPassword(strUname: String) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "editForgotPassword.php"
        val params = JSONObject()

        params.put("username", strUname)

        apiController.post(path, params) {response ->

            progressBar!!.visibility = View.GONE

            val obj = JSONObject(response.toString())
            toast(obj.getString("msg"))

        }
    }

    private fun attemptForgotPassword(mUsername: EditText) {

        mUsername.error = null

        val username = mUsername.text.toString()

        toast(username)

        var cancel = false
        lateinit var focusView: View

        if (TextUtils.isEmpty(username)) {
            mUsername.error = getString(R.string.field_required_error)
            focusView = mUsername
            cancel = true
        }

        if (!TextUtils.isEmpty(username) && !isUsernameLengthValid(username)) {
            mUsername.error = "Username must be more than 5 characters"
            focusView = mUsername
            cancel = true
        }

        if(cancel) {
            focusView.requestFocus()
        }
        else {
            editForgotPassword(username)

        }

    }

    private fun isUsernameLengthValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.length > 5
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
