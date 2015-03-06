package com.think_different.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.think_different.R;

import java.util.List;

/**
 * Created by oceancx on 15/3/5.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {


    private List<ContactInfo> contactInfoList;

    public ContactAdapter(List<ContactInfo> contactInfoList) {
        this.contactInfoList = contactInfoList;
    }



    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.cardlayout, parent, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactInfoList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.vSurname.setText(ci.surname);
        contactViewHolder.vEmail.setText(ci.email);
        contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);

    }

    @Override
    public int getItemCount() {
        return contactInfoList.size();
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }

    public class ContactInfo {
        protected String name;
        protected String surname;
        protected String email;
        protected static final String NAME_PREFIX = "Name_";
        protected static final String SURNAME_PREFIX = "Surname_";
        protected static final String EMAIL_PREFIX = "email_";
    }

}
