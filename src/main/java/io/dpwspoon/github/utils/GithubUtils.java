package io.dpwspoon.github.utils;

import java.io.IOException;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public final class GithubUtils {

	private static final String myUserName;

	static {
		try {
			myUserName = getMyUserName();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getMyUserName() throws IOException {
		return GitHubBuilder.fromCredentials().build().getMyself().getName();
	}

	public static PagedIterable<GHRepository> getRepoList(String user)
			throws GithubUtilException {
		try {
			PagedIterable<GHRepository> repositories;
			if (myUserName.equalsIgnoreCase(user)) {
				repositories = GitHubBuilder.fromCredentials().build()
						.getMyself().listRepositories();
			} else {
				repositories = GitHubBuilder.fromCredentials().build()
						.getOrganization(user).listRepositories();
			}
			return repositories;
		} catch (IOException e) {
			throw new GithubUtilException(e);
		}
	}

	public static void printListOfRepos(String user) throws GithubUtilException {
		System.out.println("hmm");
		PagedIterator<GHRepository> iter = getRepoList(user).iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().getName());
		}
	}

	public static void listUnforkedRepos(String org) throws GithubUtilException {
		// TODO
		// PagedIterable<GHRepository> orgRepos = getRepoList(org);
		PagedIterable<GHRepository> myRepos = getRepoList(myUserName);

		PagedIterator<GHRepository> iter = myRepos.iterator();
		while (iter.hasNext()) {
			GHRepository orgRepo = iter.next();
			if (orgRepo.isFork()) {
				System.out.println(orgRepo.getFullName());
				// orgRepo.
			}
		}
	}
}
