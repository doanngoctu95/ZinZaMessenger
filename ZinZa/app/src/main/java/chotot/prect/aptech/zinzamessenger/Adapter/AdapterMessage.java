package chotot.prect.aptech.zinzamessenger.Adapter;

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

/**
 * Created by ASUS on 02/13/2017.
 */

public class AdapterMessage extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<Message> mListMessage;

    public AdapterMessage(Context mContext, int mLayout, List<Message> mListMessage) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mListMessage = mListMessage;
    }

    @Override
    public int getCount() {
        return mListMessage.size();
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
        convertView = inflater.inflate(mLayout,null);
        ImageView avatarUser = (ImageView)convertView.findViewById(R.id.imgAvatar);
        TextView nameUser = (TextView)convertView.findViewById(R.id.txtName);
        TextView contentMessage = (TextView)convertView.findViewById(R.id.txtContent);
        TextView timeMessage = (TextView)convertView.findViewById(R.id.txtTime);
        String urlAvatarRecipient = "";
        String nameRecipient = "";
        String time = "";
        if(mListMessage.get(position).getIdRecipient()==2){
            urlAvatarRecipient = "http://4.bp.blogspot.com/-8Kef_ymC9t0/U2TgDlHZwiI/AAAAAAAACi8/XzNlxhXtR80/s1600/anh+avatar+dep+4.jpg";
            nameRecipient = "Văn Nam";
            time = "13/02";
        } else if(mListMessage.get(position).getIdRecipient()==3){
            nameRecipient = "Nhung";
            time = "14/02";
            urlAvatarRecipient = "http://1.bp.blogspot.com/-U96MqFNsOGA/Uzv6DLtpsHI/AAAAAAAABHs/P7lp0Kc-hYg/s1600/hinh+avatar+dep+6.jpg";
        }
        Picasso.with(mContext).load(urlAvatarRecipient).into(avatarUser);
        nameUser.setText(nameRecipient);
        contentMessage.setText(mListMessage.get(position).getContent());
        timeMessage.setText(time);

        return convertView;
    }
}
