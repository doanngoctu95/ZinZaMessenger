package chotot.prect.aptech.zinzamessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import chotot.prect.aptech.zinzamessenger.R;
import chotot.prect.aptech.zinzamessenger.model.FileHistory;

/**
 * Created by dell on 17/02/2017.
 */

public class AdapterHistoryFile  extends ArrayAdapter<FileHistory> {
    private ArrayList<FileHistory> arrFile;
    private ArrayList<FileHistory> searchList;
    private LayoutInflater inflater;

    public AdapterHistoryFile(Context context, int resource, ArrayList<FileHistory> objects) {
        super(context, resource, objects);
        arrFile= objects;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        searchList= new ArrayList<>();
        searchList.addAll(arrFile);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView==null){
            viewHolder= new ViewHolder();
            convertView= inflater.inflate(R.layout.item_history_file,parent,false);

            ImageView imageView= (ImageView) convertView.findViewById(R.id.imFile);
            TextView tvName= (TextView) convertView.findViewById(R.id.tvNameFile);
            TextView tvDate= (TextView) convertView.findViewById(R.id.tvTimeFile);

            viewHolder.img=imageView;
            viewHolder.tvName=tvName;
            viewHolder.tvDate=tvDate;

            convertView.setTag(viewHolder);
        }

        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.img.setImageDrawable(getContext().getResources().getDrawable(arrFile.get(position).getImg()));
        viewHolder.tvName.setText(arrFile.get(position).getName()+"");
        viewHolder.tvDate.setText(arrFile.get(position).getDate()+"");

        return convertView;
    }

    class ViewHolder{
        private ImageView img;
        private TextView tvName,tvDate;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrFile.clear();
        if (charText.length() == 0) {
            arrFile.addAll(searchList);
        } else {
            for (FileHistory s : searchList) {
                String nameSearch= s.getName();
                if (nameSearch.toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrFile.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }
}
