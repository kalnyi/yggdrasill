package com.iggdrasill.baldr.parser.impl;

import com.iggdrasill.baldr.parser.Parser;
import com.iggdrasill.baldr.parser.utils.CastingStrategy;
import com.iggdrasill.baldr.parser.utils.IntegerCaster;
import com.iggdrasill.baldr.parser.utils.StringCaster;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Alex Kalnij
 * @date Mar 11, 2014
 */
public class CsvParser implements Parser {

    public static final String SEPARATOR_FIELD_NAME = "separator";
    private Map<Type, CastingStrategy> strategyMap = new HashMap<Type, CastingStrategy>();

    public CsvParser() {
        strategyMap.put(String.class, new StringCaster());
        strategyMap.put(Integer.class, new IntegerCaster());
        strategyMap.put(int.class, new IntegerCaster());
    }

    public <T> List<T> parse(InputStream file, Class<T> type, InputStream config) {
        try {
            List<T> parsedList = new ArrayList<T>();
            Map<String, String> configMap = getConfigMap(config);
            List<String> document = readDocument(file);
            for (String documentLine : document) {
                T parsedLine = parseLine(documentLine, type, configMap);
                parsedList.add(parsedLine);
            }

            return parsedList;
        } catch (Exception ex) {
            //TODO:
            throw new IllegalArgumentException(ex);
        }
    }

    private Map<String, String> getConfigMap(InputStream config) throws ParserConfigurationException, SAXException, IOException {
        Map<String, String> configMap = new HashMap<String, String>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(config);
        doc.getDocumentElement().normalize();
        NodeList childNodes = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getChildNodes().getLength() == 1) {
                String nodeName = item.getNodeName();
                String nodeValue = item.getChildNodes().item(0).getNodeValue();
                System.out.println("!!! " + nodeName);
                System.out.println("!!! " + nodeValue);
                configMap.put(nodeName, nodeValue);
            }
        }

        return configMap;
    }

    private List<String> readDocument(InputStream file) throws IOException {
        List<String> document = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            document.add(line);
        }

        return document;
    }

    private <T> T parseLine(String documentLine, Class<T> type, Map<String, String> configMap) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        T instance = type.getConstructor().newInstance();
        String separator = configMap.get(SEPARATOR_FIELD_NAME);
        String[] fields = documentLine.split(separator.trim());
        for (Field field : type.getDeclaredFields()) {
            String name = field.getName();
            String position = configMap.get(name);
            if (position == null) {
                continue;
            }
            int posInt = Integer.parseInt(position.trim());
            String value = fields[posInt - 1];
            Object cast = castValue(value, field.getGenericType());
            field.setAccessible(true);
            field.set(instance, cast);
        }

        return instance;
    }

    private Object castValue(String value, Type genericType) {
        CastingStrategy caster = strategyMap.get(genericType);
        Object cast = caster.cast(value);

        return cast;
    }
}
