package net.poringsoft.tenkizuroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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


    //フィールド
    //------------------------------------------------------------------------
    private SectionsPagerAdapter m_sectionsPagerAdapter;
    private ViewPager m_vViewPager;

    //メソッド
    //------------------------------------------------------------------------
    /**
     * 画面起動時処理
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PSDebug.d("onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_vViewPager = (ViewPager) findViewById(R.id.container);
        TenkizuUrlTask.execute();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AsyncTask<Void, Void, List<TenkizuInfo>> TenkizuUrlTask = new AsyncTask<Void, Void, List<TenkizuInfo>>() {

        @Override
        protected List<TenkizuInfo> doInBackground(Void... params) {
            TenkizuManager manager = new TenkizuManager();
            return manager.getTenkizuInfoList(true, true);
        }

        @Override
        protected void onPostExecute(List<TenkizuInfo> result) {
            if (result == null)
            {
                PSUtils.toast(getApplicationContext(), "データリストの取得に失敗しました");
                return;
            }
            if (result.size() < 3)
            {
                PSUtils.toast(getApplicationContext(), "データリストが壊れています");
                return;
            }

            for (TenkizuInfo info : result)
            {
                PSDebug.d(info.toString());
            }
            m_sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), result);
            m_vViewPager.setAdapter(m_sectionsPagerAdapter);
            m_vViewPager.setCurrentItem(result.size() - 3, true);   //最新のデータを取得する
        }
    };

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ImageView tenkizuImage = (ImageView) rootView.findViewById(R.id.mainImageView);
            if (tenkizuImage != null) {
                ImageLoader.getInstance().displayImage(getArguments().getString(ARG_IMAGE_URL), tenkizuImage);
            }
            return rootView;
        }
    }

    /**
     * フラグメント切り替え用アダプター
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<TenkizuInfo> m_tenkizuList;

        public SectionsPagerAdapter(FragmentManager fm, List<TenkizuInfo> tenkizuList) {
            super(fm);

            m_tenkizuList = tenkizuList;
        }

        @Override
        public Fragment getItem(int position) {
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
    }
}
