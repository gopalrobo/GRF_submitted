package com.example.grievanceregistrationform;

import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view);
        PDFView pdfView=findViewById(R.id.pdfView);

        pdfView.fromUri(Uri.parse(getIntent().getStringExtra("uri")));

    }
}
