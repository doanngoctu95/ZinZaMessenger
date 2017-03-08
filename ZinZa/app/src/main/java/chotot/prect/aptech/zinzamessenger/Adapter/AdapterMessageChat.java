package chotot.prect.aptech.zinzamessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.Message;
import chotot.prect.aptech.zinzamessenger.utils.Helper;
import chotot.prect.aptech.zinzamessenger.utils.Utils;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

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
    public void addMessage(Message message){
        mList.add(message);
        notifyDataSetChanged();
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
        EmojiconTextView content;
        TextView time;
        ImageView imageContent;
        String type = mList.get(position).getmType();
        if(type.equals(Utils.TEXT) && mList.get(position).getRecipientOrSenderStatus() == SENDER){
            mLayout = R.layout.layout_sender_message;
            convertView = inflater.inflate(mLayout,null);
            content = (EmojiconTextView)convertView.findViewById(R.id.text_view_sender_message);
            time = (TextView)convertView.findViewById(R.id.text_view_time_sender);
            content.setText(mList.get(position).getmContent());
        } else if(type.equals(Utils.IMAGE) && mList.get(position).getRecipientOrSenderStatus() == SENDER) {
            mLayout = R.layout.layout_sender_image;
            convertView = inflater.inflate(mLayout,null);
            imageContent = (ImageView) convertView.findViewById(R.id.img_message_sender);
            time = (TextView)convertView.findViewById(R.id.text_view_time_sender_image);
            Picasso.with(mContext).load(mList.get(position).getmContent()).into(imageContent);
        } else if(type.equals(Utils.IMAGE) && mList.get(position).getRecipientOrSenderStatus() == RECIPENT) {
            mLayout = R.layout.layout_recipent_image;
            convertView = inflater.inflate(mLayout,null);
            imageContent = (ImageView) convertView.findViewById(R.id.img_message_recipent);
            time = (TextView)convertView.findViewById(R.id.text_view_time_recipent_image);
            Picasso.with(mContext).load(mList.get(position).getmContent()).into(imageContent);
        } else {
            mLayout = R.layout.layout_recipent_message;
            convertView = inflater.inflate(mLayout,null);
            content = (EmojiconTextView)convertView.findViewById(R.id.text_view_recipient_message);
            time = (TextView)convertView.findViewById(R.id.text_view_time_recipent);
            content.setText(mList.get(position).getmContent());
        }
        time.setText(Helper.convertTime(mList.get(position).getmTime()));
        return convertView;
    }
    public void cleanUp(){
        mList.clear();
    }

}
