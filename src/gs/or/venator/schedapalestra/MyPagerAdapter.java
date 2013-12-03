package gs.or.venator.schedapalestra;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {

	private final List<View> pages;

	public MyPagerAdapter(List<View> pages) {
		super();
		this.pages = pages;
	}

	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		View page = pages.get(position);
		((ViewGroup) collection).addView(page, 0);
		return page;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewGroup) collection).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (View) object;
	}

}