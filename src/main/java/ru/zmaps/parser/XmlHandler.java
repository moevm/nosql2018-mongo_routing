package ru.zmaps.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.zmaps.parser.entity.Node;

import ru.zmaps.parser.entity.Element;
import ru.zmaps.parser.entity.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class XmlHandler extends DefaultHandler {

    private Set<String> skipTag = new HashSet<>();

    private HashMap<Long, Node> nodes = new HashMap<>();

    private Element lastElem = null;

    {
        skipTag.add("created_by");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        parseElem(uri, localName, qName, attributes);
    }

    private void parseElem(String uri, String localName, String qName, Attributes attributes) {
        if ("node".equals(qName)) {
            Long id = Long.valueOf(attributes.getValue("id"));
            Double lon = Double.valueOf(attributes.getValue("lon"));
            Double lat = Double.valueOf(attributes.getValue("lat"));

            Node node = new Node(lat, lon, id);
            lastElem = node;

            nodes.put(id, node);
            return;
        }

        if ("tag".equals(qName)) {
            String key = attributes.getValue("k");

            if (skipTag.contains(key)) {
                return;
            }

            String val = attributes.getValue("v");

            lastElem.addTag(new Tag(key, val));
            return;
        }

        if ("way".equals(qName)) {
            System.out.println("its way");
            System.out.println("its way");
            System.out.println("its way");
            System.out.println("its way");
        }
    }
}
