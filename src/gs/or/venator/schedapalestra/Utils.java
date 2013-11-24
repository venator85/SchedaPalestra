package gs.or.venator.schedapalestra;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class Utils {

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

}
