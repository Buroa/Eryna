package org.apollo.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * A class to generate a simple 1024 bit RSA pair
 * @author Nikki
 */
public class RSAGenerator {

	/**
	 * The main.
	 * @param args The arguments.
	 */
	public static void main(String[] args) {
		try {
			final KeyFactory factory = KeyFactory.getInstance("RSA");
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			final KeyPair keypair = keyGen.genKeyPair();
			final PrivateKey privateKey = keypair.getPrivate();
			final PublicKey publicKey = keypair.getPublic();

			final RSAPrivateKeySpec privSpec = factory.getKeySpec(privateKey, RSAPrivateKeySpec.class);

			writeKey("rsapriv", privSpec.getModulus(), privSpec.getPrivateExponent());

			final RSAPublicKeySpec pubSpec = factory.getKeySpec(publicKey, RSAPublicKeySpec.class);

			writeKey("rsapub", pubSpec.getModulus(), pubSpec.getPublicExponent());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the key.
	 * @param file The file.
	 * @param modulus The modulus.
	 * @param exponent The exponent.
	 */
	public static void writeKey(String file, BigInteger modulus, BigInteger exponent) {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("private static final BigInteger RSA_MODULUS = new BigInteger(\"" + modulus.toString() + "\");");
			writer.newLine();
			writer.newLine();
			writer.write("private static final BigInteger RSA_EXPONENT = new BigInteger(\"" + exponent.toString() + "\");");
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}