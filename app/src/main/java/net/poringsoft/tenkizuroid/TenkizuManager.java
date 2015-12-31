package net.poringsoft.tenkizuroid;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 天気図情報管理クラス
 * Created by MRY on 2015/12/31.
 */
public class TenkizuManager {
    //定数
    //-------------------------------------------------------------
    private static final String BASE_URL = "http://www.jma.go.jp/jp/g3";
    private static final String COLOR_LIST_JSON_URL = "http://www.jma.go.jp/jp/g3/hisjs/jp_c.js";
    private static final String BW_LIST_JSON_URL = "http://www.jma.go.jp/jp/g3/hisjs/jp.js";
    private static final int TIME_OUT = 10000;


    //メソッド
    //-------------------------------------------------------------

    /**
     * データリストを取得する
     *
     * @param isColor カラー天気図を取得するかどうか
     * @param isListAsc 時系列順にするかどうか
     * @return 天気図データリスト（取得できなかった時はnullを返す）
     */
    public List<TenkizuInfo> getTenkizuInfoList(boolean isColor, boolean isListAsc) {
        String url = BW_LIST_JSON_URL;
        if (isColor) {
            url = COLOR_LIST_JSON_URL;
        }

        String jsonText = getTenkizuJsonText(url);
        List<TenkizuInfo> tenkizuInfoList = convertTenkizuInfoList(jsonText);

        if (isListAsc) {
            Collections.reverse(tenkizuInfoList);
        }
        return tenkizuInfoList;
    }

    /**
     * データリストが書かれているJSON文字列を取得する
     * @param url
     * @return
     */
    private String getTenkizuJsonText(String url) {
        String jsonBodyText = "";
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            jsonBodyText = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonBodyText;
    }

    /**
     * JSON文字列から天気図情報リストを生成して返す
     * @param jsonBodyText
     * @return
     */
    private List<TenkizuInfo> convertTenkizuInfoList(String jsonBodyText) {
        String[] lineList = jsonBodyText.split("\n");
        if (lineList.length == 0) {
            return null;
        }

        List<TenkizuInfo> tenkizuList = new ArrayList<>();
        for (String line : lineList) {
            String[] items = line.split("\"");
            if (items.length >= 5) {
                String title = items[1];
                String fileName = items[3];
                tenkizuList.add(new TenkizuInfo(BASE_URL + "/" + fileName, title));
            }
        }

        return tenkizuList;
    }
}
