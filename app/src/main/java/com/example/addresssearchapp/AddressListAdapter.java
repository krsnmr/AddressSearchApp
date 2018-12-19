package com.example.addresssearchapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddressListAdapter extends ArrayAdapter<Address>  {

    Context mContext;
    private int itemLayout;
    private List<Address> dataList,items, suggestions;
    private static final String TAG = "AddressListAdapter";

    public AddressListAdapter(@NonNull Context context, int resource, List<Address> items) {
        super(context, resource);
        this.mContext = context;
        itemLayout = resource;
        suggestions = new ArrayList<Address>();
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, parent, false);
        }
        Address address = suggestions.get(position);
        if (address != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(address.getAddrName());
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return addressFilter;
    }

    Filter addressFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            //String str = resultValue.toString();
            String str = ((Address) resultValue).getAddrName();
            return str;
        }

        /**
         * При вводе текста формирование списка,который будет в выпадающем списке
         */
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            if (charSequence != null) {
                suggestions.clear();
                Log.d(TAG, "performFiltering1: " + charSequence.toString());

                String addrSearch = charSequence.toString().toLowerCase();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                List<Address> addrResult = dbHelper.getAddrList(addrSearch);

                for (Address addr : addrResult) {
                    suggestions.add(addr);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = addrResult;
                filterResults.count = addrResult.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        /**
         * Заполнение
         */
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Log.d(TAG, "publishResults: filterResults <> null - " + (filterResults != null));

            if (filterResults != null && filterResults.count > 0) {
                List<Address> filterList = (ArrayList<Address>) filterResults.values;
                clear();
                for (Address address : filterList) {
                    add(address);//.getAddrName()
                    notifyDataSetChanged();
                }
            }
        }
    };
}
