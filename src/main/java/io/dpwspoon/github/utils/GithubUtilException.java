package io.dpwspoon.github.utils;

import java.io.IOException;

public class GithubUtilException extends Exception {

	private static final long serialVersionUID = -6072143969198596378L;

	public GithubUtilException(IOException e) {
		super(e);
	}

}
