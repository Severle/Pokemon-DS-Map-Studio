
package formats.dae;

import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Trifindo
 */
@Log4j2
@SuppressWarnings("unused")
public class DaeReader {

    public static void loadDae(String path) throws ParserConfigurationException, SAXException, IOException {

        File stocks = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(stocks);
        doc.getDocumentElement().normalize();

        log.debug("root of xml file {}", doc.getDocumentElement().getNodeName());
        NodeList nodes = doc.getElementsByTagName("library_images");

        log.debug("==========================");
        log.debug("length: {}", nodes.getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            for (int j = 0; j < nodes.item(i).getChildNodes().getLength(); j++) {

                Node n = nodes.item(i).getChildNodes().item(j);
                //n.getChildNodes()
                log.debug("{} {}: {} {} {} {} {}", i, j, n.getNodeName(), n.getLocalName(), n.getNamespaceURI(), n.getTextContent(), n.getNodeValue());
            }

            //log.debug(nodes.item(i).getNodeName());
        }
        log.debug("==========================");

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
            }
        }

        log.debug("Loaded");

    }

}
