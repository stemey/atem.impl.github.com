package org.atemsource.atem.impl.jsoup.selector;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.atemsource.atem.api.attribute.annotation.Cardinality;
import org.atemsource.atem.api.path.AttributePath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:/test/atem/jsoup/entitytype.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupSelectorPathTest {

	@Inject
	private JsoupSelectorPathFactory attributePathFactory;
	
	@Test
	public void testElements() {
		Document document = Jsoup.parse("<html><body><ul><li class='a'></li><li class='a'></li></ul></body></html>");
		AttributePath attributePath = attributePathFactory.create("*::li");
		Elements elements = (Elements) attributePath.getAttribute().getValue(document);
		Assert.assertEquals(2,elements.size());
	}
	@Test
	public void testSingleElement() {
		Document document = Jsoup.parse("<html><body><ul><li class='a'></li><li class='a'></li></ul></body></html>");
		AttributePath attributePath = attributePathFactory.create("1::li.a");
		Element element = (Element) attributePath.getAttribute().getValue(document);
		Assert.assertNotNull(element);
	}
	@Test
	public void testAttributes() {
		Document document = Jsoup.parse("<html><body><ul><li class='a'></li><li class='b'></li></ul></body></html>");
		AttributePath attributePath = attributePathFactory.create("*:class:li");
		List<String> attribtues = (List<String>) attributePath.getAttribute().getValue(document);
		Assert.assertEquals(2,attribtues.size());
		Assert.assertEquals("b",attribtues.get(1));
	}
	@Test
	public void testSingleAttribute() {
		Document document = Jsoup.parse("<html><body><ul><li class='a'></li><li class='b'></li></ul></body></html>");
		AttributePath attributePath = attributePathFactory.create("1:class:li.b");
		String attribute = (String) attributePath.getAttribute().getValue(document);
		Assert.assertEquals("b",attribute);
	}

}
