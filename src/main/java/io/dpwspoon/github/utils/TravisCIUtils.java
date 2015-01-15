package io.dpwspoon.github.utils;

import io.dpwspoon.github.utils.egit.PropertyFileCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.kohsuke.github.GHRepository;

public final class TravisCIUtils {

	public static void addTravisCIBuildToAllJavaProjects(File workingDir)
			throws IOException, InvalidRemoteException, TransportException,
			GitAPIException {
		List<GHRepository> repos = DataReaper.getRepositoriesWithFile("/",
				"pom.xml");
		for (GHRepository repo : repos) {
			if (!RepoUtils.hasFile(repo, "/", ".travis.yml")) {
				GHRepository forkedRepo = repo.fork();
				String url = forkedRepo.getUrl();
				File directory = new File("workingDir", forkedRepo.getName());
				CredentialsProvider credentialsProvider = new PropertyFileCredentialsProvider();
				Repository localRepo = Git.cloneRepository().setCredentialsProvider(credentialsProvider).setDirectory(directory)
						.setURI(url).call().getRepository();

			}
		}
	}
}
