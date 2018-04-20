package george.ni.medicare.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Thinkpad on 2018/4/19.
 */

public class BaseActivity extends AppCompatActivity {
    protected Activity mActivity;
    private Toast toast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    protected void showShortToast(String text){
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mActivity,text,Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }
}
