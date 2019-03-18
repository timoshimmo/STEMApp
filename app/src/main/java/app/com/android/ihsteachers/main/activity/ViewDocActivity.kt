package app.com.android.ihsteachers.main.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import app.com.android.ihsteachers.R
import org.jetbrains.anko.zoomControls

class ViewDocActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_doc)

        val viewDocs: WebView = findViewById(R.id.wvViewDocs)
        viewDocs.webViewClient = WebViewClient()
      //  viewDocs.addView(viewDocs.zoomControls())
        viewDocs.settings.javaScriptEnabled = true

        val bundle = intent.extras
        if(bundle!=null)
        {
            val docUrl = bundle.getString("LINK_URL")
            viewDocs.loadUrl("http://docs.google.com/gview?embedded=true&url="+docUrl)
        }

    }
}
