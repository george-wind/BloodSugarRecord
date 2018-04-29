package george.ni.medicare.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import george.ni.medicare.R;
import george.ni.medicare.db.MedicareRecordDbHelper;
import george.ni.medicare.entity.BloodPressureEntity;
import george.ni.medicare.utils.DateUtils;


public class BloodPressureCheckAdapter extends CommonAdapter<BloodPressureEntity> {
    private String tableName;
    public BloodPressureCheckAdapter(Context context, List<BloodPressureEntity> datas, String tableName) {
        super(context, datas);
        layoutId = R.layout.item_blood_sugar;
        this.tableName = tableName;
    }

    @Override
    public void convert(ViewHolder holder, final BloodPressureEntity entity) {
        holder.setText(R.id.tv_date, DateUtils.timeStampToDate(entity.getTestTimeStamp()));
        holder.setText(R.id.tv_time,DateUtils.timeStampToTime(entity.getTestTimeStamp()));
        holder.setText(R.id.tv_result,entity.getHighResult()+"/"+entity.getLowResult());
        RelativeLayout rlDelete = holder.getView(R.id.rl_delete);
        rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicareRecordDbHelper.getInstance(context).deleteBloodPressureCheckDataByLogic(tableName,entity);
                datas.remove(entity);
                BloodPressureCheckAdapter.this.notifyDataSetChanged();
            }
        });
    }
}
