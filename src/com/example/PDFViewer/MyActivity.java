package com.example.PDFViewer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

import java.io.File;
import java.io.IOException;

public class MyActivity extends Activity {
  /**
   * Called when the activity is first created.
   */

  private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
      Font.BOLD);
  private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
      Font.NORMAL, BaseColor.RED);
  private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
      Font.BOLD);
  private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
      Font.BOLD);
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
//    generatePdf();
    new FirstPdf().main(null);
  }

  public void download(View v)
  {
    new DownloadFile().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");
  }

  public void view(View v)
  {
//    File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "maven.pdf");  // -> filename = maven.pdf
    File pdfFile = new File(Environment.getExternalStorageDirectory() + "/output.pdf");  // -> filename = maven.pdf
    Uri path = Uri.fromFile(pdfFile);
    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
    pdfIntent.setDataAndType(path, "application/pdf");
    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    try{
      startActivity(pdfIntent);
    }catch(ActivityNotFoundException e){
      Toast.makeText(MyActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
    }
  }

  private class DownloadFile extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
      String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
      String fileName = strings[1];  // -> maven.pdf
      String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
      File folder = new File(extStorageDirectory, "testthreepdf");
      folder.mkdir();

      File pdfFile = new File(folder, fileName);

      try{
        pdfFile.createNewFile();
      }catch (IOException e){
        e.printStackTrace();
      }
      FileDownloader.downloadFile(fileUrl, pdfFile);
      return null;
    }
  }
}
