/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.github;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Shares the same property file ~/.github which is used by org.kohsuke:github-api
 *
 */
public class PropertyFileCredentialsProvider extends UsernamePasswordCredentialsProvider {
    private final static String USERNAME;
    private final static String PASSWORD;

    static {
        try {
            File homeDir = new File(System.getProperty("user.home"));
            File propertyFile = new File(homeDir, ".github");
            Properties props = new Properties();
            FileInputStream in = null;
            try {
                in = new FileInputStream(propertyFile);
                props.load(in);
            } finally {
                IOUtils.closeQuietly(in);
            }
            USERNAME = props.getProperty("login");
            PASSWORD = props.getProperty("password");
        } catch (Exception e) {
            throw new RuntimeException("Could not get credentials", e);
        }
    }

    public PropertyFileCredentialsProvider() {
        super(USERNAME, PASSWORD);
    }

}
