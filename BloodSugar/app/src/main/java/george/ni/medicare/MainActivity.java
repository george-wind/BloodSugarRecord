package george.ni.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button mBtBloodSugar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
    }

    private void initViews() {
        mBtBloodSugar = (Button) findViewById(R.id.bt_blood_sugar);
    }

    private void setListeners(){
        mBtBloodSugar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_blood_sugar:
                Intent bloodSugarIntent = new Intent(mActivity,BloodSugarCheckActivity.class);
                startActivity(bloodSugarIntent);
                break;
        }
    }
}
