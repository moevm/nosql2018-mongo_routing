package ru.zmaps.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.xml.parsers.SAXParserFactory;

@Component
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
public class ReaderXmlFile {

    SAXParserFactory factory = SAXParserFactory.newInstance();


}
