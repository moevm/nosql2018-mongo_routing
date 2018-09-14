package ru.zmaps.parser;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class MapInitialaizer {
    @Autowired
    ReaderXmlFile reader;



}
