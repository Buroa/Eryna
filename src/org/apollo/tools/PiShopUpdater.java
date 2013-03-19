package org.apollo.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A tool for updating the shops.
 * @author Steve
 */
public final class PiShopUpdater {

	/**
	 * The shops being added.
	 */
	private static final ArrayList<String> shops = new ArrayList<String>();

	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 * @throws Exception if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage:");
			System.err.println("  java -cp ... org.apollo.tools.PiShopUpdater [input]");
			return;
		}
		final String path = args[0];
		final BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(
				new FileInputStream(path))));
		try {
			String content = "";
			while ((content = br.readLine()) != null)
				if (content.startsWith("shop ="))
					try {
						parseLine(content);
					} catch (final Exception e) {
						e.printStackTrace();
					}
			updateXml();
		} finally {
			br.close();
		}
	}

	/**
	 * Parses a line.
	 * @param content The line.
	 * @throws IOException
	 */
	private static void parseLine(String content) throws IOException {
		final String[] arguments = content.split("\t");
		final int id = Integer.parseInt(arguments[0].replace("shop = ", ""));
		final String name = arguments[1];
		final String script = name.toLowerCase().replace(" ", "").replace("_", "") + "." + id;
		final BufferedWriter bw = new BufferedWriter(new FileWriter("data/plugins/shops/" + script + ".rb"));
		bw.write("# The " + name);
		bw.newLine();
		bw.newLine();
		bw.write("# Define our values");
		bw.newLine();
		bw.write("id = " + id);
		bw.newLine();
		bw.write("name = \"" + name.replace("_", " ") + "\"");
		bw.newLine();
		bw.write("items = {");
		bw.newLine();
		bw.write("  # item id => item amount,");
		for (int i = 4; i < arguments.length; i += 2) {
			final int item = Integer.parseInt(arguments[i]);
			final int amount = Integer.parseInt(arguments[i + 1]);
			bw.newLine();
			bw.write("  " + item + " => " + amount + ",");
		}
		bw.newLine();
		bw.write("}");
		bw.newLine();
		if (name.toLowerCase().contains("general"))
			bw.write("type = STORE_MIXED");
		else
			bw.write("type = STORE_NORMAL");
		bw.newLine();
		bw.newLine();
		bw.write("# Ship out the shop to the world.");
		bw.newLine();
		bw.write("shop = appendShop Shops.new(id, name, items, type)");
		bw.close();
		shops.add(script);
	}

	/**
	 * Updates the shops xml.
	 * @throws IOException
	 */
	private static void updateXml() throws IOException {
		final BufferedWriter bw = new BufferedWriter(new FileWriter("data/plugins/shops/plugin.xml"));
		bw.write("<?xml version=\"1.0\"?>");
		bw.newLine();
		bw.write("<plugin>");
		bw.newLine();
		bw.write("  <id>shops</id>");
		bw.newLine();
		bw.write("  <version>1</version>");
		bw.newLine();
		bw.write("  <name>World shops</name>");
		bw.newLine();
		bw.write("  <description>Add world shops.</description>");
		bw.newLine();
		bw.write("  <authors>");
		bw.newLine();
		bw.write("    <author>Steve</author>");
		bw.newLine();
		bw.write("  </authors>");
		bw.newLine();
		bw.write("  <scripts>");
		bw.newLine();
		bw.write("    <script>shops.rb</script>");
		bw.newLine();
		bw.write("    <script>stores.rb</script>");
		for (final String shop : shops) {
			bw.newLine();
			bw.write("    <script>" + shop + ".rb</script>");
		}
		bw.newLine();
		bw.write("  </scripts>");
		bw.newLine();
		bw.write("  <dependencies />");
		bw.newLine();
		bw.write("</plugin>");
		bw.close();
	}
}
