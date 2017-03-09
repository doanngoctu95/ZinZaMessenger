package vn.com.zinza.zinzamessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import vn.com.zinza.zinzamessenger.R;
import vn.com.zinza.zinzamessenger.model.FileHistory;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView==null){
            viewHolder= new ViewHolder();
            convertView= inflater.inflate(R.layout.item_history_file,parent,false);

            ImageView imageView= (ImageView) convertView.findViewById(R.id.imFile);
            TextView tvName= (TextView) convertView.findViewById(R.id.tvNameFile);
            TextView tvDate= (TextView) convertView.findViewById(R.id.tvTimeFile);
            ImageView imgOption= (ImageView) convertView.findViewById(R.id.imgOption);

            viewHolder.img=imageView;
            viewHolder.tvName=tvName;
            viewHolder.tvDate=tvDate;
            viewHolder.imgOption=imgOption;

            convertView.setTag(viewHolder);
        }

        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.img.setImageDrawable(getContext().getResources().getDrawable(arrFile.get(position).getImg()));
        viewHolder.tvName.setText(arrFile.get(position).getName()+"");
        viewHolder.tvDate.setText(arrFile.get(position).getDate()+"");
        viewHolder.imgOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id= view.getId();
                switch (id){
                    case R.id.imgOption:
                        PopupMenu popup = new PopupMenu(getContext(), view);
                        popup.getMenuInflater().inflate(R.menu.popup_menu,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.showDetail:
                                        //Or Some other code you want to put here.. This is just an example.
                                        Toast.makeText(getContext(), "Show detail at position " + " : " + position, Toast.LENGTH_LONG).show();
                                        break;
                                    case R.id.delete:
                                        Toast.makeText(getContext(), "Deleted at position " + " : " + position, Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            }
                        });

                        break;
                    default:
                        break;
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        private ImageView img, imgOption;
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
