package io.dpwspoon.github.utils;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestTravicCIUtils {

	public File workingDir;

	@Before
	public void before() {
		workingDir = new File("target/workingdir");
		if (workingDir.exists()) {
			deleteFolder(workingDir);
		}
		workingDir.mkdirs();
	}

	@Test
	@Ignore
	public void testTravisCIUtilsAddTravisFileToRepoWithOutREADME() throws InvalidRemoteException, TransportException,
			IOException, GitAPIException {
		TravisCIUtils.addTravisFileToRepo("https://github.com/dpwspoon/github.utils", "feature-travisci", "dpwspoon",
				"github.utils", workingDir);
		// workingDir.mkdirs();
		// TravisCIUtils.addTravisCIBuildToAllJavaProjects(workingDir);
	}

	@Test
	@Ignore
	public void testTravisCIUtilsAddTravisFileToRepoWithREADME() throws InvalidRemoteException, TransportException,
			IOException, GitAPIException {
		TravisCIUtils.addTravisFileToRepo("https://github.com/kaazing/gateway.distribution", "feature-travisci",
				"kaazing", "gateway.distribution", workingDir);
	}

	@Test
	public void testAddTravisCIToProjs() throws InvalidRemoteException, TransportException, IOException,
			GitAPIException {
		TravisCIUtils.addTravisCIBuildToAllJavaProjects(workingDir);
	}

	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
}
