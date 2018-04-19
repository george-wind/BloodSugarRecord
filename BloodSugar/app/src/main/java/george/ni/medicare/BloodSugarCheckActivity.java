package george.ni.medicare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import george.ni.medicare.adapter.BloodSugarCheckAdapter;
import george.ni.medicare.db.MedicareRecordDbHelper;
import george.ni.medicare.entity.BloodSugarEntity;

public class BloodSugarCheckActivity extends BaseActivity implements View.OnClickListener {
    private BloodSugarCheckAdapter mBloodSugarCheckAdapter;
    private ListView mLvBloodSugar;
    private SmartRefreshLayout mSmartRefreshLayout;
    private List<BloodSugarEntity> mBloodSugarDataList;
    private int count = 20;
    private EditText mEtBloodSuger;
    private Button mBtConfirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar);
        initViews();
        setRefreshLayout();
        setListeners();
        initData();
    }

    private void initViews() {
        mLvBloodSugar = (ListView) findViewById(R.id.lv_blood_sugar);
        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mEtBloodSuger = (EditText) findViewById(R.id.et_input_blood_sugar);
        mBtConfirm = (Button) findViewById(R.id.bt_confirm);
    }

    private void setRefreshLayout() {
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mBloodSugarDataList = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodSugarDatas(0,count);
                mBloodSugarCheckAdapter.setData(mBloodSugarDataList);
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
//        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                List<BloodSugarEntity> bloodSugarEntities = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodSugarDatas(mBloodSugarDataList.size(),20);
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
        mBtConfirm.setOnClickListener(this);
    }

    private void initData(){
        mBloodSugarDataList = new ArrayList<>();
        mBloodSugarCheckAdapter = new BloodSugarCheckAdapter(mActivity,mBloodSugarDataList);
        mLvBloodSugar.setAdapter(mBloodSugarCheckAdapter);
        mSmartRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                addBloodSugarRecord();
                break;
        }
    }

    private void addBloodSugarRecord() {
        String text = mEtBloodSuger.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            showShortToast("请输入检测结果");
            return;
        }
        BloodSugarEntity entity = new BloodSugarEntity(text,System.currentTimeMillis());
        MedicareRecordDbHelper.getInstance(mActivity).insertBloodSugarData(entity);
        mSmartRefreshLayout.autoRefresh();
        mEtBloodSuger.setText("");
    }
}
