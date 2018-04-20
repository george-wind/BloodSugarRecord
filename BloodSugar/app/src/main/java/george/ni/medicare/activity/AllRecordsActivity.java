package george.ni.medicare.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import george.ni.medicare.R;
import george.ni.medicare.adapter.BloodSugarCheckAdapter;
import george.ni.medicare.db.MedicareRecordDbHelper;
import george.ni.medicare.entity.BloodSugarEntity;

/**
 * Created by Thinkpad on 2018/4/19.
 * 全部记录页面
 */

public class AllRecordsActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLvBloodSugar;
    private SmartRefreshLayout mSmartRefreshLayout;
    protected List<BloodSugarEntity> mBloodSugarDataList;
    private BloodSugarCheckAdapter mBloodSugarCheckAdapter;
    private EditText mEtYear;
    private EditText mEtMonth;
    private Button mBtSearch;
    private int count =20;
    protected int INTENT_FROM_TYPE;
    public static final String INTENT_FROM = "INTENT_FROM";
    public static final int INTENT_FROM_BLOOD_SUGAR=0;
    public static final int INTENT_FROM_BLOOD_PRESSURE=1;
    private String mTableName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_all);
        getIntentData();
        initViews();
        setRefreshLayout();
        setListeners();
        initData();
    }

    private void getIntentData() {
        INTENT_FROM_TYPE = getIntent().getIntExtra(INTENT_FROM,INTENT_FROM_BLOOD_SUGAR);
        if (INTENT_FROM_TYPE == AllRecordsActivity.INTENT_FROM_BLOOD_SUGAR) {
            mTableName = MedicareRecordDbHelper.tableName_BLODD_SUGAR;
        }else {
            mTableName = MedicareRecordDbHelper.tableName_BLODD_PRESSURE;
        }
    }

    private void initViews() {
        mLvBloodSugar = (ListView) findViewById(R.id.lv_blood_sugar);
        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mEtMonth = (EditText) findViewById(R.id.et_month);
        mEtYear = (EditText) findViewById(R.id.et_year);
        mBtSearch = (Button) findViewById(R.id.bt_search);
    }

    private void setRefreshLayout() {
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                String year = mEtYear.getText().toString().trim();
                String month = mEtMonth.getText().toString().trim();
                MedicareRecordDbHelper.getInstance(mActivity).loadNormalCheckDatas(mTableName,0,-1,year,month);
                mBloodSugarCheckAdapter.setData(mBloodSugarDataList);
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
//        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                List<BloodSugarEntity> bloodSugarEntities = MedicareRecordDbHelper.getInstance(mActivity).loadNormalCheckDatas(mCheckDataList.size(),count,null,null);
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
        mBtSearch.setOnClickListener(this);
    }

    private void initData() {
        mBloodSugarDataList = new ArrayList<>();
        mBloodSugarCheckAdapter = new BloodSugarCheckAdapter(mActivity,mBloodSugarDataList,mTableName);
        mLvBloodSugar.setAdapter(mBloodSugarCheckAdapter);
        mSmartRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_search:
                mSmartRefreshLayout.autoRefresh();
                break;
        }
    }


    class ExportRecordsTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
//            List<BloodSugarEntity> entities =
//                    MedicareRecordDbHelper.getInstance(mActivity).loadAllCheckRecords();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
