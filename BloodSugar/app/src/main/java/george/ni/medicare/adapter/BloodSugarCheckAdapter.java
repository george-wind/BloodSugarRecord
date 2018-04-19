package george.ni.medicare.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import george.ni.medicare.R;
import george.ni.medicare.db.MedicareRecordDbHelper;
import george.ni.medicare.entity.BloodSugarEntity;
import george.ni.medicare.utils.DateUtils;


public class BloodSugarCheckAdapter extends CommonAdapter<BloodSugarEntity> {
    public BloodSugarCheckAdapter(Context context, List<BloodSugarEntity> datas) {
        super(context, datas);
        layoutId = R.layout.item_blood_sugar;
    }

    @Override
    public void convert(ViewHolder holder, final BloodSugarEntity bloodSugarEntity) {
        holder.setText(R.id.tv_date, DateUtils.timeStampToDate(bloodSugarEntity.getTestTimeStamp()));
        holder.setText(R.id.tv_time,DateUtils.timeStampToTime(bloodSugarEntity.getTestTimeStamp()));
        holder.setText(R.id.tv_result,bloodSugarEntity.getResult());
        RelativeLayout rlDelete = holder.getView(R.id.rl_delete);
        rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicareRecordDbHelper.getInstance(context).deleteBloodSugarDataByLogic(bloodSugarEntity);
                datas.remove(bloodSugarEntity);
                BloodSugarCheckAdapter.this.notifyDataSetChanged();
            }
        });
    }
}
