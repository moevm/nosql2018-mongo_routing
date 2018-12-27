package ru.zmaps.parser;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.zmaps.db.NodeDAO;
import ru.zmaps.db.RelationDAO;
import ru.zmaps.db.TipDAO;
import ru.zmaps.db.WayDAO;
import ru.zmaps.parser.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlHandler extends DefaultHandler {

    Map<Long, Node> nodes = new HashMap<>();

    @Autowired
    NodeDAO nodeDAO;

    @Autowired
    WayDAO wayDAO;

    @Autowired
    TipDAO tipDAO;

    @Autowired
    RelationDAO relationDAO;

    final List<Node> listNode = new ArrayList<>();
    final List<Way> listWay = new ArrayList<>();
    final List<Tip> listTips = new ArrayList<>();
    final List<Relation> listRelation = new ArrayList<>();

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public XmlHandler() {
        executor.execute(this::run);
    }


    private Element lastElem = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        parseElem(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("node".equals(qName)) {
            Node lastElem = (Node) this.lastElem;
            nodes.put(lastElem.getId(), lastElem);

            synchronized (listNode) {
                listNode.add(lastElem);
            }

            return;
        }

        if ("way".equals(qName)) {
            Way way = (Way) lastElem;
            synchronized (listWay) {
                listWay.add(way);
            }
        }

        if ("relation".equals(qName)) {
            synchronized (listRelation) {
                listRelation.add((Relation) lastElem);
            }
        }

    }

    private void parseElem(String uri, String localName, String qName, Attributes attributes) {
        if ("node".equals(qName)) {
            long id = Long.parseLong(attributes.getValue("id"));
            double lon = Double.parseDouble(attributes.getValue("lon"));
            double lat = Double.parseDouble(attributes.getValue("lat"));

            lastElem = new Node(lat, lon, id);

            return;
        }

        if ("tag".equals(qName)) {
            String key = attributes.getValue("k").replace('.', '-');
            String val = attributes.getValue("v").replace('.', '-');

            if (key.equals("name") && lastElem instanceof Way) {
                synchronized (listTips) {
                    listTips.add(new Tip(val, lastElem.getId()));
                }
            }


            lastElem.addTag(key, val);

            return;
        }

        if ("nd".equals(qName)) {
            ((Way) lastElem).addNode(nodes.get(Long.valueOf(attributes.getValue("ref"))));
        }

        if ("way".equals(qName)) {
            long id = Long.parseLong(attributes.getValue("id"));
            lastElem = new Way(id);
        }

        if ("member".equals(qName)) {
            String role = attributes.getValue("role");
            String ref = attributes.getValue("ref");
            if (role != null && ref != null) {
                ((Relation) lastElem).addMember(role, Long.parseLong(ref));
            }
        }

        if ("relation".equals(qName)) {
            long id = Long.parseLong(attributes.getValue("id"));
            lastElem = new Relation(id);
        }

    }

    public List<Node> getListNode() {
        return listNode;
    }

    public List<Tip> getListTips() {
        return listTips;
    }

    public List<Way> getListWay() {
        return listWay;
    }

    @SneakyThrows
    private void run() {
        while (true) {
            try {
                synchronized (listNode) {
                    if (listNode.size() > 0) {
                        nodeDAO.save(listNode);
                        listNode.clear();
                    }
                }

                synchronized (listWay) {
                    if (listWay.size() > 0) {
                        wayDAO.save(listWay);
                        listWay.clear();
                    }
                }

                synchronized (listTips) {
                    if (listTips.size() > 0) {
                        tipDAO.save(listTips);
                        listTips.clear();
                    }
                }

                synchronized (listRelation) {
                    if (listRelation.size() > 0) {
                        relationDAO.save(listRelation);
                        listRelation.clear();
                    }
                }
            } catch (Exception ex) {
                log.error(ex);
            }

            Thread.sleep(500);
        }
    }
}
