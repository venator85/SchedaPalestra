package gs.or.venator.schedapalestra;

import android.text.Editable;
import android.text.TextWatcher;

public class VenatorTextWatcher implements TextWatcher {
	private String last;

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public final void afterTextChanged(Editable s) {
		if (last == null && s.length() == 0) {
			last = s.toString();
		}
		if (!s.toString().equals(last)) {
			last = s.toString();
			afterTextChanged_(s);
		}
	}

	public void afterTextChanged_(Editable s) {

	}
}