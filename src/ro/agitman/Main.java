/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.agitman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import ro.agitman.model.Action;
import ro.agitman.model.StrutsConfig;
import ro.agitman.tilesModel.Definition;
import ro.agitman.tilesModel.TilesDefinitions;
import ro.agitman.work.IOUtils;
import ro.agitman.work.ModelCrawler;
import ro.agitman.work.OutputModel;

/**
 *
 * @author gitmaal
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws javax.xml.bind.JAXBException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXNotSupportedException
     * @throws java.io.FileNotFoundException
     * @throws org.xml.sax.SAXNotRecognizedException
     */
    public Main(String[] args) throws JAXBException, ParserConfigurationException, SAXNotSupportedException, SAXException, SAXNotRecognizedException, FileNotFoundException {
        Map<String, StrutsConfig> strutsConfigList = new HashMap<>();
        Map<String, TilesDefinitions> tilesDefinitionList = new HashMap<>();

        if (args.length != 2) {
            System.out.println("At least 2 params, location to WEB-INF and string to search !");
            System.exit(-1);
        }

        System.out.println("Start reading files...");
        File webInf = new File(args[0]);
        if (!webInf.exists() || !webInf.canRead() || !webInf.isDirectory()) {
            System.out.println("Given location is eigther missing or unreadable or not a directory !");
            System.exit(-1);
        }

        for (File f : webInf.listFiles()) {
            if (f.getName().startsWith("struts-config")) {
                strutsConfigList.put(f.getName(), IOUtils.parseStrutsConfig(f.getAbsolutePath()));
            }
            if (f.getName().startsWith("tiles-")) {
                tilesDefinitionList.put(f.getName(), IOUtils.parseTilesConfig(f.getAbsolutePath()));
            }
        }

        countActionsPerFile(strutsConfigList);
        countTilesDefinitionsPerFile(tilesDefinitionList);

        System.out.println("\n#####################################################");
        String search = args[1].startsWith("/") ? args[1] : "/" + args[1];
        System.out.println("Start building hierarchy for [" + search + "] ...\n");

        ModelCrawler mc = new ModelCrawler();
        mc.setStrutsConfigList(strutsConfigList);
        mc.setTilesDefinitionList(tilesDefinitionList);
        OutputModel out = mc.crawle(search);

        System.out.println("#####################################################");
        System.out.println("#      ACTIONS  found: " + out.getActions().size());
        System.out.println("#####################################################\n");
        for (Map.Entry<Integer, Action> entry : out.getActions().entrySet()) {
            for (int i = 0; i < entry.getKey(); i++) {
                System.out.print("\t");
            }
            System.out.print("ident: " + entry.getKey() + "\n");

            System.out.print("ACTION OUTPUT \n" + entry.getValue() + "\n\n");
        }

        System.out.println("#####################################################");
        System.out.println("#      TILES found: " + out.getDefinitions().size());
        System.out.println("#####################################################\n");

        for (Definition entry : out.buildRoots()) {
            printTile(entry, 0);
        }
    }

    private void printTile(Definition definition, int index) {
        for (int i = 0; i < index; i++) {
            System.out.print("\t");
        }
        System.out.print("ident: " + index + "\n");

        System.out.print("TILE DEFINITION OUTPUT \n" + definition + "\n\n");

        for (Definition d : definition.getChildren()) {
            printTile(d, index + 1);
        }
    }

    private void countActionsPerFile(Map<String, StrutsConfig> strutsConfigList) {
        int all = 0;
        System.out.println("\n#####################################################");
        System.out.println("Struts-config files read: " + strutsConfigList.size());

        for (Map.Entry<String, StrutsConfig> entry : strutsConfigList.entrySet()) {
            System.out.println("For file: " + entry.getKey() + " there are " + entry.getValue().getActionMappings().getAction().size() + " actions defined");
            all += entry.getValue().getActionMappings().getAction().size();
        }

        System.out.println("\nActions definitions: " + all);
    }

    private void countTilesDefinitionsPerFile(Map<String, TilesDefinitions> tilesDefinitionList) {
        int all = 0;
        System.out.println("\n#####################################################");
        System.out.println("Tiles files read: " + tilesDefinitionList.size());

        for (Map.Entry<String, TilesDefinitions> entry : tilesDefinitionList.entrySet()) {
            System.out.println("For file: " + entry.getKey() + " there are " + entry.getValue().getDefinition().size() + " tiles defined");
            all += entry.getValue().getDefinition().size();
        }

        System.out.println("\nTiles definitions: " + all);
    }

    public static void main(String[] args) {

        try {
            Main main = new Main(args);
        } catch (JAXBException | ParserConfigurationException | SAXException | FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
