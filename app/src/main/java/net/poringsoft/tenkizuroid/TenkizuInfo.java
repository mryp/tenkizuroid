package net.poringsoft.tenkizuroid;

/**
 * 天気図を保持するクラス
 * Created by MRY on 2015/12/29.
 */
public class TenkizuInfo {

    //フィールド
    //------------------------------------------------------------
    private String m_url;
    private String m_title;


    //プロパティ
    //------------------------------------------------------------

    /**
     * 画像URL
     * @return
     */
    public String getUrl() {
        return m_url;
    }

    /**
     * 画像のタイトル
     * @return
     */
    public String getTitle() {
        return m_title;
    }


    //メソッド
    //------------------------------------------------------------

    /**
     * コンストラクタ
     * @param url 画像URL
     * @param title タイトル
     */
    public TenkizuInfo(String url, String title) {
        m_url = url;
        m_title = title;
    }

    /**
     * デバッグ用文字列
     * @return
     */
    @Override
    public String toString() {
        return m_title + " " + m_url;
    }
}
