/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.agitman.work;

import java.util.HashMap;
import java.util.Map;
import ro.agitman.model.Action;
import ro.agitman.model.Forward;
import ro.agitman.model.StrutsConfig;
import ro.agitman.tilesModel.Definition;
import ro.agitman.tilesModel.Put;
import ro.agitman.tilesModel.TilesDefinitions;

/**
 *
 * @author gitmaal
 */
public class ModelCrawler {

    Map<String, StrutsConfig> strutsConfigList = new HashMap<>();
    Map<String, TilesDefinitions> tilesDefinitionList = new HashMap<>();
    private OutputModel model = new OutputModel();

    public OutputModel crawle(String pattern) {

        crawleStruts(pattern, 0);

        return model;
    }

    private Definition buildTile(String path) {
        Definition tile = findDefinition(path);
        if (tile != null) {
            Definition tileAncestor = buildTile(tile.getExtends());
            if (tileAncestor != null) {
                tile.setAncestor(tileAncestor);
                tileAncestor.getChildren().add(tile);
            }
            model.addDefinition(tile);

            for (Put p : tile.getPut()) {
                Definition childe = buildTile(p.getvalue());
                if (childe != null) {
                    tile.getChildren().add(childe);
                }
            }

            return tile;
        }
        return null;
    }

    private void crawleTiles(Action action) {
        if (action.getForwardAtribute() != null) {
            buildTile(action.getForwardAtribute());
        }

        if (action.getForward() != null) {
            for (Forward f : action.getForward()) {
                if (f.getPath() != null && findAction(f.getPath()) == null) {
                    buildTile(f.getPath());
                }
            }
        }
    }

    private void crawleStruts(String path, int index) {
        Action action = findAction(path);
        if (action != null) {
            crawleTiles(action);
            model.addAction(action, index);
            crawleStruts(action.getForwardAtribute(), index + 1);
            for (Forward f : action.getForward()) {
                crawleStruts(f.getPath(), index + 1);
            }
        }
    }

    private Definition findDefinition(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, TilesDefinitions> entry : tilesDefinitionList.entrySet()) {
            for (Definition definition : entry.getValue().getDefinition()) {
                if (definition.getName().equals(name)) {
                    return definition;
                }
            }
        }
        return null;
    }

    private Action findAction(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        if (path.endsWith(".do")) {
            path = path.substring(0, path.length() - 3);
        }

        for (Map.Entry<String, StrutsConfig> entry : strutsConfigList.entrySet()) {
            for (Action action : entry.getValue().getActionMappings().getAction()) {
                if (action.getPath().equals(path)) {
                    return action;
                }
            }
        }
        return null;
    }

    public Map<String, StrutsConfig> getStrutsConfigList() {
        return strutsConfigList;
    }

    public void setStrutsConfigList(Map<String, StrutsConfig> strutsConfigList) {
        this.strutsConfigList = strutsConfigList;
    }

    public Map<String, TilesDefinitions> getTilesDefinitionList() {
        return tilesDefinitionList;
    }

    public void setTilesDefinitionList(Map<String, TilesDefinitions> tilesDefinitionList) {
        this.tilesDefinitionList = tilesDefinitionList;
    }

    public OutputModel getModel() {
        return model;
    }
}
