package Bar.view.InterfazPrincipal;

import javafx.scene.Node;
import javafx.scene.layout.TilePane;

import java.util.Map;

public class TilePanePAManager {
    private TilePane tilePane;

    public TilePanePAManager(Map<String, Node> componentes) {
        this.tilePane = (TilePane) componentes.get("tilePanePA");
    }

    public void MostrarProductosPorCuenta(int id) {

    }
}
