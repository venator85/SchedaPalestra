package gs.or.venator.schedapalestra;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontManager {

	public static final int ROBOTO_LIGHT = 0;
	public static final int ROBOTO_REGULAR = 1;
	public static final int ROBOTO_BOLD = 2;
	public static final int ROBOTO_CONDENSED_LIGHT = 3;
	public static final int ROBOTO_CONDENSED_REGULAR = 4;
	public static final int ROBOTO_CONDENSED_BOLD = 5;

	private Typeface robotoBold;
	private Typeface robotoLight;
	private Typeface robotoRegular;
	private Typeface robotoCondensedBold;
	private Typeface robotoCondensedLight;
	private Typeface robotoCondensedRegular;
	private AssetManager am;

	// http://en.wikipedia.org/wiki/Initialization_on_demand_holder_idiom
	private static class InstanceHolder {
		private static final FontManager INSTANCE = new FontManager();
	}

	private FontManager() {
		am = SchedaPalestraApp.getContext().getAssets();
	}

	public static FontManager getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public Typeface getTypeface(int id) {
		if (id == ROBOTO_LIGHT) {
			return getRobotoLight();
		} else if (id == ROBOTO_REGULAR) {
			return getRobotoRegular();
		} else if (id == ROBOTO_BOLD) {
			return getRobotoBold();
		} else if (id == ROBOTO_CONDENSED_LIGHT) {
			return getRobotoCondensedLight();
		} else if (id == ROBOTO_CONDENSED_REGULAR) {
			return getRobotoCondensedRegular();
		} else if (id == ROBOTO_CONDENSED_BOLD) {
			return getRobotoCondensedBold();
		}
		return null;
	}

	public Typeface getRobotoBold() {
		if (robotoBold == null) {
			robotoBold = Typeface.createFromAsset(am, "Roboto-Bold.ttf");
		}
		return robotoBold;
	}

	public Typeface getRobotoLight() {
		if (robotoLight == null) {
			robotoLight = Typeface.createFromAsset(am, "Roboto-Light.ttf");
		}
		return robotoLight;
	}

	public Typeface getRobotoRegular() {
		if (robotoRegular == null) {
			robotoRegular = Typeface.createFromAsset(am, "Roboto-Regular.ttf");
		}
		return robotoRegular;
	}

	public Typeface getRobotoCondensedBold() {
		if (robotoCondensedBold == null) {
			robotoCondensedBold = Typeface.createFromAsset(am, "RobotoCondensed-Bold.ttf");
		}
		return robotoCondensedBold;
	}

	public Typeface getRobotoCondensedLight() {
		if (robotoCondensedLight == null) {
			robotoCondensedLight = Typeface.createFromAsset(am, "RobotoCondensed-Light.ttf");
		}
		return robotoCondensedLight;
	}

	public Typeface getRobotoCondensedRegular() {
		if (robotoCondensedRegular == null) {
			robotoCondensedRegular = Typeface.createFromAsset(am, "RobotoCondensed-Regular.ttf");
		}
		return robotoCondensedRegular;
	}

}
