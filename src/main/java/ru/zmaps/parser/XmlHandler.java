package ru.zmaps.parser;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Arrays;

public class XmlHandler extends DefaultHandler {
    private String attrToString(Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            System.out.println(attributes.getLocalName(i) + " - " + attributes.getValue(i));
        }
        return "";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("Start elem");
        System.out.println("uri " + uri + "\n" +
                "localName " + localName + "\n" +
                "qName " + qName + "\n" +
                "attributes " + attrToString(attributes));
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        System.out.println("Characters:");
        System.out.println("ch " + new String(Arrays.copyOfRange(ch, start, start + length)) + "\n");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("End elem");
        System.out.println("uri " + uri + "\n" +
                "localName " + localName + "\n" +
                "qName " + qName + "\n");
    }
}
