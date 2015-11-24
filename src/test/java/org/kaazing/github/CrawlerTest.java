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

import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;
import org.kaazing.github.Crawler;

public class CrawlerTest {

    @Test
    public void testArgsPattern(){
        Matcher matcher = Crawler.ARGS_PATTERN.matcher("has-file(/base/path/,file-name)");
        Assert.assertTrue(matcher.matches());
        Assert.assertTrue("has-file".equals(matcher.group(1)));
        Assert.assertTrue("/base/path/,file-name".equals(matcher.group(2)));
    }
    

}
