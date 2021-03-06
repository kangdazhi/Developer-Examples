package com.samples.realwear.viewdocumentexample;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Example activity for open a file in the document viewer
 */
public class MainActivity extends AppCompatActivity {

    private AssetManager mAssetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAssetManager = getAssets();

        /**
         * Copy file to the device first.
         */
        try {
            copyFile("sample.jpg", "wiring layout.jpg", Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * On button Click open document are certain location
     * @param v Clicked View
     */
    public void onOpenDocument(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        File file = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/sample.jpg");
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(file.toURI().toString()));

        if (mimeType != null) {
            intent.setDataAndType(Uri.fromFile(file), mimeType);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }


    /**
     * Copies file from application assets folder to location
     * @throws IOException
     */
    public void copyFile(String fileName, String location, String toLocation) throws IOException {
        AssetFileDescriptor afd = null;

        try {
            InputStream in = mAssetManager.open(location);

            // Create new file to copy into.
            File file = new File(toLocation + "/" + fileName);
            file.createNewFile();

            FileOutputStream out = new FileOutputStream(file);
            copyFile(in, out, in.available());
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + toLocation, e);
        }
    }

    private void copyFile(InputStream in, OutputStream out, int lenghtOfFile) throws IOException {
        byte[] buffer = new byte[1024];
        int read, total = 0;
        while ((read = in.read(buffer)) != -1) {
            total += read;
            out.write(buffer, 0, read);
        }
    }
}
