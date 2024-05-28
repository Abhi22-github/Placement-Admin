package com.roaaserver.placementadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfViewerActivity extends AppCompatActivity {

    private PDFView pdfView;
    private String pdfUrl, pdfNameString;
    private TextView pdfName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdfView = findViewById(R.id.pdfView);
        pdfName = findViewById(R.id.pdf_name);
        progressBar = findViewById(R.id.progress_bar);
        try {
            pdfUrl = getIntent().getStringExtra("pdfUrl");
            pdfNameString = getIntent().getStringExtra("pdfName");
            pdfName.setText(pdfNameString);
            new RetrivedPdffromFirebase().execute(pdfUrl);
        } catch (Exception e) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    //pdf reader class
    class RetrivedPdffromFirebase extends AsyncTask<String, Void, InputStream> {
        // we are calling async task and performing
        // this task to load pdf in background.
        @Override
        protected InputStream doInBackground(String... strings) {
            // below line is for declaring
            // our input stream.
            InputStream pdfStream = null;
            try {
                // creating a new URL and passing
                // our string in it.
                URL url = new URL(strings[0]);

                // creating a new http url connection and calling open
                // connection method to open http url connection.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    // if the connection is successful then
                    // we are getting response code as 200.
                    // after the connection is successful
                    // we are passing our pdf file from url
                    // in our pdfstream.
                    pdfStream = new BufferedInputStream(httpURLConnection.getInputStream());
                }

            } catch (IOException e) {
                // this method is
                // called to handle errors.
                return null;
            }
            // returning our stream
            // of PDF file.
            return pdfStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after loading stream we are setting
            // the pdf in your pdf view.
            pdfView.fromStream(inputStream)
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }).load();

        }
    }
}