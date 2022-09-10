package ru.svolf.pcompiler.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;
import java.util.zip.CRC32;

import ru.svolf.pcompiler.App;
import ru.svolf.pcompiler.BuildConfig;
import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.databinding.ActivityAboutBinding;
import ru.svolf.pcompiler.ui.fragment.dialog.SweetContentDialog;
import ru.svolf.pcompiler.ui.fragment.dialog.UpdateDialogFragment;
import ru.svolf.pcompiler.util.StrF;
import ru.svolf.pcompiler.util.StringWrapper;

public class AboutActivity extends BaseActivity {
    ActivityAboutBinding binding;

    public static final String JSON_LINK = "https://raw.githubusercontent.com/SnowVolf/GirlUpdater/master/pcompiler_check.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            toolbar.setTitleTextColor(App.getColorFromAttr(this, R.attr.icon_color));
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.ic_more_vert));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        byte[] bytes = BuildConfig.BUILD_TIME.getBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);

        if (StringWrapper.b("8zuv+ap22YnX6ohcFCYktA")) {
            binding.contentAbout.appStatus.setImageResource(R.drawable.ic_verified);
            binding.contentAbout.relativeLayout.setBackgroundColor(App.getColorFromAttr(this, R.attr.icon_color));
        } else {
            binding.contentAbout.appStatus.setImageResource(R.drawable.ic_warning);
            binding.contentAbout.relativeLayout.setBackgroundColor(App.getColorFromAttr(this, R.attr.icon_color));
        }

        binding.contentAbout.aboutVersionItemSub.setText(String.format(Locale.ENGLISH, getString(R.string.version_sub), BuildConfig.VERSION_NAME));
        binding.contentAbout.aboutCodeItemSub.setText(String.format(Locale.ENGLISH, getString(R.string.id_sub), crc32.getValue()));
        binding.contentAbout.aboutTimeItemSub.setText(String.format(Locale.ENGLISH, getString(R.string.date_sub), BuildConfig.BUILD_TIME));
        binding.contentAbout.aboutAuthorArtemMailItem.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            // Only email apps can handle this
            intent.setData(Uri.parse("mailto:svolf15@yandex.ru"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            // Base information about app and device
            intent.putExtra(Intent.EXTRA_TEXT,
                    "App version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")\n"+
                            "Android/SDK: " + Build.VERSION.RELEASE + "/" + Build.VERSION.SDK_INT +
                            "\nModel: " + Build.MANUFACTURER + ", " + Build.MODEL +
                            "\n\n --- Write your message here (English or Russian please) --- \n"
            );
            startActivity(Intent.createChooser(intent, null));
        });
        binding.contentAbout.aboutAuthorArtemPdaItem.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://4pda.to/index.php?showuser=4324432")), null)));
        binding.contentAbout.aboutHtcPdaItem.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://4pda.to/index.php?showuser=4857164")), null)));
        binding.contentAbout.aboutRadiationxPdaItem.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://4pda.to/index.php?showuser=2556269")), null)));
        binding.contentAbout.aboutAuthorArtemGithubItem.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SnowVolf/")), null)));
        binding.contentAbout.aboutRadiationxGithubItem.setOnClickListener(__ -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RadiationX/")), null)));
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
            final Uri uri = Uri.parse("http://4pda.to/forum/index.php?showtopic=575450&view=findpost&p=64449177");
            final Intent forum = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(forum, null));
        });
    }

    private void showChangelog(){
        SweetContentDialog dialog = new SweetContentDialog(this);
        dialog.setContentText(StrF.parseText("changelog.txt"));
        dialog.setTypeface(R.font.mono);
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
            case R.id.action_help:{
                try {
                    startActivity(new Intent(this, HelpActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                return true;
            }
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
}