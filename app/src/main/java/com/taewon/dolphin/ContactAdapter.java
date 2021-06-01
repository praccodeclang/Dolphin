package com.taewon.dolphin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private List<ContactItem> contactItemList;

    public ContactAdapter(Context context, List<ContactItem> contactItemList) {
        this.context = context;
        this.contactItemList = contactItemList;
    }


    //getters
    @Override
    public int getCount() {
        return contactItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return contactItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.contact_item, null);
        TextView contactName = v.findViewById(R.id.contactName);
        TextView contactDept = v.findViewById(R.id.contactDept);
        contactName.setText(contactItemList.get(i).getContactUserName());
        contactDept.setText(contactItemList.get(i).getContactUserDept());
        return v;
    }
}
