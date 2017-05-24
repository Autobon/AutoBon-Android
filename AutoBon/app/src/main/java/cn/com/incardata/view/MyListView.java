package cn.com.incardata.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class MyListView extends ListView{

	public MyListView(Context context)
	{
		super(context);
	}
	
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//通过这个方法就可以实现了获取item中的view
	public  View getViewByPosition(int pos) {
		final int firstListItemPosition = getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return getAdapter().getView(pos, null, this);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return (getChildAt(childIndex));
		}
	} 
		
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
