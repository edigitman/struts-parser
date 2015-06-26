/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.agitman.work;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import ro.agitman.model.StrutsConfig;
import ro.agitman.tilesModel.TilesDefinitions;

/**
 *
 * @author gitmaal
 */
public class IOUtils {

    private static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    private static final String FEATURES_VALIDATE = "http://xml.org/sax/features/validation";

    
    public static TilesDefinitions parseTilesConfig(final String file) throws JAXBException, ParserConfigurationException, SAXNotSupportedException, SAXNotRecognizedException, SAXException, FileNotFoundException {

        JAXBContext jc = JAXBContext.newInstance(TilesDefinitions.class);

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature(LOAD_EXTERNAL_DTD, false);
        spf.setFeature(FEATURES_VALIDATE, false);

        XMLReader xmlreader = spf.newSAXParser().getXMLReader();

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File(file);
        try {
            InputSource input = new InputSource(new FileReader(xml));
            Source source = new SAXSource(xmlreader, input);

            return (TilesDefinitions) unmarshaller.unmarshal(source);
        } catch (IOException | JAXBException exception) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }
    
    public static StrutsConfig parseStrutsConfig(final String file) throws JAXBException, SAXNotRecognizedException, ParserConfigurationException, SAXNotSupportedException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(StrutsConfig.class);

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature(LOAD_EXTERNAL_DTD, false);
        spf.setFeature(FEATURES_VALIDATE, false);

        XMLReader xmlreader = spf.newSAXParser().getXMLReader();

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File(file);
        try {
            InputSource input = new InputSource(new FileReader(xml));
            Source source = new SAXSource(xmlreader, input);

            return (StrutsConfig) unmarshaller.unmarshal(source);
        } catch (IOException | JAXBException exception) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    private static void totalDebugStrutsConfig(StrutsConfig sc) {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(StrutsConfig.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "SummaryCart.xsd");
            marshaller.marshal(sc, System.out);
        } catch (JAXBException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
