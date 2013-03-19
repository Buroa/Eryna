package org.apollo.util;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class which contains text-related utility methods.
 * @author Graham
 */
public final class TextUtil {

	/**
	 * An array of characters ordered by frequency - the elements with lower
	 * indices (generally) appear more often in
	 * chat messages.
	 */
	public static final char[] FREQUENCY_ORDERED_CHARS = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l',
			'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+',
			'=', '\243', '$', '%', '"', '[', ']' };

	/**
	 * Capitalizes the string correctly.
	 * @param str The input string.
	 * @return The string with correct capitalization.
	 */
	public static String capitalize(String str) {
		final char[] chars = str.toCharArray();
		boolean sentenceStart = true;
		for (int i = 0; i < chars.length; i++) {
			final char c = chars[i];
			if (sentenceStart) {
				if (c >= 'a' && c <= 'z') {
					chars[i] -= 0x20;
					sentenceStart = false;
				}
				else if (c >= 'A' && c <= 'Z')
					sentenceStart = false;
			}
			else if (c >= 'A' && c <= 'Z')
				chars[i] += 0x20;
			if (c == '.' || c == '!' || c == '?')
				sentenceStart = true;
		}
		return new String(chars, 0, chars.length);
	}

	/**
	 * Does something..
	 * @param integer The integer.
	 * @return The commify'd string.
	 */
	public static String commify(int integer) {
		return String.valueOf(integer).replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
	}

	/**
	 * Compresses the input text ({@code in}) and places the result in the
	 * {@code out} array.
	 * @param in The input text.
	 * @param out The output array.
	 * @return The number of bytes written to the output array.
	 */
	public static int compress(String in, byte[] out) {
		if (in.length() > 80)
			in = in.substring(0, 80);
		in = in.toLowerCase();
		int carry = -1;
		int outPos = 0;
		for (int inPos = 0; inPos < in.length(); inPos++) {
			final char c = in.charAt(inPos);
			int tblPos = 0;
			for (int i = 0; i < FREQUENCY_ORDERED_CHARS.length; i++)
				if (c == FREQUENCY_ORDERED_CHARS[i]) {
					tblPos = i;
					break;
				}
			if (tblPos > 12)
				tblPos += 195;
			if (carry == -1) {
				if (tblPos < 13)
					carry = tblPos;
				else
					out[outPos++] = (byte) tblPos;
			}
			else if (tblPos < 13) {
				out[outPos++] = (byte) ((carry << 4) + tblPos);
				carry = -1;
			}
			else {
				out[outPos++] = (byte) ((carry << 4) + (tblPos >> 4));
				carry = tblPos & 0xF;
			}
		}
		if (carry != -1)
			out[outPos++] = (byte) (carry << 4);
		return outPos;
	}

	/**
	 * Filters invalid characters from the specified string.
	 * @param str The input string.
	 * @return The filtered string.
	 */
	public static String filterInvalidCharacters(String str) {
		final StringBuilder bldr = new StringBuilder();
		for (final char c : str.toLowerCase().toCharArray())
			for (final char validChar : FREQUENCY_ORDERED_CHARS)
				if (c == validChar) {
					bldr.append(c);
					break;
				}
		return bldr.toString();
	}

	/**
	 * Formats a items amount.
	 * @param amount The amount.
	 * @return The formatted value.
	 */
	public static String formatValue(int amount) {
		if (amount < 1_000)
			return String.valueOf(amount);
		else if (amount < 10_000_000)
			return amount / 1_000 + "K";
		else
			return amount / 1_000_000 + "M";
	}

	/**
	 * Request url parameters.
	 * @param url The url query.
	 * @return A map with the correct key value pairs.
	 * @throws Exception Exception creating the url.
	 */
	public static Map<String, List<String>> getUrlParameters(String url) throws Exception {
		final Map<String, List<String>> params = new HashMap<String, List<String>>();
		final String[] urlParts = url.split("\\?");
		if (urlParts.length > 1) {
			final String query = urlParts[1];
			for (final String param : query.split("&")) {
				final String pair[] = param.split("=");
				final String key = URLDecoder.decode(pair[0], "UTF-8");
				String value = "";
				if (pair.length > 1)
					value = URLDecoder.decode(pair[1], "UTF-8");
				List<String> values = params.get(key);
				if (values == null) {
					values = new ArrayList<String>();
					params.put(key, values);
				}
				values.add(value);
			}
		}
		return params;
	}

	/**
	 * Inserts commas for a integer.
	 * @param str The number (in string format)
	 * @return The integer formatted with commas.
	 */
	public static String insertCommas(String str) {
		if (str.length() < 4)
			return str;
		return insertCommas(str.substring(0, str.length() - 3)) + "," + str.substring(str.length() - 3, str.length());
	}

	/**
	 * Gets a random number.
	 * @param range The range.
	 * @return The number.
	 */
	public static int random(int range) {
		final int number = (int) (Math.random() * (range + 1));
		return number < 0 ? 0 : number;
	}

	/**
	 * Uncompresses the compressed data ({@code in}) with the length ({@code len}
	 * ) and returns the uncompressed {@link String}.
	 * @param in The compressed input data.
	 * @param len The length.
	 * @return The uncompressed {@link String}.
	 */
	public static String uncompress(byte[] in, int len) {
		final byte[] out = new byte[4096];
		int outPos = 0;
		int carry = -1;
		for (int i = 0; i < len * 2; i++) {
			final int tblPos = in[i / 2] >> 4 - 4 * (i % 2) & 0xF;
			if (carry == -1) {
				if (tblPos < 13)
					out[outPos++] = (byte) FREQUENCY_ORDERED_CHARS[tblPos];
				else
					carry = tblPos;
			}
			else {
				out[outPos++] = (byte) FREQUENCY_ORDERED_CHARS[(carry << 4) + tblPos - 195];
				carry = -1;
			}
		}
		return new String(out, 0, outPos);
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private TextUtil() {
	}
}
