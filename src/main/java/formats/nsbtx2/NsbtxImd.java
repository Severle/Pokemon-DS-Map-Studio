
package formats.nsbtx2;

import formats.imd.ImdAttribute;
import formats.imd.ImdNode;
import formats.imd.nodes.Body;
import formats.imd.nodes.TexImage;
import formats.imd.nodes.TexPalette;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "HttpUrlsUsage", "DuplicatedCode", "GrazieInspection"})
public class NsbtxImd extends ImdNode {

    public NsbtxImd(Nsbtx2 nsbtx) {
        super("imd");

        attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("version", "1.6.0"));
            }
        };

        Body body = new Body();

        ImdNode texImageArray = new ImdNode("tex_image_array");
        for (int i = 0; i < nsbtx.getTextures().size(); i++) {
            TexImage texImage = new TexImage(i, nsbtx.getTexture(i), "");
            texImageArray.subnodes.add(texImage);
        }

        ImdNode texPaletteArray = new ImdNode("tex_palette_array");
        for (int i = 0; i < nsbtx.getPalettes().size(); i++) {
            TexPalette texPalette = new TexPalette(i, nsbtx.getPalette(i));
            texPaletteArray.subnodes.add(texPalette);
        }

        texImageArray.attributes.add(new ImdAttribute("size", nsbtx.getTextures().size()));
        texPaletteArray.attributes.add(new ImdAttribute("size", nsbtx.getPalettes().size()));

        body.subnodes.add(texImageArray);
        body.subnodes.add(texPaletteArray);

        subnodes.add(body);
    }

    public void saveToFile(String path) throws
            ParserConfigurationException, TransformerException, IOException {

        saveToXML(path);
    }

    private void saveToXML(String xmlPath) throws ParserConfigurationException,
            TransformerException, IOException {
        Document dom;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // use factory to get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
        // create instance of DOM
        dom = db.newDocument();

        // create the root element
        Element rootEle = dom.createElement(nodeName);
        for (ImdAttribute attrib : attributes) {
            rootEle.setAttribute(attrib.tag, attrib.value);
        }

        for (ImdNode subnode : subnodes) {
            printImdNode(subnode, dom, rootEle);
        }

        //printImdNode(this, dom, rootEle);
        dom.appendChild(rootEle);

        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        //tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        // send DOM to file
        //tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(xmlPath));
        StreamResult streamResult = new StreamResult(new FileOutputStream(xmlPath));
        tr.transform(new DOMSource(dom), streamResult);
        streamResult.getOutputStream().close();

        System.out.println("IMD saved!");

    }

    private void printImdNode(ImdNode node, Document dom, Element parent) {
        Element e;

        e = dom.createElement(node.nodeName);

        for (int i = 0; i < node.attributes.size(); i++) {
            ImdAttribute attrib = node.attributes.get(i);
            e.setAttribute(attrib.tag, attrib.value);
        }

        e.appendChild(dom.createTextNode(node.content));

        for (int i = 0; i < node.subnodes.size(); i++) {
            ImdNode subnode = node.subnodes.get(i);
            printImdNode(subnode, dom, e);
        }
        parent.appendChild(e);

    }

}
