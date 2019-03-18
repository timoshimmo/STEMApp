package app.com.android.ihsteachers.providers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import okhttp3.ResponseBody;


public class DownloadFile extends AsyncTask<ResponseBody, Pair<Integer, Long>, String>  {

    private ProgressDialog progressDialog;
 //   private String folder;
    private String outPutFileName;
    private boolean isDownloaded;
    private Activity mActivity;
    private static final String TAG = "DOWNLOAD_FILE";

    public DownloadFile(Activity acty, String op){
        this.outPutFileName = op;
        this.mActivity = acty;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected String doInBackground(ResponseBody... responseBodies) {

        processFile(responseBodies[0], outPutFileName);
        return null;
    }

    private void processFile(ResponseBody body, String filename) {

        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
            try {

           /* URL url = new URL(urlLink);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength(); */

                Log.d(TAG, "File Dir" + destinationFile.getAbsolutePath());

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);

                Log.d(TAG, "Input Stream" + body.byteStream().toString());

                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d(TAG, "File Size=" + fileSize);

                while ((count = inputStream.read(data)) != -1) {

                    outputStream.write(data, 0, count);
                    progress += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    publishProgress(pairs);
                    //  Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                }

                // flushing output
                outputStream.flush();

                Log.d(TAG, destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                doProgress(pairs);
                return;

            }
            catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                doProgress(pairs);
                Log.d(TAG, "Failed to save the file!");
                return;
            }

            finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }

    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(Pair<Integer, Long>... progress) {
        // setting progress percentage
        Log.d("API123", progress[0].second + " ");

        if (progress[0].first == 100)
            Toast.makeText(mActivity.getApplicationContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();


        if (progress[0].second > 0) {
            int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
            progressDialog.setProgress(currentProgress);

        }

        if (progress[0].first == -1) {
            Toast.makeText(mActivity.getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void doProgress(Pair<Integer, Long> progressDetails) {
        publishProgress(progressDetails);
    }


    @Override
    protected void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        this.progressDialog.dismiss();

        // Display File path after downloading
       // Toast.makeText(mActivity.getApplicationContext(),
        //        message, Toast.LENGTH_LONG).show();
    }

}
