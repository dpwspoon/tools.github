package io.dpwspoon.github.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.kohsuke.github.GHRepository;

public class DataReaperTest {

	@Test
	public void printReposWithPomOnMainBranch() throws IOException{
		List<GHRepository> repos = DataReaper.getRepositoriesWithPomsOnMainBranch();
		for(GHRepository repo: repos){
			System.out.println(repo.getName());
		}
	}
	
	@Test
	public void printReposWithPomOnMainBranchViaGeneric() throws IOException{
		List<GHRepository> repos = DataReaper.getRepositoriesWithFile("/", "pom.xml");
		for(GHRepository repo: repos){
			System.out.println(repo.getName());
		}
	}
}
