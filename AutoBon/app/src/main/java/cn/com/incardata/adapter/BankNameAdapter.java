package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import cn.com.incardata.autobon.R;

public class BankNameAdapter extends BaseAdapter{
	private String[] bankArray;
	private Context context;

	public BankNameAdapter(Context context, String[] bankArray) {
		this.context = context;
		this.bankArray = bankArray;
	}

	@Override
	public int getCount() {
		if (bankArray != null && bankArray.length > 0) {
			return bankArray.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return bankArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.spinner_indicator, parent, false);
			TextView text = (TextView) convertView.findViewById(R.id.spinner_indicator);
			text.setText(bankArray[position]);

			convertView.setTag(text);
		}else {
			((TextView)convertView.getTag()).setText(bankArray[position]);
		}
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {  
			convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);  
			CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.spinner_drop_item_text);  
			textView.setText(bankArray[position]);

			convertView.setTag(textView);  
		} else {  
			((CheckedTextView) convertView.getTag()).setText(bankArray[position]);
		}  
		return convertView;  
	}
}
