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
import chotot.prect.aptech.zinzamessenger.model.User;

/**
 * Created by ASUS on 02/10/2017.
 */

public class AdapterFriendOnline extends BaseAdapter {
    Context mContext;
    int mLayout;
    List<User> list;

    public AdapterFriendOnline(Context mContext, int mLayout, List<User> list) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ImageView avatarUser = (ImageView)convertView.findViewById(R.id.imgFrendAvatar);
        TextView nameUser = (TextView)convertView.findViewById(R.id.txtNameFriend);
        ImageView statusUser = (ImageView)convertView.findViewById(R.id.imgStatus);
        Picasso.with(mContext).load(list.get(position).getAvatar()).into(avatarUser);
        nameUser.setText(list.get(position).getUsername());
        statusUser.setImageResource(list.get(position).getStatus());
        return convertView;
    }
}
