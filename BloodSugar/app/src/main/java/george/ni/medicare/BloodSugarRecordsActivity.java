package george.ni.medicare;

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

import george.ni.medicare.adapter.BloodSugarCheckAdapter;
import george.ni.medicare.db.MedicareRecordDbHelper;
import george.ni.medicare.entity.BloodSugarEntity;

/**
 * Created by Thinkpad on 2018/4/19.
 * 全部记录页面
 */

public class BloodSugarRecordsActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLvBloodSugar;
    private SmartRefreshLayout mSmartRefreshLayout;
    private List<BloodSugarEntity> mBloodSugarDataList;
    private BloodSugarCheckAdapter mBloodSugarCheckAdapter;
    private EditText mEtYear;
    private EditText mEtMonth;
    private Button mBtSearch;
    private int count =20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_all);
        initViews();
        setRefreshLayout();
        setListeners();
        initData();
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
                mBloodSugarDataList = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodSugarDatas(0,-1,year,month);
                mBloodSugarCheckAdapter.setData(mBloodSugarDataList);
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
//        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                List<BloodSugarEntity> bloodSugarEntities = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodSugarDatas(mBloodSugarDataList.size(),count,null,null);
//                if (bloodSugarEntities.size() <= 0) {
//                    showShortToast("没有更多数据");
//                }else {
//                    mBloodSugarDataList.addAll(bloodSugarEntities);
//                    mBloodSugarCheckAdapter.setData(mBloodSugarDataList);
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
        mBloodSugarCheckAdapter = new BloodSugarCheckAdapter(mActivity,mBloodSugarDataList);
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
}
