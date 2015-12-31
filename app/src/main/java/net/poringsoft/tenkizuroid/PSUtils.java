package net.poringsoft.tenkizuroid;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * ユーティリティクラス
 * Created by MRY on 2015/12/31.
 */
public class PSUtils {
    private static Toast s_toast = null;

    /**
     * トースト！（短め）
     * @param c
     * @param msg
     */
    public static void toast(Context c, String msg)
    {
        if (c == null)
        {
            return;
        }

        try
        {
            if (s_toast != null)
            {
                s_toast.cancel();
            }
            s_toast = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
            s_toast.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ダイアログを表示する
     * @param context
     * @param title
     * @param text
     */
    public static void alert(Context context, String title, String text)
    {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //何もしない
                    }
                })
                .create();
        dialog.show();
    }

}
