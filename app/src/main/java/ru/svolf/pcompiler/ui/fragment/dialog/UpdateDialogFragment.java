package ru.svolf.pcompiler.ui.fragment.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.svolf.girl.compat.Html;
import ru.svolf.girl.utils.IntentHandler;
import ru.svolf.pcompiler.App;
import ru.svolf.pcompiler.BuildConfig;
import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.net.Client;
import ru.svolf.pcompiler.net.NetworkResponse;
import ru.svolf.pcompiler.util.RuntimeUtil;

/**
 * Created by Snow Volf on 28.09.2017, 22:41
 */

@SuppressWarnings("deprecation")
public class UpdateDialogFragment extends BottomSheetDialogFragment {
    private static String mValue;
    String jsonSource = null;
    private View rootView;


    public static UpdateDialogFragment newInstance(@Nullable String jsonValue) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        mValue = jsonValue;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.content_updater, null);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!RuntimeUtil.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                RuntimeUtil.storage(getActivity(), RuntimeUtil.REQUEST_EXTERNAL_STORAGE_ZIP);
            } else scheduleUpdate();
        } else scheduleUpdate();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(rootView);
        rootView.findViewById(R.id.update_button).setOnClickListener(null);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode){
                case RuntimeUtil.REQUEST_EXTERNAL_STORAGE_ZIP:{
                    scheduleUpdate();
                    break;
                }
            }
        }
    }

    private void setRefreshing(boolean isRefreshing) {
        if (isRefreshing) {

        } else {

        }
    }

    private void scheduleUpdate(){
        if (jsonSource != null) {
            checkSource(jsonSource);
        } else {
            refreshInfo();
        }
    }

    private void refreshInfo() {
        setRefreshing(true);
        ((LinearLayout) rootView.findViewById(R.id.update_content)).removeAllViews();
        Observable.fromCallable(() -> {
            NetworkResponse response = Client.getInstance().get(mValue);
            String body;
            body = response.getBody();
            return body;
        })
                .onErrorReturn(throwable -> "pizda rulyi")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkSource);
    }

    private void checkSource(String jsonSource) {
        setRefreshing(false);
        if (jsonSource.length() == 0) {
            return;
        }
        try {
            final JSONObject jsonBody = new JSONObject(jsonSource);
            final JSONObject updateObject = jsonBody.getJSONObject("update");
            checkUpdate(updateObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkUpdate(JSONObject updateObject) throws JSONException {
        final int currentVersionCode = BuildConfig.VERSION_CODE;
        final int versionCode = Integer.parseInt(updateObject.getString("version_code"));

        if (versionCode > currentVersionCode) {
            final String versionName = updateObject.getString("version_name");
            final String versionBuild = updateObject.getString("version_build");
            final String buildDate = updateObject.getString("build_date");

            final String linkGit = updateObject.getString("link_github");

            final JSONObject changesObject = updateObject.getJSONObject("changes");
            final JSONArray important = changesObject.getJSONArray("important");
            final JSONArray added = changesObject.getJSONArray("added");
            final JSONArray fixed = changesObject.getJSONArray("fixed");
            final JSONArray changed = changesObject.getJSONArray("changed");

            ((TextView) rootView.findViewById(R.id.update_info)).setText(generateCurrentInfo(versionName, versionBuild, buildDate));
            addSection(R.string.update_important, important);
            addSection(R.string.update_added, added);
            addSection(R.string.update_fixed, fixed);
            addSection(R.string.update_changed, changed);

            if (linkGit.matches(Patterns.WEB_URL.toString())){
                rootView.findViewById(R.id.update_button).setOnClickListener(v -> IntentHandler.handleDownload(linkGit));
            }
        } else {
            rootView.findViewById(R.id.update_button).setOnClickListener(null);
            Toast.makeText(getActivity(), R.string.message_no_updates, Toast.LENGTH_LONG).show();
            dismiss();
        }
    }

    private void addSection(@StringRes int title, JSONArray array) {
        if (array == null || array.length() == 0) {
            return;
        }
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, 0, 0, App.dpToPx(24));

        TextView sectionTitle = new TextView(getContext());
        sectionTitle.setText(title);
        sectionTitle.setPadding(0, 0, 0, App.dpToPx(8));
        sectionTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(sectionTitle);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < array.length(); i++) {
            try {
                String item = array.getString(i);
                stringBuilder.append("— ").append(item);
                if (i + 1 < array.length()) {
                    stringBuilder.append("<br>");
                }
            } catch (JSONException ignore) {
            }
        }

        TextView sectionText = new TextView(getContext());
        sectionText.setText(Html.INSTANCE.htmlCompat(stringBuilder.toString()));
        sectionText.setPadding(App.dpToPx(8), 0, 0, 0);
        root.addView(sectionText);

        ((LinearLayout) rootView.findViewById(R.id.update_content)).addView(root,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private String generateCurrentInfo(String name, String build, String date) {
        return String.format("%s (%s), %s", name, build, date);
    }
}
