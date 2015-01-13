package io.dpwspoon.github.utils;

import static io.dpwspoon.github.utils.GithubUtils.getMyUserName;
import static io.dpwspoon.github.utils.GithubUtils.printListOfRepos;
import static io.dpwspoon.github.utils.GithubUtils.listUnforkedRepos;

import java.io.IOException;

import org.junit.Test;

public class GithubUtilsTest {

	@Test
	public void shouldPrintMyRepos() throws GithubUtilException, IOException {
		printListOfRepos(getMyUserName());
//		listUnforkedRepos(getMyUserName());
	}

	@Test
	public void printOrgReposTest() throws GithubUtilException {
		printListOfRepos("kaazing");
	}
	
}
