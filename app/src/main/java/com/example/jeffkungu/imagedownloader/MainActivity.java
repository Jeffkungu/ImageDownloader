package com.example.jeffkungu.imagedownloader;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String INPUT_STREAM = "INPUT_STREAM: ";
    private EditText editText;
    private ListView listView;
    private String[] listOfImages;
    private ProgressBar progressBar;
    private LinearLayout loadingSection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.downloadUrl);
        listView = findViewById(R.id.urlList);
        listView.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        progressBar = findViewById(R.id.downloadProgress);
        loadingSection = findViewById(R.id.loadingSection);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        editText.setText(listOfImages[i]);
    }

    public void downloadImage(View view) {
        String url = editText.getText().toString();
        DownloadImagesTask downloadImagesTask = new DownloadImagesTask();
        downloadImagesTask.execute(url); // used to start the asynchronous task. the url param will be received at the doInBackground method.
    }

    public boolean downloadImageUsingThreads(String url) {
        boolean successful = false;
        URL downloadUrl;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file;
        try {
            downloadUrl = new URL(url);
            connection = (HttpURLConnection) downloadUrl.openConnection();
            inputStream = connection.getInputStream();

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.
                    DIRECTORY_PICTURES).getAbsolutePath() + "/" + Uri.parse(url).getLastPathSegment());
            Log.d("URL_PATH", " " + file.getAbsolutePath());
            fileOutputStream = new FileOutputStream(file);

            int read;
            byte[] buffer = new byte[1024];
            while ((read=inputStream.read(buffer))!=-1) {
                Log.d(INPUT_STREAM, INPUT_STREAM + read);
                fileOutputStream.write(buffer, 0, read);
            }
            successful = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection!=null) {
                connection.disconnect();
            }
            if (inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream!=null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return successful;
    }

    public class DownloadImagesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) { // Performs the long running task.
            downloadImageUsingThreads(strings[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {  // Its called immediately the .execute method is called. Sets up the relevant variables needed before the task is started.
            loadingSection.setVisibility(VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Its called after the task is finished.
            loadingSection.setVisibility(View.GONE);
        }

    }
}
