package gs.or.venator.schedapalestra.view;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

	public static class Page {
		public View page;
		public String title;

		public Page(View page, String title) {
			super();
			this.page = page;
			this.title = title;
		}
	}

	private final List<Page> pages;

	public ViewPagerAdapter(List<Page> pages) {
		super();
		this.pages = pages;
	}

	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return pages.get(position).title;

	}

	@Override
	public Object instantiateItem(View collection, int position) {
		View page = pages.get(position).page;
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