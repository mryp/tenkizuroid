package net.poringsoft.tenkizuroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 設定画面
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
