package co.devhub.selectcontactslib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import co.devhub.retrievecontacts.Contact;
import co.devhub.retrievecontacts.RetrieveContacts;
import co.devhub.selectcontactslib.utils.CompareSort;
import co.devhub.selectcontactslib.utils.Hanzi2Pinyin;
import co.devhub.selectcontactslib.utils.SideBarView;
import co.devhub.selectcontactslib.utils.User;
import co.devhub.selectcontactslib.utils.UserAdapter;


public class SelectContacts extends ActionBarActivity implements SideBarView.LetterSelectListener {

    //contacts to be displayed packed in intent by calling activity
    ArrayList<User> mContactsIn = new ArrayList<>();
    //Phone number pre-selected before packed in intent by calling activity
    //and this variable also contains new selected by user in this activity
    String[] mPreselectedPhoneNumber;
    private static final int START_NUMBER_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcontacts);

        setCustomActionBar();

        //extract contacts and preselected number from received intent
        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                //mContactsIn = bundle.getParcelableArrayList("contacts");
                mPreselectedPhoneNumber = bundle.getStringArray("preselected-number");
            }
        }

        init();
    }

    ListView mListview;
    UserAdapter mAdapter;
    TextView mTip;
    private void init() {
        mListview = (ListView) findViewById(R.id.listview);
        SideBarView sideBarView = (SideBarView) findViewById(R.id.sidebarview);
        mTip = (TextView) findViewById(R.id.tip);

        //retrieving contacts from phone
        ArrayList<Contact> contacts = new RetrieveContacts(this).getContacts();


        //添加数据到Arraylist
        if (contacts != null) {
            for (Contact contact: contacts) {
                User user = new User();
                user.setContactName(contact.getContactName());
                String firstSpell =
                        Hanzi2Pinyin.cn2Spell(user.getContactName());
                String subString = firstSpell.substring(0, 1).toUpperCase();
                if (subString.matches("[A-Z]")) {
                    user.setLetter(subString);
                } else {
                    user.setLetter("#");
                }

                user.setPhoneNumber(contact.getPhoneNumber());
                user.setSelected(false);
                mContactsIn.add(user);
            }
        }


        //排序
        Collections.sort(mContactsIn, new CompareSort());
        //Pre-set contacts for selected
        List<String> tmpList = Arrays.asList(mPreselectedPhoneNumber);
        for (User u: mContactsIn) {
            if ((u.getPhoneNumber() != null) &&
                 tmpList.contains(u.getPhoneNumber())){
                u.setSelected(true);
            }
        }

        //设置数据
        mAdapter = new UserAdapter(this);
        mAdapter.setData(mContactsIn);
        mListview.setAdapter(mAdapter);

        //设置回调
        sideBarView.setOnLetterSelectListen(this);
    }

    @Override
    public void onLetterSelected(String letter) {
        setListviewPosition(letter);
        mTip.setText(letter);
        mTip.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLetterChanged(String letter) {
        setListviewPosition(letter);
        mTip.setText(letter);
    }

    @Override
    public void onLetterReleased(String letter) {
        mTip.setVisibility(View.GONE);
    }

    private void setListviewPosition(String letter){
        int firstLetterPosition = mAdapter.getFirstLetterPosition(letter);
        if(firstLetterPosition != -1){
            mListview.setSelection(firstLetterPosition);
        }
    }

    //custome action bar layput, title and add back control
    private void setCustomActionBar() {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        ImageButton bkBtn = (ImageButton) mActionBarView.findViewById(R.id.img_bt_back);
        bkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    //back button on action bar triggering
    @Override
    public void onBackPressed() {
        //pickup all selected phone number from mContactsIn
        //update mPreselectedPhoneNumber
        ArrayList<String> tmp = new ArrayList<>();
        for (String number: mPreselectedPhoneNumber) {
            tmp.add(number);
        }
        for (User user: mContactsIn) {
            if (user.isSelected()) {
                //If this number has been selected
                //Adding it if it isn't contained in pre-selected set
                if(!tmp.contains(user.getPhoneNumber())) {
                    tmp.add(user.getPhoneNumber());
                }
            }else {
                //If this number has been de-selected
                //Removing it if it is contained in pre-selected set
                if(tmp.contains(user.getPhoneNumber())) {
                    tmp.remove(user.getPhoneNumber());
                }
            }
        }
        mPreselectedPhoneNumber = tmp.toArray(new String[0]);

        //return selected phone numbers
        Intent intent = new Intent();
        intent.putExtra("phone-number-selected", mPreselectedPhoneNumber);
        this.setResult(RESULT_OK, intent);

        super.onBackPressed();
    }
}