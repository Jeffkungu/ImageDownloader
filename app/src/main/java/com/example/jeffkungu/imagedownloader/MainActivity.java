package com.example.jeffkungu.imagedownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

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
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        editText.setText(listOfImages[i]);
    }

    public void downloadImage(View view) {
    }
}
