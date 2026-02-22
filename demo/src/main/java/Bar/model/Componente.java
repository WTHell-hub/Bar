package Bar.model;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;

public class Componente {
    FlowPane flowPane;
    TilePane tilePane;
    TilePane tilePanePA;

    public Componente(FlowPane flowPane, TilePane tilePane, TilePane tilePanePA) {
        this.flowPane = flowPane;
        this.tilePane = tilePane;
        this.tilePanePA = tilePanePA;
    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    public void setFlowPane(FlowPane flowPane) {
        this.flowPane = flowPane;
    }

    public TilePane getTilePane() {
        return tilePane;
    }

    public void setTilePane(TilePane tilePane) {
        this.tilePane = tilePane;
    }

    public TilePane getTilePanePA() {
        return tilePanePA;
    }

    public void setTilePanePA(TilePane tilePanePA) {
        this.tilePanePA = tilePanePA;
    }
}
