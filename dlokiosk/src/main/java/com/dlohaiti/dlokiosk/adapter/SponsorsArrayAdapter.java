package com.dlohaiti.dlokiosk.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.SponsorFormActivity;
import com.dlohaiti.dlokiosk.domain.CustomerAccounts;
import com.dlohaiti.dlokiosk.domain.Sponsor;
import com.dlohaiti.dlokiosk.domain.Sponsors;

import java.util.List;


public class SponsorsArrayAdapter extends ArrayAdapter<Sponsor> {
    private final Context context;
    private final List<Sponsor> sponsors;

    public SponsorsArrayAdapter(Context context, Sponsors sponsors) {
        super(context, R.layout.layout_sponsors_list_item, sponsors);
        this.context = context;
        this.sponsors = sponsors;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SponsorViewHolder holder;
        if (view == null) {
            holder = new SponsorViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_sponsors_list_item, parent, false);
            holder.listItem = view.findViewById(R.id.sponsor_list_item);
            holder.sponsorName = (TextView) view.findViewById(R.id.sponsor_name);
            holder.editButton = (ImageButton) view.findViewById(R.id.edit_button);
            view.setTag(holder);
        } else {
            holder = (SponsorViewHolder) view.getTag();
        }
        fillSponsorDetails(position, view, holder);
        handleSponsorEdit(position, view, holder);
        return view;
    }

    private void fillSponsorDetails(int position, View view, SponsorViewHolder holder) {
        Sponsor sponsor = sponsors.get(position);
        holder.sponsorName.setText(sponsor.getName());
        TextView contactName = (TextView) view.findViewById(R.id.contact_name);
        contactName.setText(sponsor.getContactName());
        TextView phoneNumber = (TextView) view.findViewById(R.id.phone_number);
        phoneNumber.setText(sponsor.getPhoneNumber());
        fillCustomerAccount(view, sponsor);
    }

    private void handleSponsorEdit(int position, View view, SponsorViewHolder holder) {
//        holder.editButton = (ImageButton) view.findViewById(R.id.edit_button);
//        if (holder.editButton==null) return;
        holder.editButton.setId(position);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SPONSOR ID ", String.valueOf(view.getId()));
                Sponsor s = sponsors.get(view.getId());
                Intent intent = new Intent(context, SponsorFormActivity.class);
                intent.putExtra("sponsor_id",String.valueOf(s.getId()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void fillCustomerAccount(View view, Sponsor sponsor) {
        TextView accounts = (TextView) view.findViewById(R.id.customers);
        CustomerAccounts customerAccounts = new CustomerAccounts(sponsor.customerAccounts());
        List<String> names = customerAccounts.getContactNames();
        accounts.setText(TextUtils.join(", ",names.toArray()));
    }


    class SponsorViewHolder {
        ImageButton editButton;
        View listItem;
        TextView sponsorName;
    }
}
