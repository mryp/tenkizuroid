package net.poringsoft.tenkizuroid;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * 設定画面フラグメント
 * Created by MRY on 2015/12/31.
 */
public class PrefFragment extends PreferenceFragment {
    public PrefFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        //バージョン情報セット
        PreferenceScreen versionScreen = (PreferenceScreen)this.findPreference("version_info");
        if (versionScreen != null) {
            String versionName = "X.X.X";
            PackageManager pm = getActivity().getPackageManager();
            if (pm != null) {
                try {
                    PackageInfo info = pm.getPackageInfo(getActivity().getPackageName(), 0);
                    versionName = info.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    versionName = "X.X.X";
                }
            }
            versionScreen.setTitle("version " + versionName);
            versionScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getString(R.string.url_poringsoft)));
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

}
