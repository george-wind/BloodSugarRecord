package george.ni.medicare.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewHolder {

    private SparseArray<View> mViews;
    public int position;
    private View mConvertView;
    protected Context context;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.context = context;
        this.position = position;
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int
            layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.position = position;
            return holder;
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 设置选择项
     *
     * @param viewId
     * @param checked
     * @return
     */
    public ViewHolder setChecked(int viewId, boolean checked) {
        ((CheckBox) getView(viewId)).setChecked(checked);
        return this;
    }

    /**
     * 设置选择项
     *
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnCheckedChangedListener(int viewId, CompoundButton
            .OnCheckedChangeListener listener) {
        ((CheckBox) getView(viewId)).setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }


}
