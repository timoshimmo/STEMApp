package app.com.android.ihsteachers.main.activity

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.annotation.NonNull
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.NotificationCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import kotlinx.android.synthetic.main.activity_class_details.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import android.text.Spannable
import android.text.Html
import android.util.Log
import android.widget.*
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import org.jetbrains.anko.toast
import tm.charlie.expandabletextview.ExpandableTextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import app.com.android.ihsteachers.providers.*
import com.github.kittinunf.fuel.Fuel
import org.jetbrains.anko.selector
import pub.devrel.easypermissions.EasyPermissions
import java.io.*

class ClassDetailsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    val CUSTOM_PREF_NAME = "UserID"
    private var progressBar: ProgressBar? = null
    lateinit var txtSubjectName: TextView
    var txtClassName: TextView? = null
    var txtDuration: TextView? = null
    private var txtDate: TextView? = null
    var txtTerm: TextView? = null
    private var txtContentTitle: TextView? = null
    private lateinit var txtWordContent: ExpandableTextView
    private var classCollapsibleToolbar: CollapsingToolbarLayout? = null
    private var txtClassId: TextView? = null
    private var filesRowLinearLayout:View? = null
    private var attachmentList: MutableList<String> = mutableListOf()
    private var fileNametList: MutableList<String> = mutableListOf()
    private val TAG = this::class.java.simpleName
    private val WRITE_REQUEST_CODE = 300
    var html: Spannable? = null

    private var mNotifyManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null

    var id = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_details)
        setSupportActionBar(toolbarClassDetails)

        progressBar = findViewById(R.id.progressBarClassDetails)

        txtSubjectName = findViewById(R.id.txtSubjectName)
        txtClassName = findViewById(R.id.txtClassName)
        txtDuration = findViewById(R.id.txtDuration)
        txtDate = findViewById(R.id.txtClassDate)
        txtContentTitle = findViewById(R.id.txtStudyMaterialTitle)
        txtWordContent = findViewById(R.id.txtClassMaterialContent)
        classCollapsibleToolbar = findViewById(R.id.collapsibleToolbar)
        txtTerm = findViewById(R.id.txtTermName)
        txtClassId = findViewById(R.id.txtClassId)

        val btnCovered: Button = findViewById(R.id.btnCoveredClass)
        val btnDownload: Button = findViewById(R.id.btnDownloadClass)

        progressBar!!.visibility = View.VISIBLE

        val bundle = intent.extras
        if(bundle!=null)
        {

            val strTopic = bundle.getString("SELECTED_TOPIC")
            val strSubject = bundle.getString("SELECTED_SUBJECT")

            classCollapsibleToolbar!!.title = strSubject
            loadClassDetails(strTopic, strSubject)

            app_bar_image.setImageResource(R.drawable.tinted_biology_applayout_background)
            txtClassMaterialContent.onStateChange { oldState, newState ->

                if("$oldState".equals("Expanded", true) && "$newState".equals("Collapsed", true)) {
                    txtViewMore.text = resources.getString(R.string.str_view_more)
                }
                if("$oldState".equals("Collapsed", true) && "$newState".equals("Expanded", true)) {
                    txtViewMore.text = resources.getString(R.string.str_view_less)

                }
            }

            txtViewMore.setOnClickListener {
                txtClassMaterialContent.toggle(true)
            }
        }

        btnCovered.setOnClickListener {
            insertCovered(applicationContext)
        }

        btnDownload.setOnClickListener {

            var resultVal = ""

            if(attachmentList.size > 1) {
                selector("Selet file to download", fileNametList) { _, i ->
                    resultVal = attachmentList[i]

                    Log.d(TAG, "File URL $resultVal")

                    if (EasyPermissions.hasPermissions(this@ClassDetailsActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) run {
                        Log.d(TAG, "File URL after permit $resultVal")
                        downloadFuel(resultVal)
                    }
                    else {
                        EasyPermissions.requestPermissions(this@ClassDetailsActivity, "This app needs access to your file storage so that it can write files.",
                                WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
            else {
                resultVal = attachmentList[0]

                Log.d(TAG, "File URL $resultVal")

                if (EasyPermissions.hasPermissions(this@ClassDetailsActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) run {
                    Log.d(TAG, "File URL after permit $resultVal")
                    downloadFuel(resultVal)
                }
                else {
                    EasyPermissions.requestPermissions(this@ClassDetailsActivity, "This app needs access to your file storage so that it can write files.",
                            WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }




        }
    }

    private fun loadClassDetails(topicid: String, subjName: String) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "getClassDetails.php"
        val params = JSONObject()


        params.put("topicid", topicid)
        params.put("subjectName", subjName)

        apiController.post(path, params) {response ->

            progressBar!!.visibility = View.GONE

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                val arrayAll = obj.getJSONArray("result")

                val allData = arrayAll.getJSONObject(0)

                txtSubjectName.text = allData.getString("topic")
                txtClassName!!.text = allData.getString("class")
             //   txtDuration!!.text = allData.getString("class")
              //  txtDate!!.text = allData.getString("dayWeek")
                txtContentTitle!!.text = allData.getString("topic")
                txtTerm!!.text = allData.getString("term")
                txtClassId!!.text = allData.getString("id")

                val imageGetter = PicassoImageGetterJava(txtClassMaterialContent, applicationContext)

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    html = Html.fromHtml(allData.getString("wordContent"), Html.FROM_HTML_MODE_LEGACY, imageGetter, null) as Spannable
                } else {
                    html = Html.fromHtml(allData.getString("wordContent"), imageGetter, null) as Spannable
                }

                txtClassMaterialContent.text = html


                val ll = findViewById<LinearLayout>(R.id.attachContainer)


                if(allData.getString("files") != null) {

                    val strFileName = allData.getString("files")

                    if(allData.getString("files").contains(",")) {
                        val strFiles: List<String> = strFileName.split(",")
                        val iterator = strFiles.listIterator()


                        for(item in iterator) {

                            filesRowLinearLayout = View.inflate(applicationContext, R.layout.attachment_files_row, null)
                            filesRowLinearLayout!!.findViewById<TextView>(R.id.txtFileName).text = item
                            ll.addView(filesRowLinearLayout)

                            val fileName = item
                            fileNametList.add(fileName)
                            val filePathName = "http://stemapp.com.ng/ihs-teachers-stem/documents/"+fileName
                            attachmentList.add(filePathName)

                            filesRowLinearLayout!!.setOnClickListener {
                                val filename = it.findViewById<TextView>(R.id.txtFileName).text

                                val filePath = "http://stemapp.com.ng/ihs-teachers-stem/documents/"+filename


                                if (filename.contains("mp4", true)) {
                                    val intent = Intent(this, VideoViewActivity::class.java)
                                    intent.putExtra("VIDEO_URL", filePath)
                                    startActivity(intent)
                                }
                                else {

                                    val intent = Intent(this, ViewDocActivity::class.java)
                                    intent.putExtra("LINK_URL", filePath)
                                    startActivity(intent)
                                }
                            }
                        }
                    }

                    else {

                        filesRowLinearLayout = View.inflate(applicationContext, R.layout.attachment_files_row, null)
                        filesRowLinearLayout!!.findViewById<TextView>(R.id.txtFileName).text = strFileName
                        ll.addView(filesRowLinearLayout)

                        val filePathName = "http://stemapp.com.ng/ihs-teachers-stem/documents/"+strFileName
                        attachmentList.add(filePathName)

                        filesRowLinearLayout!!.setOnClickListener {
                            val filename = it.findViewById<TextView>(R.id.txtFileName).text

                            val filePath = "http://stemapp.com.ng/ihs-teachers-stem/documents/"+filename


                            if (filename.contains("mp4", true)) {
                                val intent = Intent(this, VideoViewActivity::class.java)
                                intent.putExtra("VIDEO_URL", filePath)
                                startActivity(intent)
                            }
                            else {

                                val intent = Intent(this, ViewDocActivity::class.java)
                                intent.putExtra("LINK_URL", filePath)
                                startActivity(intent)
                            }
                        }
                    }

                }
            }
        }
    }

    private fun insertCovered(ctx: Context) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "addCovered.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(ctx, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        val currentDate: String

        currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val curDate = LocalDateTime.now()
            curDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            sdf.format(Date())
        }

        params.put("username", userId)
        params.put("subject_name", txtSubjectName.text)
        params.put("topic_title", classCollapsibleToolbar!!.title)
        params.put("class_name", txtClassName!!.text)
        params.put("term_name", txtTerm!!.text)
        params.put("full_date", currentDate)
        params.put("class_period", txtDuration!!.text)

        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {
                deleteCoveredClass()
            }
            else {
                val unsuccessful = obj.getString("msg")
                toast(unsuccessful)
            }
        }
    }

    private fun deleteCoveredClass() {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "deleteCoveredClass.php"
        val params = JSONObject()

        params.put("classid", txtClassId!!.text)

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


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        var resultVal = ""

        if(attachmentList.size > 1) {
            selector("Selet file to download", fileNametList) { _, i ->
                resultVal = attachmentList[i]
            }
        }
        else {
            resultVal = attachmentList[0]
        }

        downloadFuel(resultVal)

    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@ClassDetailsActivity)
    }

    private fun downloadFuel(fileUrl: String) {

        //id += 1
        val fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1, fileUrl.length)
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), fileName)

        val intent = Intent(Intent.ACTION_VIEW) //ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(file), "*/*")
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        val i = Intent.createChooser(intent, "Open with")
   //     startActivity(i)

        val resultIntent = PendingIntent.getActivity(this@ClassDetailsActivity,
                0, i, PendingIntent.FLAG_UPDATE_CURRENT)

        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(this@ClassDetailsActivity)

        mBuilder!!.setContentTitle("File Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ihs_logo_white)
                .setContentIntent(resultIntent)

        mBuilder!!.setProgress(0, 0, true)
        mNotifyManager!!.notify(id, mBuilder!!.build())



        Fuel.download(fileUrl).fileDestination { response, url ->
            val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            File(folder, fileName)
        }.progress { readBytes, totalBytes ->
            val progress = readBytes.toFloat() / totalBytes.toFloat()
            Log.d("log",progress.toString())

        }.response { request, response, result ->
            Log.d("status result", result.component1().toString())
            Log.d("status res", response.responseMessage) //OK
            Log.d("status req", request.url.toString())

            if(response.responseMessage == "OK") {
            //    loadDialog.dismiss()
                mBuilder!!.setContentText("Download completed")
                        // Removes the progress bar
                        .setProgress(0,0,false)
                mNotifyManager!!.notify(id, mBuilder!!.build())
                toast("$fileName Successfully Downloaded")
            }

        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.class_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_complete -> {
            onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }

    }
}
