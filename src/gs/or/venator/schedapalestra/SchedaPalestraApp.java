package gs.or.venator.schedapalestra;

import gs.or.venator.schedapalestra.core.BrzyckiOneRepMax;
import gs.or.venator.schedapalestra.core.ColuzziOneRepMax;
import gs.or.venator.schedapalestra.core.OneRepMaxMethod;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;

public class SchedaPalestraApp extends Application {

	private static SchedaPalestraApp instance;

	private List<OneRepMaxMethod> oneRepMaxMethods;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initOneRepMaxMethods();
	}

	protected void initOneRepMaxMethods() {
		oneRepMaxMethods = new ArrayList<OneRepMaxMethod>(2);
		oneRepMaxMethods.add(new ColuzziOneRepMax());
		oneRepMaxMethods.add(new BrzyckiOneRepMax());
	}

	public static SchedaPalestraApp getInstance() {
		return instance;
	}

	public static Context getContext() {
		return instance;
	}

	public List<OneRepMaxMethod> getOneRepMaxMethods() {
		return oneRepMaxMethods;
	}

}
