package george.ni.medicare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import george.ni.medicare.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button mBtBloodSugar;
    private Button mBtBloodPressure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
    }

    private void initViews() {
        mBtBloodSugar = (Button) findViewById(R.id.bt_blood_sugar);
        mBtBloodPressure = (Button) findViewById(R.id.bt_blood_pressure);
    }

    private void setListeners(){
        mBtBloodSugar.setOnClickListener(this);
        mBtBloodPressure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_blood_sugar:
                Intent bloodSugarIntent = new Intent(mActivity,CheckBloodSugarActivity.class);
                bloodSugarIntent.putExtra(AllBloodSugarRecordsActivity.INTENT_FROM, AllBloodSugarRecordsActivity.INTENT_FROM_BLOOD_SUGAR);
                startActivity(bloodSugarIntent);
                break;
            case R.id.bt_blood_pressure:
                Intent bloodPressureIntent = new Intent(mActivity,CheckBloodPressureActivity.class);
                bloodPressureIntent.putExtra(AllBloodSugarRecordsActivity.INTENT_FROM, AllBloodSugarRecordsActivity.INTENT_FROM_BLOOD_PRESSURE);
                startActivity(bloodPressureIntent);
                break;
        }
    }
}
