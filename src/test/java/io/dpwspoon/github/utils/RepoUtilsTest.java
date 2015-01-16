package io.dpwspoon.github.utils;

import static io.dpwspoon.github.utils.RepoUtils.PUBLIC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

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

}
