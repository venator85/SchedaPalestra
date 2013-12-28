package gs.or.venator.schedapalestra.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

	public static double roundToIncrement(double x, double increment) {
		double y = x / increment;
		double q = Math.round(y);
		return q * increment;
	}

	public static String formatWeight(double weight) {
		return formatWeight(weight, true);
	}

	public static String formatWeight(double weight, boolean addUnit) {
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		formatSymbols.setDecimalSeparator(',');
		formatSymbols.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0.##", formatSymbols);
		df.setGroupingSize(3);
		return df.format(weight) + (addUnit ? " Kg" : "");
	}

	public static String formatPercentage(double perc) {
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		formatSymbols.setDecimalSeparator(',');
		formatSymbols.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0.##", formatSymbols);
		df.setGroupingSize(3);
		return df.format(perc * 100.0) + "%";
	}

	public static Spannable getColoredString(String string, int color) {
		Spannable span = new SpannableString(string);
		span.setSpan(new ForegroundColorSpan(color), 0, span.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return span;
	}

	public static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String doubleToShortString(Double d) {
		if (d == null || Double.isNaN(d)) {
			return "";
		}
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		nf.setGroupingUsed(false);
		return nf.format(d);
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@SuppressWarnings("unchecked")
	public static <T extends View> T findView(View root, int id) {
		return (T) root.findViewById(id);
	}

}
