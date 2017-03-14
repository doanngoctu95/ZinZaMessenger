package vn.com.zinza.zinzamessenger.adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import vn.com.zinza.zinzamessenger.R;
import vn.com.zinza.zinzamessenger.downloadfirebase.Download;
import vn.com.zinza.zinzamessenger.downloadfirebase.DownloadService;
import vn.com.zinza.zinzamessenger.model.Message;
import vn.com.zinza.zinzamessenger.utils.Helper;
import vn.com.zinza.zinzamessenger.utils.Utils;

import static vn.com.zinza.zinzamessenger.activity.ChattingActivity.MESSAGE_PROGRESS;

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
    public static final int PERMISSION_REQUEST_CODE = 1;
    private Context mContext;
    private List<Message> mList;
    private int mLayout;

    public AdapterMessageChat(Context mContext, List<Message> mList) {
        this.mContext = mContext;
        this.mList = mList;
        registerReceiver();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case SENDER_TEXT:
                ViewHolderSenderText viewHolderSenderText = (ViewHolderSenderText) holder;
                configureMessageSenderView(viewHolderSenderText, position);
                break;
            case SENDER_IMAGE:
                ViewHolderSenderImage viewHolderSenderImage = (ViewHolderSenderImage) holder;
                configureImageSenderView(viewHolderSenderImage, position);
                ImageView img = (ImageView)viewHolderSenderImage.getSenderImage();
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDetailImage(mList.get(position).getmContent());
                    }
                });
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showDetailImage(mList.get(position).getmContent());
//
//                    }
//                });
                break;
            case RECIPENT_TEXT:
                ViewHolderRecipientText viewHolderRecipientText = (ViewHolderRecipientText) holder;
                configureMessageRecipientView(viewHolderRecipientText, position);
                break;
            case RECIPENT_IMAGE:
                ViewHolderRecipientImage viewHolderRecipientImage = (ViewHolderRecipientImage) holder;
                configureImageRecipientView(viewHolderRecipientImage, position);
                ImageView img2 = (ImageView)viewHolderRecipientImage.getRecipientImage();
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDetailImage(mList.get(position).getmContent());
                    }
                });
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showDetailImage(mList.get(position).getmContent());
//                    }
//                });
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

    private void configureImageSenderView(ViewHolderSenderImage viewHolderSenderImage, final int position) {
        Message senderFireMessage = mList.get(position);
        Glide.with(mContext)
                .load(senderFireMessage
                        .getmContent())
                .placeholder(R.drawable.place_hoder)
                .crossFade()
                .into(viewHolderSenderImage.getSenderImage());
        viewHolderSenderImage.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
    }

    private void configureMessageRecipientView(ViewHolderRecipientText viewHolderMessageRecipient, int position) {
        Message senderFireMessage = mList.get(position);
        viewHolderMessageRecipient.getRecipientContent().setText(senderFireMessage.getmContent());
        viewHolderMessageRecipient.getTime().setText(Helper.convertTime(senderFireMessage.getmTime()));
    }

    private void configureImageRecipientView(ViewHolderRecipientImage viewHolderRecipientImage, final int position) {
        Message senderFireMessage = mList.get(position);
        Glide.with(mContext)
                .load(senderFireMessage.getmContent())
                .placeholder(R.drawable.place_hoder)
                .crossFade()
                .into(viewHolderRecipientImage.getRecipientImage());
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

    private void showDetailImage(final String url) {
        final Dialog nagDialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(true);
        nagDialog.setContentView(R.layout.detail_image);
        Button btnDownload = (Button) nagDialog.findViewById(R.id.btnDownloadImage);
        ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.imgDetail);
        Glide.with(mContext).load(url).crossFade().placeholder(R.drawable.place_hoder).into(ivPreview);
        final String finalURl = url.substring(url.lastIndexOf("apis.com")+8);
        Utils.FIREBASE_END_URL = finalURl;
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
                startDownload();
            }
        });
        nagDialog.show();
    }

    private void startDownload(){

        Intent intent = new Intent(mContext,DownloadService.class);
        mContext.startService(intent);

    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");
                if(download.getProgress() == 100){

                } else {

                }
            }
        }
    };

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

//    private void requestPermission(){
//
//        ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
//
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    startDownload();
//                } else {
//
//
//                }
//                break;
//        }
//    }


}
