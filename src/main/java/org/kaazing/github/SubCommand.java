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

public abstract class SubCommand {

    private String[] args = new String[]{};

    /**
     * Name of the command as ran from the command line
     * @return
     */
    public abstract String getName();

    /**
     * Description of what the command does
     * @return
     */
    public abstract String getDescription();

    public String[] getArgs() {
        return args;
    }

    /**
     * Args passed into the command
     * @param args
     */
    public void setArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }
        this.args = args;
    }

    /**
     * List args that are required
     * @return
     */
    public abstract String listArgs();
}
