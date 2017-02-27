package chotot.prect.aptech.zinzamessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.Friend;
import chotot.prect.aptech.zinzamessenger.model.User;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ASUS on 02/27/2017.
 */

public class AdapterFriendSearch extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<User> mListUser;
    private String idCurrentUser;
    private DatabaseReference mReference;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public AdapterFriendSearch(Context mContext, int mLayout, List<User> mListUser, String idCurUser) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mListUser = mListUser;
        idCurrentUser=idCurUser;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getCount() {
        return mListUser.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mLayout,null);
        ImageView avatarUser = (ImageView)convertView.findViewById(R.id.imgFriendSearch);
        TextView nameUser = (TextView)convertView.findViewById(R.id.txtNameFriendSearch);
        Button btnAddFr= (Button) convertView.findViewById(R.id.btnAddFriend);
        if(!mListUser.get(position).getmAvatar().equals("")){
            Picasso.with(mContext).load(mListUser.get(position).getmAvatar()).into(avatarUser);
        }
        nameUser.setText(mListUser.get(position).getmUsername());
        btnAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idFr= mListUser.get(position).getmId();
                String idTblFriend="";
                final String tblContact=idCurrentUser+"-"+idFr;
                idTblFriend= tblContact;
                final Friend friend= new Friend(idTblFriend,idCurrentUser,idFr,createAt());


                mReference= mDatabase.getInstance().getReference();
                mReference.orderByChild("tblFriend").equalTo(tblContact).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0){
                            Toast.makeText(getApplicationContext(),"no add",Toast.LENGTH_LONG).show();
                        }
                        else {
                            mReference= mDatabase.getInstance().getReference("tblFriend");
                            mReference.child(tblContact).setValue(friend);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return convertView;
    }


    private String createAt(){
        return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }
}
