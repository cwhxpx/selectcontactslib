package co.devhub.selectcontactslib.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.devhub.selectcontactslib.R;

/**
 * Created by Administrator on 2016/1/8.
 */
public class UserAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<User> users;
    public UserAdapter(Context context) {
        this.mContext = context;
        users = new ArrayList<>();
    }

    public void setData(List<User> data){
        this.users.clear();
        this.users.addAll(data);
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();;

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);

        viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
        viewHolder.tvName = (TextView) convertView.findViewById(R.id.contact);
        viewHolder.cbSelected = (CheckBox) convertView.findViewById(R.id.chkbox);
        viewHolder.tvItem = (LinearLayout) convertView.findViewById(R.id.item);
        convertView.setTag(viewHolder);

        //set up tvName
        String contact = users.get(position).getContactName() + "("
                + users.get(position).getPhoneNumber() + ")";
        viewHolder.tvName.setText(contact);

        //set up checkbox :
        // 1. binding position with checkbox by packing position in checkbox' Tag
        // 2. checked for pre-selected contact
        viewHolder.cbSelected.setTag(position);
        viewHolder.cbSelected.setChecked(users.get(position).isSelected());
        //3. set up change listener for this checkbox
        viewHolder.cbSelected.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton chkBox, boolean b) {
                        //chkBoxId is the position where the checkbox sitting on
                        int chkBoxId = (int)chkBox.getTag();
                        if (position == chkBoxId) {
                            users.get(chkBoxId).setSelected(b);
                        }
                    }
                }
        );


        //当前的item的title与上一个item的title不同的时候会显示title(A,B,C......)
        if(position == getFirstLetterPosition(position) &&
           !users.get(position).getLetter().equals("@"))
        {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(users.get(position).getLetter().toUpperCase());
        }else {
            viewHolder.tvTitle.setVisibility(View.GONE);
        }

        //set up item click listener call back
        viewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,
                               users.get(position).getContactName(),
                               Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    /**
     * 顺序遍历所有元素．找到position对应的title是什么（A,B,C?）然后找这个title下的第一个item对应的position
     *
     * @param position
     * @return
     */
    private int getFirstLetterPosition(int position) {

        //String letter = users.get(position).getLetter();
        char letter =  users.get(position).getLetter().charAt(0);
        //int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
        int size = users.size();
        for (int i = 0; i < size; i++) {
            //if(cnAscii == users.get(i).getLetter().charAt(0)){
            if(letter == users.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 顺序遍历所有元素．找到letter下的第一个item对应的position
     * @param letter
     * @return
     */
    public int getFirstLetterPosition(String letter){
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if(letter.charAt(0) == users.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvTitle;
        ImageView tvAvatar;
        LinearLayout tvItem;
        TextView tvNumber;

        CheckBox cbSelected;
    }
}