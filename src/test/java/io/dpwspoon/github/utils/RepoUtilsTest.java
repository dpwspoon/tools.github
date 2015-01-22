package io.dpwspoon.github.utils;

import static io.dpwspoon.github.utils.GithubUtils.MY_GITHUB_USER_NAME;
import static io.dpwspoon.github.utils.RepoUtils.PUBLIC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public class RepoUtilsTest {

	List<String> listOfRepos = new ArrayList<String>();

	public RepoUtilsTest() {
		listOfRepos.add("netx");
		listOfRepos.add("qpid.jms.itest");
		listOfRepos.add("code.quality");
		listOfRepos.add("nuklei.amqp_1_0.jms");
		listOfRepos.add("community");
		listOfRepos.add("common");
		listOfRepos.add("neoload.codec.jms");
		listOfRepos.add("community.license");
		listOfRepos.add("license-maven-plugin");
		listOfRepos.add("gateway.service.amqp");
		listOfRepos.add("gateway.bridge");
		listOfRepos.add("gateway.client.java.bridge");
		listOfRepos.add("gateway.client.java.common");
		listOfRepos.add("gateway.client.java");
		listOfRepos.add("gateway.client.java.transport");
		listOfRepos.add("gateway.distribution");
		listOfRepos.add("gateway.management");
		listOfRepos.add("gateway.resource.address");
		listOfRepos.add("gateway.resource.address.http");
		listOfRepos.add("gateway.resource.address.httpx");
		listOfRepos.add("gateway.resource.address.httpxdraft");
		listOfRepos.add("gateway.resource.address.httpxe");
		listOfRepos.add("gateway.resource.address.pipe");
		listOfRepos.add("gateway.resource.address.rtmp");
		listOfRepos.add("gateway.resource.address.sse");
		listOfRepos.add("gateway.resource.address.ssl");
		listOfRepos.add("gateway.resource.address.tcp");
		listOfRepos.add("gateway.resource.address.udp");
		listOfRepos.add("gateway.resource.address.ws");
		listOfRepos.add("gateway.resource.address.wsdraft");
		listOfRepos.add("gateway.resource.address.wse");
		listOfRepos.add("gateway.resource.address.wsn");
		listOfRepos.add("gateway.resource.address.wsr");
		listOfRepos.add("gateway.resource.address.wsx");
		listOfRepos.add("gateway.resource.address.wsxdraft");
		listOfRepos.add("gateway.security");
		listOfRepos.add("gateway.server");
		listOfRepos.add("gateway.server.api");
		listOfRepos.add("gateway.server.demo");
		listOfRepos.add("gateway.server.spi");
		listOfRepos.add("gateway.service");
		listOfRepos.add("gateway.service.broadcast");
		listOfRepos.add("gateway.service.echo");
		listOfRepos.add("gateway.service.http.balancer");
		listOfRepos.add("gateway.service.http.directory");
		listOfRepos.add("gateway.service.proxy");
		listOfRepos.add("gateway.service.update.check");
		listOfRepos.add("gateway.service.update.check.management");
		listOfRepos.add("gateway.test.ca");
		listOfRepos.add("gateway.transport");
		listOfRepos.add("gateway.transport.bio");
		listOfRepos.add("gateway.transport.http");
		listOfRepos.add("gateway.transport.nio");
		listOfRepos.add("gateway.transport.pipe");
		listOfRepos.add("gateway.transport.sse");
		listOfRepos.add("gateway.transport.ssl");
		listOfRepos.add("gateway.transport.ws");
		listOfRepos.add("gateway.transport.wseb");
		listOfRepos.add("gateway.transport.wsn");
		listOfRepos.add("gateway.transport.wsr");
		listOfRepos.add("gateway.truststore");
		listOfRepos.add("gateway.util");
		listOfRepos.add("mina.core");
		listOfRepos.add("mina.netty");
		listOfRepos.add("test.util");
		listOfRepos.add("tools.wscat");
		listOfRepos.add("version-properties-maven-plugin");
		listOfRepos.add("gateway.client.java.demo");
		listOfRepos.add("amqp.client.java");
		listOfRepos.add("amqp.client.java.demo");
		listOfRepos.add("truststore-maven-plugin");
		listOfRepos.add("sigar.dist");
		listOfRepos.add("unpack-bower-dependency-maven-plugin");
		listOfRepos.add("snmp4j");
		listOfRepos.add("snmp4j.agent");
	}

	@Test
	@Ignore
	public void shouldPrintMyRepos() throws IOException {
		RepoUtils.printListOfRepos("dpwspoon", PUBLIC);
	}

	@Test
	@Ignore
	public void printListOfReposNotInListAndIsPublic() throws IOException {
		RepoUtils.printListOfRepos("kaazing",
				r -> !listOfRepos.contains(r.getName().replace("kaazing/", "")) && !r.isPrivate());
	}

	@Test
	@Ignore
	public void printListOfReposInListAndPrivate() throws IOException {
		RepoUtils.printListOfRepos("kaazing",
				r -> listOfRepos.contains(r.getName().replace("kaazing/", "")) && r.isPrivate());
	}

	@Test
	public void findIssuesBasedOnSearch() throws IOException {
		Consumer<GHRepository> consumer = new Consumer<GHRepository>() {

			@Override
			public void accept(GHRepository r) {
				PagedIterable<GHPullRequest> open = r.listPullRequests(GHIssueState.OPEN);
				PagedIterable<GHPullRequest> closed = r.listPullRequests(GHIssueState.CLOSED);
				Integer find = listRepos(open.iterator());
				if (find == null) {
					find = listRepos(closed.iterator());
				}
				if (find != null) {
					System.out.println("kaazing/" + r.getName() + "#" + find);
				}

			}

			private Integer listRepos(PagedIterator<GHPullRequest> pagedIterator) {
				while (pagedIterator.hasNext()) {
					GHPullRequest pullRequest = pagedIterator.next();
					String title = pullRequest.getTitle();
					if (title.contains("travis.yml")) {
						return pullRequest.getNumber();
					}
				}
				return null;
			}
		};
		RepoUtils.processRepositories("kaazing", PUBLIC, consumer);
	}

	@Test
	public void printWhatIWant() {
		List<String> list = new ArrayList<String>();
		list.add("kaazing/netx#13");
		list.add("kaazing/kaazing-client-javascript-util#7");
		list.add("kaazing/qpid.jms.itest#1");
		list.add("kaazing/code.quality#2");
		list.add("kaazing/community#3");
		list.add("kaazing/common#13");
		list.add("kaazing/community.license#3");
		list.add("kaazing/license-maven-plugin#6");
		list.add("kaazing/kaazing-amqp-0-9-1-client-javascript#13");
		list.add("kaazing/gateway.service.amqp#2");
		list.add("kaazing/gateway.bridge#3");
		list.add("kaazing/gateway.client.ios#6");
		list.add("kaazing/gateway.client.ios.common#3");
		list.add("kaazing/gateway.client.java.bridge#1");
		list.add("kaazing/gateway.client.java.common#2");
		list.add("kaazing/gateway.client.java#6");
		list.add("kaazing/kaazing-client-javascript#11");
		list.add("kaazing/kaazing-client-javascript-bridge#6");
		list.add("kaazing/gateway.client.java.transport#2");
		list.add("kaazing/kaazing-command-center#7");
		list.add("kaazing/gateway.distribution#26");
		list.add("kaazing/gateway.management#4");
		list.add("kaazing/gateway.resource.address#3");
		list.add("kaazing/gateway.resource.address.http#2");
		list.add("kaazing/gateway.resource.address.httpx#2");
		list.add("kaazing/gateway.resource.address.httpxdraft#2");
		list.add("kaazing/gateway.resource.address.httpxe#1");
		list.add("kaazing/gateway.resource.address.pipe#1");
		list.add("kaazing/gateway.resource.address.rtmp#1");
		list.add("kaazing/gateway.resource.address.sse#1");
		list.add("kaazing/gateway.resource.address.ssl#1");
		list.add("kaazing/gateway.resource.address.tcp#1");
		list.add("kaazing/gateway.resource.address.udp#1");
		list.add("kaazing/gateway.resource.address.ws#1");
		list.add("kaazing/gateway.resource.address.wsdraft#1");
		list.add("kaazing/gateway.resource.address.wse#1");
		list.add("kaazing/gateway.resource.address.wsn#1");
		list.add("kaazing/gateway.resource.address.wsr#1");
		list.add("kaazing/gateway.resource.address.wsx#1");
		list.add("kaazing/gateway.resource.address.wsxdraft#1");
		list.add("kaazing/gateway.security#1");
		list.add("kaazing/gateway.server#3");
		list.add("kaazing/gateway.server.api#3");
		list.add("kaazing/gateway.server.demo#1");
		list.add("kaazing/gateway.server.spi#2");
		list.add("kaazing/gateway.service#1");
		list.add("kaazing/gateway.service.broadcast#1");
		list.add("kaazing/gateway.service.echo#1");
		list.add("kaazing/gateway.service.http.balancer#1");
		list.add("kaazing/gateway.service.http.directory#4");
		list.add("kaazing/gateway.service.proxy#1");
		list.add("kaazing/gateway.service.update.check#3");
		list.add("kaazing/gateway.service.update.check.management#2");
		list.add("kaazing/gateway.test.ca#4");
		list.add("kaazing/gateway.transport#1");
		list.add("kaazing/gateway.transport.bio#2");
		list.add("kaazing/gateway.transport.http#5");
		list.add("kaazing/gateway.transport.nio#3");
		list.add("kaazing/gateway.transport.pipe#1");
		list.add("kaazing/gateway.transport.sse#1");
		list.add("kaazing/gateway.transport.ssl#5");
		list.add("kaazing/gateway.transport.ws#1");
		list.add("kaazing/gateway.transport.wseb#4");
		list.add("kaazing/gateway.transport.wsn#3");
		list.add("kaazing/gateway.transport.wsr#1");
		list.add("kaazing/gateway.truststore#1");
		list.add("kaazing/gateway.util#1");
		list.add("kaazing/mina.core#3");
		list.add("kaazing/mina.netty#1");
		list.add("kaazing/test.util#1");
		list.add("kaazing/tools.wscat#1");
		list.add("kaazing/version-properties-maven-plugin#3");
		list.add("kaazing/grunt-stripbanner#2");
		list.add("kaazing/gateway.client.java.demo#5");
		list.add("kaazing/gateway.client.ios.demo#5");
		list.add("kaazing/amqp.client.java#5");
		list.add("kaazing/amqp.client.java.demo#4");
		list.add("kaazing/truststore-maven-plugin#3");
		list.add("kaazing/kaazing-client-javascript-demo#3");
		list.add("kaazing/sigar.dist#6");
		list.add("kaazing/unpack-bower-dependency-maven-plugin#2");
		list.add("kaazing/snmp4j#4");
		list.add("kaazing/snmp4j.agent#2");
		for (String string : list) {
			Pattern pattern = Pattern.compile("kaazing/(?<name>.*)#\\d+");
			Matcher matcher = pattern.matcher(string);
			matcher.matches();
			System.out.print(string + " ");
			string = matcher.group("name");
			System.out
					.println(String
							.format("[![Build Status](https://travis-ci.org/kaazing/%s.svg?branch=develop)]"
									+ "(https://travis-ci.org/kaazing/%s)", string, string));
		}
	}

}
