package app.com.android.ihsteachers.splash

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.com.android.ihsteachers.R
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import app.com.android.ihsteachers.providers.PreferencesHelper.customPreference
import app.com.android.ihsteachers.providers.PreferencesHelper.bkgImgOne
import app.com.android.ihsteachers.providers.PreferencesHelper.bkgImgTwo
import app.com.android.ihsteachers.providers.PreferencesHelper.bkgImgThree
import app.com.android.ihsteachers.providers.PreferencesHelper.bkgImgLogin
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import app.com.android.ihsteachers.welcome.activity.WelcomeActivity
import org.json.JSONObject


class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 2000 //2 seconds

    val CUSTOM_PREF_BKGONE = "BKGIMG_ONE"
    val CUSTOM_PREF_BKGTWO = "BKGIMG_TWO"
    val CUSTOM_PREF_BKGTHREE = "BKGIMG_THREE"
    val CUSTOM_PREF_BKGLOGIN = "BKGIMG_LOGIN"

    var progressBar: ProgressBar? = null

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            progressBar!!.visibility = View.VISIBLE
            loadBkgImages(this.applicationContext)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        progressBar = findViewById(R.id.progressBarSplash)

        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    private fun loadBkgImages(ctx: Context) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getBackgroundImages.php"
        val params = JSONObject()
        params.put("", "")

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                for(i in 0 until arrayAll.length()) {

                    val objValue = arrayAll.getJSONObject(i)

                    if(objValue.getString("imagePage") == "first_page") {

                        val fullPath = "http://stemapp.com.ng/ihs-teachers-stem/"+objValue.getString("imageName")
                        val prefs = customPreference(ctx, CUSTOM_PREF_BKGONE)
                        prefs.bkgImgOne = fullPath
                      //  val mBkgImgPrefOne = prefs.bkgImgOne
                    }

                    if(objValue.getString("imagePage") == "second_page") {

                        val fullPath = "http://stemapp.com.ng/ihs-teachers-stem/"+objValue.getString("imageName")
                        val prefs = customPreference(ctx, CUSTOM_PREF_BKGTWO)
                        prefs.bkgImgTwo = fullPath

                    }

                    if(objValue.getString("imagePage") == "third_page") {

                        val fullPath = "http://stemapp.com.ng/ihs-teachers-stem/"+objValue.getString("imageName")
                        val prefs = customPreference(ctx, CUSTOM_PREF_BKGTHREE)
                        prefs.bkgImgThree = fullPath

                    }

                    if(objValue.getString("imagePage") == "login_page") {

                        val fullPath = "http://stemapp.com.ng/ihs-teachers-stem/"+objValue.getString("imageName")
                        val prefs = customPreference(ctx, CUSTOM_PREF_BKGLOGIN)
                        prefs.bkgImgLogin = fullPath

                    }


                }

                progressBar!!.visibility = View.GONE

                val intent = Intent(applicationContext,
                        WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}
