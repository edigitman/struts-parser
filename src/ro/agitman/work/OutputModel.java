/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.agitman.work;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import ro.agitman.model.Action;
import ro.agitman.tilesModel.Definition;

/**
 *
 * @author gitmaal
 */
public class OutputModel {

    private SortedMap<Integer, Action> actions;
    private List<Definition> definitions;
    private List<String> jspFiles;

    public List<Definition> buildRoots() {
        List<Definition> result = new ArrayList<>();

        for (Definition d : definitions) {
            Definition da = findAncestor(d);
            if (!result.contains(da)) {
                result.add(da);
            }
        }

        return result;
    }

    private Definition findAncestor(Definition d) {
        if (d.getAncestor() == null) {
            return d;
        } else {
            return findAncestor(d.getAncestor());
        }
    }

    public OutputModel() {
        this.definitions = new ArrayList<>();
        this.actions = new TreeMap<>();
        this.jspFiles = new ArrayList<>();
    }

    public SortedMap<Integer, Action> getActions() {
        return actions;
    }

    public void setActions(SortedMap<Integer, Action> actions) {
        this.actions = actions;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public void addAction(Action a, int order) {
        this.actions.put(order, a);
    }

    public void addDefinition(Definition d) {
        this.definitions.add(d);
    }
}
