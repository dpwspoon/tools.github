package io.dpwspoon.github.utils;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public final class RepoUtils {

	public static final Predicate<GHRepository> ALL = new Predicate<GHRepository>() {
		@Override
		public boolean test(GHRepository t) {
			return true;
		}
	};

	public static final Predicate<GHRepository> PRIVATE = new Predicate<GHRepository>() {
		@Override
		public boolean test(GHRepository t) {
			return t.isPrivate();
		}
	};

	public static final Predicate<GHRepository> PUBLIC = new Predicate<GHRepository>() {
		@Override
		public boolean test(GHRepository t) {
			return !t.isPrivate();
		}
	};

	public static final String MY_GITHUB_USER_NAME;
	static {
		try {
			MY_GITHUB_USER_NAME = GitHubBuilder.fromCredentials().build()
					.getMyself().getLogin();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void processRepositories(String user,
			Predicate<GHRepository> predicate,
			Consumer<GHRepository> consumer)
			throws IOException {
		PagedIterable<GHRepository> repositories;
		if (MY_GITHUB_USER_NAME.equalsIgnoreCase(user)) {
			repositories = GitHubBuilder.fromCredentials().build().getMyself()
					.listRepositories();
		} else {
			repositories = GitHubBuilder.fromCredentials().build()
					.getOrganization(user).listRepositories();
		}
		PagedIterator<GHRepository> iter = repositories.iterator();
		while (iter.hasNext()) {
			GHRepository repo = iter.next();
			if (predicate.test(repo)) {
				consumer.accept(repo);
			}
		}
	}

	public static void printListOfRepos(String user,
			Predicate<GHRepository> predicate) throws IOException {

		Consumer<GHRepository> consumer = new Consumer<GHRepository>() {
			@Override
			public void accept(GHRepository t) {
				System.out.println(t.getFullName());
			}
		};

		processRepositories(user, predicate, consumer);
	}
}
