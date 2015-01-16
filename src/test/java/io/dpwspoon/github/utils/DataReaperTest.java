package io.dpwspoon.github.utils;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GHRepository;

public class DataReaperTest {

	@Test
	@Ignore
	public void printReposWithPomOnMainBranch() throws IOException{
		List<GHRepository> repos = DataReaper.getRepositoriesWithFile("/", "pom.xml");
		for(GHRepository repo: repos){
			System.out.println(repo.getName());
		}
	}
	
	@Test
	public void printReposWithoutREADMEOnMainBranch() throws IOException{
		List<GHRepository> repos = DataReaper.getRepositoriesWithOutFile("/", "README.md");
		for(GHRepository repo: repos){
			System.out.println(repo.getName());
		}
	}
	
}
