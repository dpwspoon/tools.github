package io.dpwspoon.github.utils;

import java.io.IOException;

import org.kohsuke.github.GitHubBuilder;

public final class GithubUtils {

	public static final String MY_GITHUB_USER_NAME;

	static {
		try {
			MY_GITHUB_USER_NAME = GitHubBuilder.fromCredentials().build().getMyself().getLogin();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
