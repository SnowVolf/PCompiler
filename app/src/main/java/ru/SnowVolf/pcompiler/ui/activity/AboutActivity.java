package ru.SnowVolf.pcompiler.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.zip.CRC32;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.girl.utils.RenderUtils;
import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.BuildConfig;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.SweetContentDialog;
import ru.SnowVolf.pcompiler.ui.fragment.dialog.UpdateDialogFragment;
import ru.SnowVolf.pcompiler.util.StrF;
import ru.SnowVolf.pcompiler.util.StringWrapper;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.about_version_item_sub) TextView version;
    @BindView(R.id.about_code_item_sub) TextView id;
    @BindView(R.id.about_time_item_sub) TextView time;
    @BindView(R.id.artem_header_img) ImageView melissaDebling;
    @BindView(R.id.app_status) ImageView appStatus;
    @BindView(R.id.about_author_artem_mail_item) Button volfMailContact;
    @BindView(R.id.about_author_artem_pda_item) Button volfPdaContact;
    @BindView(R.id.about_htc_pda_item) Button htcPdaContact;
    @BindView(R.id.about_author_artem_github_item) Button volfGitContact;
    @BindView(R.id.relativeLayout) RelativeLayout appStatusBackground;
    @BindView(R.id.scroll1) NestedScrollView contentLayout;

    public final static String JSON_LINK = "https://raw.githubusercontent.com/SnowVolf/GirlUpdater/master/pcompiler_check.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        new CreateBitMap().execute();
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            toolbar.setTitleTextColor(App.getColorFromAttr(this, R.attr.colorAccent));
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.ic_more_vert));
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        byte[] bytes = BuildConfig.BUILD_TIME.getBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);

        if (StringWrapper.b("8zuv+ap22YnX6ohcFCYktA")) {
            appStatus.setImageResource(R.drawable.ic_verified);
            appStatusBackground.setBackgroundColor(App.getColorFromAttr(this, R.attr.colorAccent));
        } else {
            appStatus.setImageResource(R.drawable.ic_warning);
            appStatusBackground.setBackgroundColor(App.getColorFromAttr(this, R.attr.colorAccent));
        }

        version.setText(String.format(Locale.ENGLISH, getString(R.string.version_sub), BuildConfig.VERSION_NAME));
        id.setText(String.format(Locale.ENGLISH, getString(R.string.id_sub), crc32.getValue()));
        time.setText(String.format(Locale.ENGLISH, getString(R.string.date_sub), BuildConfig.BUILD_TIME));
        volfMailContact.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            // Только программы для отправки Email смогут это перехватить
            intent.setData(Uri.parse("mailto:svolf15@yandex.ru"));
            //intent.putExtra(Intent.EXTRA_EMAIL, "svolf15@yandex.ru");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            // Базовая информация об устройстве и приложении
            intent.putExtra(Intent.EXTRA_TEXT,
                    "App version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")\n"+
                            "Android/SDK: " + Build.VERSION.RELEASE + "/" + Build.VERSION.SDK_INT +
                            "\nModel: " + Build.MANUFACTURER + ", " + Build.MODEL +
                            "\n\n --- Write your message here (English or Russian please) --- \n"
            );
            startActivity(Intent.createChooser(intent, null));
        });
        volfPdaContact.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://4pda.ru/index.php?showuser=4324432")), null)));
        htcPdaContact.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://4pda.ru/index.php?showuser=4857164")), null)));
        volfGitContact.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SnowVolf/")), null)));

        initAdditionalSheet();
    }

    private void initAdditionalSheet() {
        findViewById(R.id.button_repo).setOnClickListener(view1 -> {
            final Uri uri = Uri.parse("https://github.com/SnowVolf/PCompiler/");
            final Intent git = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(git, null));
        });
        findViewById(R.id.button_changelog).setOnClickListener(view1 -> showChangelog());
        findViewById(R.id.button_forum).setOnClickListener(view1 -> {
            final Uri uri = Uri.parse("http://4pda.ru/forum/index.php?showtopic=575450&view=findpost&p=64449177");
            final Intent forum = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(forum, null));
        });
    }

    private void showChangelog(){
        SweetContentDialog dialog = new SweetContentDialog(this);
        dialog.setContentText(StrF.parseText("changelog.txt"));
        dialog.setTypeface("fonts/RobotoMono-Regular.ttf");
        dialog.setPositive(android.R.string.ok, view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_schedule_update:{
                FragmentManager manager = getSupportFragmentManager();
                UpdateDialogFragment dialogFragment = UpdateDialogFragment.newInstance(JSON_LINK);
                dialogFragment.show(manager, null);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        App.ctx().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class CreateBitMap extends AsyncTask<Void, BitmapDrawable, BitmapDrawable>{
        @Override
        protected BitmapDrawable doInBackground(Void... voids) {
            final Bitmap f = BitmapFactory.decodeResource(getResources(), R.drawable.censorship);
            return new BitmapDrawable(getResources(),
                    RenderUtils.fastBlur(f, 90, false));
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
            super.onPostExecute(bitmapDrawable);
            melissaDebling.setBackground(bitmapDrawable);
        }
    }
}
