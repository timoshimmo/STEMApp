package app.com.android.ihsteachers.login.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import app.com.android.ihsteachers.R
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.security.MessageDigest
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import app.com.android.ihsteachers.main.activity.MainActivity
import app.com.android.ihsteachers.providers.PreferencesHelper.customPreference
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.providers.PreferencesHelper.bkgImgLogin
import com.squareup.picasso.Picasso
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity() {

    var progressBar: ProgressBar? = null
    val CUSTOM_PREF_NAME = "UserID"
    val CUSTOM_PREF_BKGLOGIN = "BKGIMG_LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUsername.clearFocus()
        txtPassword.clearFocus()
        loginBody.requestFocus()

        progressBar = findViewById(R.id.progressBar)
        val imgBackground : ImageView = findViewById(R.id.imgLoginBackground)

        val prefs = customPreference(this.applicationContext, CUSTOM_PREF_BKGLOGIN)
        val bkgImgLogin = prefs.bkgImgLogin

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val txtUsername: EditText = findViewById(R.id.txtUsername)
        val txtPassword: EditText = findViewById(R.id.txtPassword)
        val txtForgottenPassword: TextView = findViewById(R.id.txtForgotenPassword)

        Picasso.get()
                .load(bkgImgLogin)
                .into(imgBackground)

        txtUsername.setOnFocusChangeListener {
            _, hasFocus ->

            if(hasFocus) {
                appbarLogin.setExpanded(false, true)
            }
            else {
                appbarLogin.setExpanded(true, true)
            }


        }

        txtPassword.setOnFocusChangeListener {
            _, hasFocus ->

            if(hasFocus) {
                appbarLogin.setExpanded(false, true)
            }
            else {
                appbarLogin.setExpanded(true, true)
            }
        }

        txtForgottenPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

      /*  btnBack.setOnClickListener {
            onBackPressed()
        } */

        btnLogin.setOnClickListener {
            attemptLogin(txtUsername, txtPassword)
        }

    }

    private fun attemptLogin(mUsername: EditText, mPassword: EditText) {

        // Reset errors.
        mUsername.error = null
        mPassword.error = null

        val username = mUsername.text.toString()
        val password = mPassword.text.toString()

        var cancel = false
        lateinit var focusView: View

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) run {
            mUsername.error = getString(R.string.field_required_error)
            mPassword.error = getString(R.string.field_required_error)
            focusView = mUsername

            focusView.requestFocus()

        }

        if (TextUtils.isEmpty(username)) {
            mUsername.error = getString(R.string.field_required_error)
            focusView = mUsername
            cancel = true
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.error =getString(R.string.field_required_error)
            focusView = mPassword
            cancel = true
        }

        if (!TextUtils.isEmpty(username) && !isUsernameLengthValid(username)) {
            mUsername.error = "Username is too short"
            focusView = mUsername
            cancel = true
        }

        if (!TextUtils.isEmpty(password) && !isPassLengthValid(password)) {
            mPassword.error = "Password is too short"
            focusView = mUsername
            cancel = true
        }

        if(cancel) {
            focusView.requestFocus()
        }
        else {
           /* if(username.equals("francola") && password.equals("123456")) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } */
            progressBar!!.visibility = View.VISIBLE
            authenticateUser(username, password)
            focusView = mUsername

        }

    }

    private fun isUsernameLengthValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.length > 5
    }

    private fun isPassLengthValid(homeAdd: String): Boolean {
        //TODO: Replace this with your own logic
        return homeAdd.length > 5
    }

    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    private fun authenticateUser(uname: String, pword: String) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getuser.php"
        val params = JSONObject()

        val hashPass: String =  hashString("SHA-1", pword)

        params.put("username", uname)
        params.put("userpass", hashPass)

        apiController.post(path, params) {response ->

            progressBar!!.visibility = View.GONE

            if(response!!.getInt("status") == 1) {

                val prefs = customPreference(this, CUSTOM_PREF_NAME)
                prefs.userId = uname

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                toast(response.getString("msg"))
            }
        }
    }

}
