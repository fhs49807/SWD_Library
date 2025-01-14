package at.ac.fhsalzburg.swd.spring.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	/**
	 * @param date string representation of the date in format yyyy-MM-dd
	 */
	public static Date getDateFromString(String date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param date date
	 */
	public static LocalDate getLocalDateFromDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date now() {
		return new Date();
	}
}
