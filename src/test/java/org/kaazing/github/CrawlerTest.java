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
