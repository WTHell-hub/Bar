package Bar.context;

import Bar.view.InterfazPrincipal.PanelProductos.ProductoManager;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class UIContext {
    private FlowPane paneCuentas;
    private TilePane paneProductos;
    private TilePane tilePanePA;
    private TextField buscar;
    private ComboBox<String> filtro;
    private Button btnCerrar;
    private Button btnAggProducto;
    private VBox panelProducto;
    private VBox paneObjTemp;
    private TilePane tilePaneObjTemp;
    private Button btnAceptar;
    private Button btnCancelar;
    private VBox panelPA;
    private Label lblNombreCuenta;


    public UIContext(FlowPane paneCuentas, TilePane paneProductos, TilePane tilePanePA,
                     TextField buscar, ComboBox<String> filtro, Button btnCerrar,
                     Button btnAggProducto, VBox panelProducto, VBox paneObjTemp,
                     TilePane tilePaneObjTemp, Button btnAceptar, Button btnCancelar, VBox panelPA, Label lblNombreCuenta) {
        this.paneCuentas = paneCuentas;
        this.paneProductos = paneProductos;
        this.tilePanePA = tilePanePA;
        this.buscar = buscar;
        this.filtro = filtro;
        this.btnCerrar = btnCerrar;
        this.btnAggProducto = btnAggProducto;
        this.panelProducto = panelProducto;
        this.paneObjTemp = paneObjTemp;
        this.tilePaneObjTemp = tilePaneObjTemp;
        this.btnAceptar = btnAceptar;
        this.btnCancelar = btnCancelar;
        this.panelPA = panelPA;
        this.lblNombreCuenta = lblNombreCuenta;
    }

    public FlowPane getPaneCuentas() { return paneCuentas; }
    public TilePane getPaneProductos() { return paneProductos; }
    public TilePane getTilePanePA() { return tilePanePA; }
    public TextField getBuscar() { return buscar; }
    public ComboBox<String> getFiltro() { return filtro; }
    public Button getBtnCerrar() { return btnCerrar; }
    public Button getBtnAggProducto() { return btnAggProducto; }
    public VBox getPanelProducto() { return panelProducto; }
    public VBox getPaneObjTemp() { return paneObjTemp; }
    public TilePane getTilePaneObjTemp() { return tilePaneObjTemp; }
    public Button getBtnAceptar() { return btnAceptar; }
    public Button getBtnCancelar() { return btnCancelar; }
    public VBox getPanelPA() { return panelPA; }
    public Label getLblNombreCuenta() { return lblNombreCuenta; }
}
