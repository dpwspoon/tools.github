package io.dpwspoon.github.utils;

import io.dpwspoon.github.utils.egit.PropertyFileCredentialsProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.kohsuke.github.GHRepository;

public final class TravisCIUtils {

	public static void addTravisCIBuildToAllJavaProjects(File workingDir) throws IOException, InvalidRemoteException,
			TransportException, GitAPIException {

		List<String> alreadyCompletedButPullRequestNotMerged = new ArrayList<String>();
		alreadyCompletedButPullRequestNotMerged.add("netx");
		alreadyCompletedButPullRequestNotMerged.add("qpid.jms.itest");
		alreadyCompletedButPullRequestNotMerged.add("code.quality");
		alreadyCompletedButPullRequestNotMerged.add("nuklei.amqp_1_0.jms");
		alreadyCompletedButPullRequestNotMerged.add("community");
		alreadyCompletedButPullRequestNotMerged.add("common");
		alreadyCompletedButPullRequestNotMerged.add("neoload.codec.jms");
		alreadyCompletedButPullRequestNotMerged.add("community.license");
		alreadyCompletedButPullRequestNotMerged.add("license-maven-plugin");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.amqp");
		alreadyCompletedButPullRequestNotMerged.add("gateway.bridge");
		alreadyCompletedButPullRequestNotMerged.add("gateway.client.java.bridge");
		alreadyCompletedButPullRequestNotMerged.add("gateway.client.java.common");
		alreadyCompletedButPullRequestNotMerged.add("gateway.client.java");
		alreadyCompletedButPullRequestNotMerged.add("gateway.client.java.transport");
		alreadyCompletedButPullRequestNotMerged.add("gateway.distribution");
		alreadyCompletedButPullRequestNotMerged.add("gateway.management");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.http");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.httpx");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.httpxdraft");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.httpxe");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.pipe");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.rtmp");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.sse");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.ssl");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.tcp");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.udp");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.ws");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.wsdraft");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.wse");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.wsn");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.wsr");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.wsx");
		alreadyCompletedButPullRequestNotMerged.add("gateway.resource.address.wsxdraft");
		alreadyCompletedButPullRequestNotMerged.add("gateway.security");
		alreadyCompletedButPullRequestNotMerged.add("gateway.server");
		alreadyCompletedButPullRequestNotMerged.add("gateway.server.api");
		alreadyCompletedButPullRequestNotMerged.add("gateway.server.demo");
		alreadyCompletedButPullRequestNotMerged.add("gateway.server.spi");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.broadcast");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.echo");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.http.balancer");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.http.directory");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.proxy");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.update.check");
		alreadyCompletedButPullRequestNotMerged.add("gateway.service.update.check.management");
		alreadyCompletedButPullRequestNotMerged.add("gateway.test.ca");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.bio");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.http");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.nio");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.pipe");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.sse");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.ssl");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.ws");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.wseb");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.wsn");
		alreadyCompletedButPullRequestNotMerged.add("gateway.transport.wsr");
		alreadyCompletedButPullRequestNotMerged.add("gateway.truststore");
		alreadyCompletedButPullRequestNotMerged.add("gateway.util");
		alreadyCompletedButPullRequestNotMerged.add("mina.core");
		alreadyCompletedButPullRequestNotMerged.add("mina.netty");
		alreadyCompletedButPullRequestNotMerged.add("test.util");
		alreadyCompletedButPullRequestNotMerged.add("tools.wscat");
		alreadyCompletedButPullRequestNotMerged.add("version-properties-maven-plugin");
		alreadyCompletedButPullRequestNotMerged.add("gateway.client.java.demo");
		alreadyCompletedButPullRequestNotMerged.add("amqp.client.java");
		alreadyCompletedButPullRequestNotMerged.add("amqp.client.java.demo");
		alreadyCompletedButPullRequestNotMerged.add("truststore-maven-plugin");
		alreadyCompletedButPullRequestNotMerged.add("sigar.dist");
		alreadyCompletedButPullRequestNotMerged.add("unpack-bower-dependency-maven-plugin");
		alreadyCompletedButPullRequestNotMerged.add("snmp4j");
		alreadyCompletedButPullRequestNotMerged.add("snmp4j.agent");



		List<GHRepository> repos = DataReaper.getRepositoriesWithFile("/", "pom.xml");
		for (GHRepository repo : repos) {
			String mainRepoName = repo.getName();
			if (!mainRepoName.contains("ios")) {
				if (!alreadyCompletedButPullRequestNotMerged.contains(mainRepoName) && !RepoUtils.hasFile(repo, "/", ".travis.yml")) {
					GHRepository forkedRepo = repo.fork();
					String branchName = "feature-travisci";
					String repoName = forkedRepo.getName();
					addTravisFileToRepo(forkedRepo.getUrl(), branchName, repo.getOwner().getLogin(), repoName,
							workingDir);
					System.out.println("Added Repo: " + mainRepoName);
					repo.createPullRequest("Added .travis.yml and badge to README.md", GithubUtils.MY_GITHUB_USER_NAME
							+ ":" + branchName, repo.getMasterBranch(), "");
				}
			} else {
				System.out.println("ignoring ios repo: " + mainRepoName);
			}
		}
	}

	private static final CredentialsProvider credentialsProvider = new PropertyFileCredentialsProvider();

	public static void addTravisFileToRepo(final String uri, final String branchName, final String orgName,
			final String repoName, File workingDir) throws IOException, InvalidRemoteException, TransportException,
			GitAPIException {
		File directory = new File(workingDir, repoName);
		Git localRepo = null;
		for (int trys = 0; true; trys++) {
			try {
				localRepo = Git.cloneRepository().setCredentialsProvider(credentialsProvider).setDirectory(directory)
						.setURI(uri).call();
				break;
			} catch (TransportException e) {
				// sporadic failure
				FileUtils.deleteFolder(directory);
				if (trys > 3) {
					throw e;
				}
			}
		}
		localRepo.checkout().setCreateBranch(true).setName(branchName).call();
		addTravisFileTo(localRepo, directory, orgName, repoName);
		localRepo.commit().setAll(true).setMessage("Added .travis.yml and badge to README.md").call();
		localRepo.push().setCredentialsProvider(credentialsProvider).call();
	}

	public static void addTravisFileTo(Git localRepo, File dir, String orgName, String repoName) throws IOException,
			NoFilepatternException, GitAPIException {
		String travisFileName = ".travis.yml";
		try (FileWriter travisWriter = new FileWriter(new File(dir, travisFileName))) {
			travisWriter.write("language: Java\n" + "jdk:\n" + "  - oraclejdk7\n" + "  - openjdk7\n"
					+ "script: mvn verify\n");
		}
		localRepo.add().addFilepattern(travisFileName).call();

		String readmeFileName = "README.md";
		File readme = new File(dir, readmeFileName);
		if (readme.exists()) {

			String TEXT_TO_ADD = "[![Build Status][build-status-image]][build-status]\n\n"
					+ String.format("[build-status-image]: https://travis-ci.org/%s/%s.svg?branch=develop\n", orgName,
							repoName)
					+ String.format("[build-status]: https://travis-ci.org/%s/%s\n", orgName, repoName);

			List<String> fileLines = new ArrayList<String>();
			Scanner scanner = null;
			try {
				scanner = new Scanner(readme);
				boolean linesAdded = false;
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					fileLines.add(line);
					if (fileLines.size() == 2) {
						linesAdded = true;
						fileLines.add(TEXT_TO_ADD);
					}
				}
				if (!linesAdded) {
					fileLines.add(TEXT_TO_ADD);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (scanner != null) {
					scanner.close();
				}
			}

			PrintWriter pw = null;
			try {
				pw = new PrintWriter(readme);
				for (String line : fileLines) {
					pw.println(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (pw != null) {
					pw.close();
				}
			}

		} else {
			try (FileWriter readmeWriter = new FileWriter(readme)) {
				readmeWriter.write("#" + repoName + "\n" + "[![Build Status][build-status-image]][build-status]\n\n");
				readmeWriter.write(String.format(
						"[build-status-image]: https://travis-ci.org/%s/%s.svg?branch=develop\n", orgName, repoName));
				readmeWriter.write(String.format("[build-status]: https://travis-ci.org/%s/%s", orgName, repoName));
				readmeWriter.write("\n");
			}
			localRepo.add().addFilepattern(readmeFileName).call();
		}
	}
}
