package io.dpwspoon.github.utils;

import static io.dpwspoon.github.utils.RepoUtils.PUBLIC;

import java.io.IOException;

import org.junit.Test;

public class RepoUtilsTest {

	@Test
	public void shouldPrintMyRepos() throws IOException {
		RepoUtils.printListOfRepos("dpwspoon", PUBLIC);
	}

}
