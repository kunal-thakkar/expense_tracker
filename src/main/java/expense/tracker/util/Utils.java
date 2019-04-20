package expense.tracker.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class Utils {

	public static String newLineSeparator = System.getProperty("line.separator");
	public static List<String> months = Arrays.asList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");

	public static long getDateTime(String year, String month, String day) {
		return LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), 0, 0)
				.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
	}
	

}
