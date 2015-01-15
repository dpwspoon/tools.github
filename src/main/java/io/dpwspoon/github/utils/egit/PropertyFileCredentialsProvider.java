package io.dpwspoon.github.utils.egit;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Shares the same property file ~/.github which is used by
 * org.kohsuke:github-api
 *
 */
public class PropertyFileCredentialsProvider extends
		UsernamePasswordCredentialsProvider {
	private final static String USERNAME;
	private final static String PASSWORD;

	static {
		try {
			File homeDir = new File(System.getProperty("user.home"));
			File propertyFile = new File(homeDir, ".github");
			Properties props = new Properties();
			FileInputStream in = null;
			try {
				in = new FileInputStream(propertyFile);
				props.load(in);
			} finally {
				IOUtils.closeQuietly(in);
			}
			USERNAME = props.getProperty("login");
			PASSWORD = props.getProperty("password");
		} catch (Exception e) {
			throw new RuntimeException("Could not get credentials", e);
		}
	}

	public PropertyFileCredentialsProvider() {

		super(USERNAME, PASSWORD);
	}

}
