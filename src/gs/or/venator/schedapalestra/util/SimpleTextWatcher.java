package gs.or.venator.schedapalestra.util;

import android.text.Editable;
import android.text.TextWatcher;

public class SimpleTextWatcher implements TextWatcher {

	@Override
	public final void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public final void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public final void afterTextChanged(Editable s) {
		afterTextChanged_(s.toString());
	}

	public void afterTextChanged_(String s) {

	}

}