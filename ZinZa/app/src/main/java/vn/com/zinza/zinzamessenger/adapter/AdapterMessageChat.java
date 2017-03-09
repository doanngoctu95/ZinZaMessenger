package vn.com.zinza.zinzamessenger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.com.zinza.zinzamessenger.R;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import vn.com.zinza.zinzamessenger.model.Message;
import vn.com.zinza.zinzamessenger.utils.Helper;

/**
 * Created by ASUS on 02/20/2017.
 */

public class AdapterMessageChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int SENDER = 0;
    public static final int RECIPENT = 1;
    private Context mContext;
    private List<Message> mList;
    private int mLayout;

    public AdapterMessageChat(Context mContext, List<Message> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void addMessage(Message message) {
        mList.add(message);
        notifyItemInserted(getItemCount()-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case SENDER:
                View viewSender = inflater.inflate(R.layout.layout_sender_message,parent,false);
                viewHolder = new ViewHolderSenderText(viewSender);
                break;
            case RECIPENT:
                View viewRecipient = inflater.inflate(R.layout.layout_recipent_message, parent, false);
                viewHolder=new ViewHolderRecipientText(viewRecipient);
                break;
            default:
                View viewSenderDefault = inflater.inflate(R.layout.layout_sender_message, parent, false);
                viewHolder= new ViewHolderSenderText(viewSenderDefault);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case SENDER:
                ViewHolderSenderText viewHolderSenderText = (ViewHolderSenderText)holder;
                configureSenderView(viewHolderSenderText,position);
                break;
            case RECIPENT:
                ViewHolderRecipientText viewHolderRecipientText = (ViewHolderRecipientText)holder;
                configureRecipientView(viewHolderRecipientText,position);
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getRecipientOrSenderStatus() == SENDER) {
            return SENDER;
        } else {
            return RECIPENT;
        }
    }
//
//    @Override
//    public int getCount() {
//        return mList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        EmojiconTextView content;
//        TextView time;
//        ImageView imageContent;
//        String type = mList.get(position).getmType();
//        if(type.equals(Utils.TEXT) && mList.get(position).getRecipientOrSenderStatus() == SENDER){
//            mLayout = R.layout.layout_sender_message;
//            convertView = inflater.inflate(mLayout,null);
//            content = (EmojiconTextView)convertView.findViewById(R.id.text_view_sender_message);
//            time = (TextView)convertView.findViewById(R.id.text_view_time_sender);
//            content.setText(mList.get(position).getmContent());
//        } else if(type.equals(Utils.IMAGE) && mList.get(position).getRecipientOrSenderStatus() == SENDER) {
//            mLayout = R.layout.layout_sender_image;
//            convertView = inflater.inflate(mLayout,null);
//            imageContent = (ImageView) convertView.findViewById(R.id.img_message_sender);
//            time = (TextView)convertView.findViewById(R.id.text_view_time_sender_image);
//            Picasso.with(mContext).load(mList.get(position).getmContent()).into(imageContent);
//        } else if(type.equals(Utils.IMAGE) && mList.get(position).getRecipientOrSenderStatus() == RECIPENT) {
//            mLayout = R.layout.layout_recipent_image;
//            convertView = inflater.inflate(mLayout,null);
//            imageContent = (ImageView) convertView.findViewById(R.id.img_message_recipent);
//            time = (TextView)convertView.findViewById(R.id.text_view_time_recipent_image);
//            Picasso.with(mContext).load(mList.get(position).getmContent()).into(imageContent);
//        } else {
//            mLayout = R.layout.layout_recipent_message;
//            convertView = inflater.inflate(mLayout,null);
//            content = (EmojiconTextView)convertView.findViewById(R.id.text_view_recipient_message);
//            time = (TextView)convertView.findViewById(R.id.text_view_time_recipent);
//            content.setText(mList.get(position).getmContent());
//        }
//        time.setText(Helper.convertTime(mList.get(position).getmTime()));
//
//        return convertView;
//    }
    private void configureSenderView(ViewHolderSenderText viewHolderSender, int position) {
        Message senderFireMessage = mList.get(position);
        viewHolderSender.getSenderContent().setText(senderFireMessage.getmContent());
        viewHolderSender.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
    }

    private void configureRecipientView(ViewHolderRecipientText viewHolderRecipient, int position) {
        Message senderFireMessage = mList.get(position);
        viewHolderRecipient.getRecipientContent().setText(senderFireMessage.getmContent());
        viewHolderRecipient.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
    }

    public void cleanUp() {
        mList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolderSenderText extends RecyclerView.ViewHolder {
        EmojiconTextView content;
        TextView time;

        public ViewHolderSenderText(View itemView) {
            super(itemView);
            content = (EmojiconTextView) itemView.findViewById(R.id.text_view_sender_message);
            time = (TextView) itemView.findViewById(R.id.text_view_time_sender);
        }

        public EmojiconTextView getSenderContent() {
            return content;
        }

        public TextView getTime() {
            return time;
        }
    }

    public class ViewHolderRecipientText extends RecyclerView.ViewHolder {
        EmojiconTextView content;
        TextView time;

        public ViewHolderRecipientText(View itemView) {
            super(itemView);
            content = (EmojiconTextView) itemView.findViewById(R.id.text_view_recipient_message);
            time = (TextView) itemView.findViewById(R.id.text_view_time_recipent);
        }

        public EmojiconTextView getRecipientContent() {
            return content;
        }

        public TextView getTime() {
            return time;
        }
    }


}
