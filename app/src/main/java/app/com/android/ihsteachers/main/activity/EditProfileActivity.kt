package app.com.android.ihsteachers.main.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.ImageButton
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.providers.PreferencesHelper.customPreference
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import org.jetbrains.anko.toast
import org.json.JSONObject


class EditProfileActivity : AppCompatActivity() {

    var firstname: EditText? = null
    var lastname: EditText? = null
    var username: EditText? = null
    var phonenum: EditText? = null
    var email: EditText? = null

    val CUSTOM_PREF_NAME = "UserID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

    //    dobTextView = findViewById(R.id.editDOB)

        firstname = findViewById(R.id.editFullname)
        lastname = findViewById(R.id.editLastname)
        username = findViewById(R.id.editUsername)
        phonenum = findViewById(R.id.editMobile)
        email = findViewById(R.id.editEmail)

        val tbEditProfile: Toolbar = findViewById(R.id.tbEditProfile)

        val btnClose: ImageButton = tbEditProfile.findViewById(R.id.btnEditProfileClose)
        val btnSave: ImageButton = tbEditProfile.findViewById(R.id.btnEditProfileSave)

        val bundle = intent.extras
        if(bundle != null) {
            val uname = bundle.getString("PROFILE_USERNAME")
            val fname = bundle.getString("PROFILE_FNAME")
            val lname = bundle.getString("PROFILE_LNAME")
            val strEmail = bundle.getString("PROFILE_EMAIL")
            val phoneNum = bundle.getString("PROFILE_PHONENUM")
         //   val gender = bundle.getString("PROFILE_GENDER")

            firstname!!.setText(fname)
            lastname!!.setText(lname)
            username!!.setText(uname)
            email!!.setText(strEmail)
            phonenum!!.setText(phoneNum)
        }

        val context: Context = this.applicationContext

        btnClose.setOnClickListener {
            onBackPressed()
        }

        btnSave.setOnClickListener {
            editProfile(context)
        }

    }


    private fun editProfile(ctx: Context) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "editProfile.php"
        val params = JSONObject()

        val prefs = customPreference(ctx, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        params.put("userid", userId)
        params.put("username", username!!.text)
        params.put("fName", firstname!!.text)
        params.put("lName", lastname!!.text)
        params.put("email", email!!.text)
        params.put("mobile", phonenum!!.text)

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val success = obj.getString("msg")
                toast(success)
                onBackPressed()

            }
            else {
                val unsuccessful = obj.getString("msg")
                toast(unsuccessful)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        this.finish()
    }

}
