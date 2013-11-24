package gs.or.venator.schedapalestra;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class Log {

	private static final int MAXIMUM_LINE_LENGTH = 4000;

	private static boolean enabled = true;
	private static int minLevel = -1;

	public static void setEnabled(boolean enabled) {
		Log.enabled = enabled;
	}

	public static void setMinLevel(int minLevel) {
		Log.minLevel = minLevel;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void v(String msg, Throwable t, Object obj) {
		log(android.util.Log.VERBOSE, getTag(obj), msg, t);
	}

	public static void d(String msg, Throwable t, Object obj) {
		log(android.util.Log.DEBUG, getTag(obj), msg, t);
	}

	public static void i(String msg, Throwable t, Object obj) {
		log(android.util.Log.INFO, getTag(obj), msg, t);
	}

	public static void w(String msg, Throwable t, Object obj) {
		log(android.util.Log.WARN, getTag(obj), msg, t);
	}

	public static void e(String msg, Throwable t, Object obj) {
		log(android.util.Log.ERROR, getTag(obj), msg, t);
	}

	public static void v(String msg, Object obj) {
		v(msg, null, obj);
	}

	public static void d(String msg, Object obj) {
		d(msg, null, obj);
	}

	public static void i(String msg, Object obj) {
		i(msg, null, obj);
	}

	public static void w(String msg, Object obj) {
		w(msg, null, obj);
	}

	public static void e(String msg, Object obj) {
		e(msg, null, obj);
	}

	private static void log(int level, String tag, String msg, Throwable t) {
		if (enabled && level >= minLevel) {
			if (msg.length() > MAXIMUM_LINE_LENGTH) {
				List<String> chunks = split(msg, '\n', MAXIMUM_LINE_LENGTH);
				StringBuffer buf = new StringBuffer(MAXIMUM_LINE_LENGTH);
				for (int i = 0; i < chunks.size(); i++) {
					String chunk = chunks.get(i);

					if (buf.length() + chunk.length() + 1 < MAXIMUM_LINE_LENGTH) {
						buf.append(chunk);
						buf.append("\n");
					} else {
						invokeLogger(level, tag, buf.toString());

						buf = new StringBuffer(MAXIMUM_LINE_LENGTH);
						buf.append(chunk);
						buf.append("\n");
					}
				}
				if (buf.length() > 0) {
					invokeLogger(level, tag, buf.toString());
				}
			} else {
				invokeLogger(level, tag, msg);
			}
			if (t != null) {
				log(level, tag, getStackTraceString(t), null);
			}
		}
	}

	private static void invokeLogger(int level, String tag, String msg) {
		android.util.Log.println(level, tag, msg);
	}

	private static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.append('\n');
		tr.printStackTrace(pw);
		return sw.toString();
	}

	private static String getTag(Object obj) {
		if (obj != null) {
			String tag;
			if (obj instanceof String) {
				tag = (String) obj;
			} else if (obj instanceof Class) {
				Class<?> c = (Class<?>) obj;
				tag = c.getSimpleName();
			} else {
				Class<?> c = obj.getClass();
				tag = c.getSimpleName();
			}
			return tag;
		} else {
			return "(null)";
		}
	}

	public final static List<String> split(String in, char ch, int chunkMaxLen) {
		List<String> list = new ArrayList<String>();
		int pos = in.indexOf(ch);
		int oldpos = 0;
		while (pos - oldpos >= 0) {
			final String chunk = in.substring(oldpos, pos);
			if (chunk.length() > chunkMaxLen) {
				final List<String> splitByLength = splitByLength(chunk, chunkMaxLen);
				list.addAll(splitByLength);
			} else {
				list.add(chunk);
			}
			oldpos = pos + 1;
			pos = oldpos + in.substring(oldpos).indexOf(ch);
		}
		final String lastChunk = in.substring(oldpos);
		if (lastChunk.length() > chunkMaxLen) {
			final List<String> splitByLength = splitByLength(lastChunk, chunkMaxLen);
			list.addAll(splitByLength);
		} else {
			list.add(lastChunk);
		}
		return list;
	}

	private static List<String> splitByLength(String msg, int chunkMaxLen) {
		List<String> list = new ArrayList<String>();
		StringReader r = new StringReader(msg);
		char buf[] = new char[chunkMaxLen];
		int nread;
		try {
			while ((nread = r.read(buf)) != -1) {
				list.add(String.valueOf(buf, 0, nread));
			}
		} catch (IOException ignore) {
		} finally {
			r.close();
		}
		return list;
	}
}
