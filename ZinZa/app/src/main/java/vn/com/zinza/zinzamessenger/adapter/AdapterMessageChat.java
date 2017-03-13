package vn.com.zinza.zinzamessenger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import vn.com.zinza.zinzamessenger.R;
import vn.com.zinza.zinzamessenger.model.Message;
import vn.com.zinza.zinzamessenger.utils.Helper;
import vn.com.zinza.zinzamessenger.utils.Utils;

/**
 * Created by ASUS on 02/20/2017.
 */

public class AdapterMessageChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SENDER = 0;
    public static final int RECIPENT = 1;
    public static final int SENDER_TEXT = 2;
    public static final int SENDER_IMAGE = 3;
    public static final int RECIPENT_TEXT = 4;
    public static final int RECIPENT_IMAGE = 5;
    private Context mContext;
    private List<Message> mList;
    private int mLayout;

    public AdapterMessageChat(Context mContext, List<Message> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void addMessage(Message message) {
        mList.add(message);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case SENDER_TEXT:
                View viewSenderText = inflater.inflate(R.layout.layout_sender_message, parent, false);
                viewHolder = new ViewHolderSenderText(viewSenderText);
                break;
            case SENDER_IMAGE:
                View viewSenderImage = inflater.inflate(R.layout.layout_sender_image, parent, false);
                viewHolder = new ViewHolderSenderImage(viewSenderImage);
                break;
            case RECIPENT_TEXT:
                View viewRecipientText = inflater.inflate(R.layout.layout_recipent_message, parent, false);
                viewHolder = new ViewHolderRecipientText(viewRecipientText);
                break;
            case RECIPENT_IMAGE:
                View viewRecipientImage = inflater.inflate(R.layout.layout_recipent_image, parent, false);
                viewHolder = new ViewHolderRecipientImage(viewRecipientImage);
                break;
            default:
                View viewSenderDefault = inflater.inflate(R.layout.layout_sender_message, parent, false);
                viewHolder = new ViewHolderSenderText(viewSenderDefault);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SENDER_TEXT:
                ViewHolderSenderText viewHolderSenderText = (ViewHolderSenderText) holder;
                configureMessageSenderView(viewHolderSenderText, position);
                break;
            case SENDER_IMAGE:
                ViewHolderSenderImage viewHolderSenderImage = (ViewHolderSenderImage) holder;
                configureImageSenderView(viewHolderSenderImage, position);
                break;
            case RECIPENT_TEXT:
                ViewHolderRecipientText viewHolderRecipientText = (ViewHolderRecipientText) holder;
                configureMessageRecipientView(viewHolderRecipientText, position);
                break;
            case RECIPENT_IMAGE:
                ViewHolderRecipientImage viewHolderRecipientImage = (ViewHolderRecipientImage) holder;
                configureImageRecipientView(viewHolderRecipientImage, position);
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        String type = mList.get(position).getmType();
        int status = mList.get(position).getRecipientOrSenderStatus();
        if (status == SENDER_TEXT && type.equals(Utils.TEXT)) {
            return SENDER_TEXT;
        } else if (status == SENDER_IMAGE && type.equals(Utils.IMAGE)) {
            return SENDER_IMAGE;
        } else if (status == RECIPENT_TEXT && type.equals(Utils.TEXT)) {
            return RECIPENT_TEXT;
        } else {
            return RECIPENT_IMAGE;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void configureMessageSenderView(ViewHolderSenderText viewHolderSender, int position) {
        Message senderFireMessage = mList.get(position);
        viewHolderSender.getSenderContent().setText(senderFireMessage.getmContent());
        viewHolderSender.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
    }

    private void configureImageSenderView(ViewHolderSenderImage viewHolderSenderImage, int position) {
        Message senderFireMessage = mList.get(position);
        Picasso.with(mContext)
                .load(senderFireMessage
                        .getmContent())
                .placeholder(R.drawable.place_hoder)
                .into(viewHolderSenderImage.getSenderImage());
        viewHolderSenderImage.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
    }

    private void configureMessageRecipientView(ViewHolderRecipientText viewHolderMessageRecipient, int position) {
        Message senderFireMessage = mList.get(position);
        viewHolderMessageRecipient.getRecipientContent().setText(senderFireMessage.getmContent());
        viewHolderMessageRecipient.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
    }

    private void configureImageRecipientView(ViewHolderRecipientImage viewHolderRecipientImage, int position) {
        Message senderFireMessage = mList.get(position);
        Picasso.with(mContext).load(senderFireMessage.getmContent()).into(viewHolderRecipientImage.getRecipientImage());
        viewHolderRecipientImage.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
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

    public class ViewHolderSenderImage extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView time;

        public ViewHolderSenderImage(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_message_sender);
            time = (TextView) itemView.findViewById(R.id.text_view_time_sender_image);
        }

        public ImageView getSenderImage() {
            return imageView;
        }

        public TextView getTime() {
            return time;
        }
    }

    public class ViewHolderRecipientImage extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView time;

        public ViewHolderRecipientImage(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_message_recipent);
            time = (TextView) itemView.findViewById(R.id.text_view_time_recipent_image);
        }

        public ImageView getRecipientImage() {
            return imageView;
        }

        public TextView getTime() {
            return time;
        }
    }


}
