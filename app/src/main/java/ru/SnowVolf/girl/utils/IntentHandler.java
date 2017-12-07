package ru.SnowVolf.girl.utils;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.net.Client;
import ru.SnowVolf.pcompiler.net.NetworkRequest;
import ru.SnowVolf.pcompiler.net.NetworkResponse;
import ru.SnowVolf.pcompiler.util.Constants;

/**
 * Created by Snow Volf on 01.10.2017, 0:50
 */

public class IntentHandler {
    public static void handleDownload(String url) {
        Log.d(Constants.INSTANCE.getTAG(), "handleDownload " + url);
        String fileName = url;
        try {
            fileName = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int cut = fileName.lastIndexOf('/');
        if (cut != -1) {
            fileName = fileName.substring(cut + 1);
        }
        handleDownload(App.getActivity(), fileName, url);
    }

    public static void handleDownload(Context context, String fileName, String url) {
        Log.d(Constants.INSTANCE.getTAG(), "handleDownload " + fileName + " : " + url);
            new AlertDialog.Builder(context)
                    .setItems(new CharSequence[]{context.getString(R.string.update_sys), context.getString(R.string.update_ext)}, (dialogInterface, i) -> {
                        switch (i){
                            case 0:
                                systemDownloader(fileName, url);
                                break;
                            case 1:
                                redirectDownload(fileName, url);
                                break;
                        }
                    })
                    .show();
    }

    private static void redirectDownload(String fileName, String url) {
        Toast.makeText(App.getContext(), String.format(App.getContext().getString(R.string.perform_request_link), fileName), Toast.LENGTH_SHORT).show();
        Observable.fromCallable(() -> Client.getInstance().request(new NetworkRequest.Builder().url(url).withoutBody().build()))
                .onErrorReturn(throwable -> new NetworkResponse(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getUrl() == null) {
                        Toast.makeText(App.getContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    externalDownloader(response.getRedirect());
                });
    }

    private static void systemDownloader(String fileName, String url) {
        DownloadManager dm = (DownloadManager) App.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setMimeType("application/vnd.android.package-archive");
        dm.enqueue(request);
    }

    public static void externalDownloader(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.ctx().startActivity(Intent.createChooser(intent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
