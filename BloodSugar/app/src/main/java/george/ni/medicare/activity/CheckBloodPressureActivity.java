package george.ni.medicare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import george.ni.medicare.R;
import george.ni.medicare.adapter.BloodPressureCheckAdapter;
import george.ni.medicare.db.MedicareRecordDbHelper;
import george.ni.medicare.entity.BloodPressureEntity;

/**
 * Created by Thinkpad on 2018/4/20.
 */

public  class CheckBloodPressureActivity extends BaseActivity implements View.OnClickListener{
    private BloodPressureCheckAdapter mBloodSugarCheckAdapter;
    private ListView mLvBloodSugar;
    private SmartRefreshLayout mSmartRefreshLayout;
    protected List<BloodPressureEntity> mCheckDataList;
    protected int count = 20;
    protected EditText mEtBloodPressureLow;
    protected EditText mEtBloodPressureHigh;
    private Button mBtConfirm;
    private TextView mTvAllRecords;
    protected TextView mTvTitle;
    private String mTableName;
    private int INTENT_FROM_TYPE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_blood_pressure);
        getIntentData();
        initViews();
        setRefreshLayout();
        setListeners();
        initData();
    }

    private void getIntentData() {
//        INTENT_FROM_TYPE = getIntent().getIntExtra(AllBloodSugarRecordsActivity.INTENT_FROM, AllBloodSugarRecordsActivity.INTENT_FROM_BLOOD_SUGAR);
//        if (INTENT_FROM_TYPE == AllBloodSugarRecordsActivity.INTENT_FROM_BLOOD_SUGAR) {
//            mTableName = MedicareRecordDbHelper.tableName_BLODD_SUGAR;
//        }else {
//        }
        mTableName = MedicareRecordDbHelper.tableName_BLODD_PRESSURE;
    }

    private void initViews() {
        mLvBloodSugar = (ListView) findViewById(R.id.lv_blood_sugar);
        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mEtBloodPressureLow = (EditText) findViewById(R.id.et_input_low_blood_pressure);
        mEtBloodPressureHigh = (EditText) findViewById(R.id.et_input_high_blood_pressure);
        mBtConfirm = (Button) findViewById(R.id.bt_confirm);
        mTvAllRecords = (TextView) findViewById(R.id.tv_all_record);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

    }

    private void setRefreshLayout() {
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCheckDataList = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodPressureCheckDatas(mTableName,0,count,null,null);
                mBloodSugarCheckAdapter.setData(mCheckDataList);
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
//        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                List<BloodSugarEntity> bloodSugarEntities = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodSugarCheckDatas(mCheckDataList.size(),20);
//                if (bloodSugarEntities.size() <= 0) {
//                    showShortToast("没有更多数据");
//                }else {
//                    mCheckDataList.addAll(bloodSugarEntities);
//                    mBloodSugarCheckAdapter.setData(mCheckDataList);
//                }
//                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
//            }
//        });
    }

    private void setListeners() {
        mBtConfirm.setOnClickListener(this);
        mTvAllRecords.setOnClickListener(this);
    }

    private void initData(){
        mCheckDataList = new ArrayList<>();
        mBloodSugarCheckAdapter = new BloodPressureCheckAdapter(mActivity, mCheckDataList,mTableName);
        mLvBloodSugar.setAdapter(mBloodSugarCheckAdapter);
        mSmartRefreshLayout.autoRefresh();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                responseToConfirm();
                break;
            case R.id.tv_all_record:
                Intent allRecordIntent = new Intent(mActivity,AllBloodPressureRecordsActivity.class);
                allRecordIntent.putExtra(AllBloodSugarRecordsActivity.INTENT_FROM,INTENT_FROM_TYPE);
                startActivity(allRecordIntent);
                break;
        }
    }

    private void responseToConfirm() {
        String lowResult = mEtBloodPressureLow.getText().toString().trim();
        String highResult = mEtBloodPressureHigh.getText().toString().trim();
        if (TextUtils.isEmpty(lowResult) || TextUtils.isEmpty(highResult)) {
            showShortToast("请输入检测结果");
            return;
        }
        BloodPressureEntity entity = new BloodPressureEntity(lowResult,highResult,System.currentTimeMillis());
        MedicareRecordDbHelper.getInstance(mActivity).insertBloodPressureCheckData(mTableName,entity);
        mSmartRefreshLayout.autoRefresh();
        mEtBloodPressureLow.setText("");
        mEtBloodPressureHigh.setText("");
    }

}
