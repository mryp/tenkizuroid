package net.poringsoft.tenkizuroid;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * Created by MRY on 2015/12/29.
 */
public class PrefActivity extends PreferenceActivity {
    /**
     * 設定画面起動時処理
     * @param savedInstanceState 保存データ
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefFragment())
                .commit();
    }

}
