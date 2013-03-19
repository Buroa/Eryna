package org.apollo.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A tool for updating the npc spawns.
 * @author Steve
 */
public final class PiSpawnUpdater {

	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 * @throws Exception if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage:");
			System.err.println("  java -cp ... org.apollo.tools.PiSpawnUpdater [input]");
			return;
		}
		final String path = args[0];
		final BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(
				new FileInputStream(path))));
		final BufferedWriter bw = new BufferedWriter(new FileWriter("data/npc-spawns.xml"));
		try {
			bw.write("<spawn>");
			String content = "";
			while ((content = br.readLine()) != null)
				if (content.startsWith("spawn ="))
					try {
						parseLine(content, bw);
					} catch (final Exception e) {
						e.printStackTrace();
					}
			bw.newLine();
			bw.write("</spawn>");
		} finally {
			br.close();
			bw.close();
		}
	}

	/**
	 * Parses a line.
	 * @param content The line.
	 * @param bw The output stream.
	 * @throws IOException
	 */
	private static void parseLine(String content, BufferedWriter bw) throws IOException {
		final String[] arguments = content.split("\t");
		final int id = Integer.parseInt(arguments[0].replace("spawn = ", ""));
		final int x = Integer.parseInt(arguments[1]);
		final int y = Integer.parseInt(arguments[2]);
		final int height = Integer.parseInt(arguments[3]);
		final int face = Integer.parseInt(arguments[4]);
		bw.newLine();
		if (face > 1)
			bw.write("\t<npc id=\"" + id + "\" face=\"" + face + "\">");
		else
			bw.write("\t<npc id=\"" + id + "\">");
		bw.newLine();
		bw.write("\t\t<position>");
		bw.newLine();
		bw.write("\t\t\t<x>" + x + "</x>");
		bw.newLine();
		bw.write("\t\t\t<y>" + y + "</y>");
		bw.newLine();
		if (height > 0) {
			bw.write("\t\t\t<height>" + height + "</height>");
			bw.newLine();
		}
		bw.write("\t\t</position>");
		bw.newLine();
		bw.write("\t</npc>");
	}
}
