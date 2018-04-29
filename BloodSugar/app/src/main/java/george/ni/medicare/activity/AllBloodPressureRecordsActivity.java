package george.ni.medicare.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import george.ni.medicare.R;
import george.ni.medicare.adapter.BloodPressureCheckAdapter;
import george.ni.medicare.adapter.BloodSugarCheckAdapter;
import george.ni.medicare.db.MedicareRecordDbHelper;
import george.ni.medicare.entity.BloodPressureEntity;
import george.ni.medicare.entity.BloodSugarEntity;
import george.ni.medicare.permission.OnPermissionCallback;
import george.ni.medicare.permission.PermissionAlterDialogFactory;
import george.ni.medicare.permission.PermissionHelper;
import george.ni.medicare.utils.FileUtils;

/**
 * Created by Thinkpad on 2018/4/19.
 * 全部记录页面
 */

public class AllBloodPressureRecordsActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLvBloodSugar;
    private SmartRefreshLayout mSmartRefreshLayout;
    protected List<BloodPressureEntity> mBloodPressureDataList;
    private BloodPressureCheckAdapter mBloodSugarCheckAdapter;
    private EditText mEtYear;
    private EditText mEtMonth;
    private Button mBtSearch;
    private Button mBtExport;
    private int count = 20;
    protected int INTENT_FROM_TYPE;
    public static final String INTENT_FROM = "INTENT_FROM";
    public static final int INTENT_FROM_BLOOD_SUGAR = 0;
    public static final int INTENT_FROM_BLOOD_PRESSURE = 1;
    private String mTableName;
    private ExportRecordsTask mExportRecordTask;
    private PermissionHelper permissionHelper;

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
//        INTENT_FROM_TYPE = getIntent().getIntExtra(INTENT_FROM, INTENT_FROM_BLOOD_SUGAR);
//        if (INTENT_FROM_TYPE == AllBloodPressureRecordsActivity.INTENT_FROM_BLOOD_SUGAR) {
//            mTableName = MedicareRecordDbHelper.tableName_BLODD_SUGAR;
//        } else {
//        }
        mTableName = MedicareRecordDbHelper.tableName_BLODD_PRESSURE;
    }

    private void initViews() {
        mLvBloodSugar = (ListView) findViewById(R.id.lv_blood_sugar);
        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mEtMonth = (EditText) findViewById(R.id.et_month);
        mEtYear = (EditText) findViewById(R.id.et_year);
        mBtSearch = (Button) findViewById(R.id.bt_search);
        mBtExport = (Button) findViewById(R.id.bt_export);
    }

    private void setRefreshLayout() {
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                String year = mEtYear.getText().toString().trim();
                String month = mEtMonth.getText().toString().trim();
                mBloodPressureDataList = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodPressureCheckDatas(mTableName, -1, -1, year, month);
                mBloodSugarCheckAdapter.setData(mBloodPressureDataList);
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
//        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                List<BloodSugarEntity> bloodSugarEntities = MedicareRecordDbHelper.getInstance(mActivity).loadNormalBloodSugarCheckDatas(mCheckDataList.size(),count,null,null);
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
        mBtExport.setOnClickListener(this);
    }

    private void initData() {
        mBloodPressureDataList = new ArrayList<>();
        mBloodSugarCheckAdapter = new BloodPressureCheckAdapter(mActivity, mBloodPressureDataList, mTableName);
        mLvBloodSugar.setAdapter(mBloodSugarCheckAdapter);
        mSmartRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:
                mSmartRefreshLayout.autoRefresh();
                break;
            case R.id.bt_export:
                if (permissionHelper == null) {
                    permissionHelper = PermissionHelper.getInstance(mActivity, new OnPermissionCallback() {
                        @Override
                        public void onPermissionGranted(@NonNull String[] permissionName) {
                            mExportRecordTask = new ExportRecordsTask();
                            mExportRecordTask.execute(null, null, null);
                        }

                        @Override
                        public void onPermissionDeclined(@NonNull String[] permissionName) {

                        }

                        @Override
                        public void onPermissionPreGranted(@NonNull String permissionsName) {
                            mExportRecordTask = new ExportRecordsTask();
                            mExportRecordTask.execute(null, null, null);
                        }

                        @Override
                        public void onPermissionNeedExplanation(@NonNull final String permissionName) {
                            AlertDialog alertDialog = PermissionAlterDialogFactory.getAlertDialog(
                                    AllBloodPressureRecordsActivity.this,
                                    "请求权限",
                                    "允许",
                                    "请开启读写文件权限",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            permissionHelper.requestAfterExplanation(permissionName);
                                        }
                                    }
                            );
                            if (!alertDialog.isShowing()) {
                                alertDialog.show();
                            }
                        }

                        @Override
                        public void onPermissionReallyDeclined(@NonNull String permissionName) {
                            AlertDialog alertDialog = PermissionAlterDialogFactory.getAlertDialog(
                                    AllBloodPressureRecordsActivity.this,
                                    "请求权限",
                                    "允许",
                                    "请开启读写文件权限",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            permissionHelper.openSettingsScreen();
                                        }
                                    }
                            );
                            if (!alertDialog.isShowing()) {
                                alertDialog.show();
                            }

                        }

                        @Override
                        public void onNoPermissionNeeded(@NonNull Object permissionName) {
                            mExportRecordTask = new ExportRecordsTask();
                            mExportRecordTask.execute(null, null, null);
                        }
                    });
                }
                permissionHelper.request(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mExportRecordTask != null) {
            mExportRecordTask.cancel(true);
        }
    }

    class ExportRecordsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<BloodSugarEntity> entities =
                    MedicareRecordDbHelper.getInstance(mActivity).loadAllBloodSugarCheckRecords(mTableName);
            String jsonStr = JSON.toJSONString(entities);
            FileUtils.writeStingToFile(getExportPath(), jsonStr, false);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            File shareFile = new File(getExportPath());
            shareIntent.putExtra(Intent.EXTRA_STREAM,
                    Uri.fromFile(shareFile));
            shareIntent.setType("text/plain");//此处可发送多种文件
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "分享到："));
        }

        String getExportPath() {
            String path;
            File directory = new File(Environment.getExternalStorageDirectory(),"Medicare_george");
            if (!directory.exists()){
                directory.mkdirs();
            }
            path = directory.getAbsolutePath()+File.separator+"BloodPressureRecord.txt";
            return path;
        }
    }
}
