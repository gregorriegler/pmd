/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package net.sourceforge.pmd.build.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.pmd.build.PmdBuildException;
import net.sourceforge.pmd.build.xml.XmlFileTemplater;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
*
* @author Romain PELISSE, belaran@gmail.com
*
*/
public final class XmlUtil {

	private XmlUtil() {};
	
	public static Transformer createTransformer(String xsl) throws PmdBuildException {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			StreamSource src = new StreamSource(xsl);
			return factory.newTransformer(src);
		} catch (TransformerConfigurationException e) {
			throw new PmdBuildException(e);
		}
	}
	
	public static DOMSource createDomSourceFrom(InputStream inputStream) {
		try {
			return new DOMSource(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream));
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static DOMSource createXmlBackbone(XmlFileTemplater templater) {
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			doc = parser.newDocument();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
		Element root = doc.createElement("root");
		doc = templater.doTemplate(doc,root);
		doc.appendChild(root);
		return new DOMSource(doc);
	}
}