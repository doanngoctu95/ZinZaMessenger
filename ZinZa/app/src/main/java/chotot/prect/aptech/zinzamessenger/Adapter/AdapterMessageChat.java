package chotot.prect.aptech.zinzamessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.Message;

/**
 * Created by ASUS on 02/20/2017.
 */

public class AdapterMessageChat extends BaseAdapter {
    public static final int SENDER = 0;
    public static final int RECIPENT = 1;
    private Context mContext;
    private List<Message> mList;
    private int mLayout;
    public AdapterMessageChat(Context mContext, List<Message> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView content;
        if(mList.get(position).getRecipientOrSenderStatus() == SENDER){
            mLayout = R.layout.layout_sender_message;
            convertView = inflater.inflate(mLayout,null);
            content = (TextView)convertView.findViewById(R.id.text_view_sender_message);
        } else {
            mLayout = R.layout.layout_recipent_message;
            convertView = inflater.inflate(mLayout,null);
            content = (TextView)convertView.findViewById(R.id.text_view_recipient_message);
        }
        content.setText(mList.get(position).getmContent());
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
