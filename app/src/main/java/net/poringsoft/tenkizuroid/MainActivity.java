package net.poringsoft.tenkizuroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * メイン画面
 */
public class MainActivity extends AppCompatActivity {
    //定数
    //------------------------------------------------------------------------
    private static final int REQ_CODE_PREF = 1;         //設定画面から
    private final static int INDEX_LATEST = 3;  //最新データの位置

    //フィールド
    //------------------------------------------------------------------------
    private SectionsPagerAdapter m_sectionsPagerAdapter;
    private ViewPager m_viewPager;
    private TenkizuManagerTask m_task;

    //メソッド
    //------------------------------------------------------------------------

    /**
     * 画面起動時処理
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PSDebug.d("onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_viewPager = (ViewPager) findViewById(R.id.container);
        m_task = new TenkizuManagerTask();
        m_task.execute();
    }

    /**
     * メニュー表示オプション
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * メニューアイテム選択処理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSetting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 設定画面へ遷移する
     */
    private void startSetting() {
        Intent intent = new Intent(this, PrefActivity.class);
        this.startActivityForResult(intent, REQ_CODE_PREF);
    }


    /**
     * 画面戻りイベント
     *
     * @param requestCode 呼び出し元（この画面で指定）コード
     * @param resultCode  呼び出された側の結果コード
     * @param data        インテント
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PSDebug.d("requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == REQ_CODE_PREF) {
            m_task = new TenkizuManagerTask();
            m_task.execute();
        }
    }

    /**
     * 表示する天気図のURLリストを取得するタスク
     */
    private class TenkizuManagerTask extends AsyncTask<Void, Void, List<TenkizuInfo>> {

        /**
         * 実処理タスク
         *
         * @param params
         * @return
         */
        @Override
        protected List<TenkizuInfo> doInBackground(Void... params) {
            TenkizuManager manager = new TenkizuManager();
            return manager.getTenkizuInfoList(EnvOption.getViewShowColor(getApplicationContext()), true);
        }

        /**
         * 処理完了後処理（UIスレッド）
         *
         * @param result
         */
        @Override
        protected void onPostExecute(List<TenkizuInfo> result) {
            if (result == null) {
                PSUtils.toast(getApplicationContext(), "データリストの取得に失敗しました");
                return;
            }
            if (result.size() < INDEX_LATEST) {
                PSUtils.toast(getApplicationContext(), "データリストが壊れています");
                return;
            }

            m_sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), result);
            m_viewPager.setAdapter(m_sectionsPagerAdapter);
            m_viewPager.setCurrentItem(result.size() - INDEX_LATEST, true);   //最新のデータを取得する
            m_viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    ;

    /**
     * 内部画面フラグメント
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_IMAGE_URL = "image_url";

        public PlaceholderFragment() {
        }

        /**
         * 画面インスタンス生成
         */
        public static PlaceholderFragment newInstance(TenkizuInfo tenkizuInfo) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_IMAGE_URL, tenkizuInfo.getUrl());
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * 画面構成
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            String imageUrl = getArguments().getString(ARG_IMAGE_URL);
            PSDebug.d("call imageUrl=" + imageUrl);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ImageView tenkizuImage = (ImageView) rootView.findViewById(R.id.mainImageView);
            if (tenkizuImage != null) {
                ImageLoader.getInstance().displayImage(imageUrl, tenkizuImage);
            }
            return rootView;
        }
    }

    /**
     * フラグメント切り替え用アダプター
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<TenkizuInfo> m_tenkizuList;

        public SectionsPagerAdapter(FragmentManager fm, List<TenkizuInfo> tenkizuList) {
            super(fm);

            m_tenkizuList = tenkizuList;
        }

        @Override
        public Fragment getItem(int position) {
            PSDebug.d("call position=" + position);
            return PlaceholderFragment.newInstance(m_tenkizuList.get(position));
        }

        @Override
        public int getCount() {
            return m_tenkizuList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = m_tenkizuList.get(position).getTitle();
            return title;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
