package Bar.context;

import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class UIContextDep {
    private FlowPane paneCuentas;
    private TilePane paneProductos;
    private TilePane tilePanePA;
    private TextField buscar;
    private ComboBox<String> filtro;
    private Button btnCerrar;
    private Button btnCerrarSeccion;
    private VBox panelProducto;
    private VBox paneObjTemp;
    private TilePane tilePaneObjTemp;
    private Button btnAceptar;
    private Button btnCancelar;
    private VBox panelPA;
    private Label lblNombreCuenta;
    private VBox panelRetirarProducto;
    private TextField txfCantidad;
    private RadioButton rbHecho;
    private RadioButton rbNoHecho;
    private TextArea txtAreaJustificacion;
    private Button btnAceptarEliminacion;
    private Button btnCancelarEliminacion;


    public UIContextDep(FlowPane paneCuentas, TilePane paneProductos, TilePane tilePanePA,
                        TextField buscar, ComboBox<String> filtro, Button btnCerrar,
                        Button btnCerrarSeccion, VBox panelProducto, VBox paneObjTemp,
                        TilePane tilePaneObjTemp, Button btnAceptar, Button btnCancelar, VBox panelPA, Label lblNombreCuenta,
                        VBox panelRetirarProducto, TextField txfCantidad, RadioButton rbHecho, RadioButton rbNoHecho, TextArea txtAreaJustificacion,
                        Button btnAceptarEliminacion, Button btnCancelarEliminacion) {
        this.paneCuentas = paneCuentas;
        this.paneProductos = paneProductos;
        this.tilePanePA = tilePanePA;
        this.buscar = buscar;
        this.filtro = filtro;
        this.btnCerrar = btnCerrar;
        this.btnCerrarSeccion = btnCerrarSeccion;
        this.panelProducto = panelProducto;
        this.paneObjTemp = paneObjTemp;
        this.tilePaneObjTemp = tilePaneObjTemp;
        this.btnAceptar = btnAceptar;
        this.btnCancelar = btnCancelar;
        this.panelPA = panelPA;
        this.lblNombreCuenta = lblNombreCuenta;
        this.panelRetirarProducto = panelRetirarProducto;
        this.txfCantidad = txfCantidad;
        this.rbHecho = rbHecho;
        this.rbNoHecho = rbNoHecho;
        this.txtAreaJustificacion = txtAreaJustificacion;
        this.btnAceptarEliminacion = btnAceptarEliminacion;
        this.btnCancelarEliminacion = btnCancelarEliminacion;
    }

    public FlowPane getPaneCuentas() { return paneCuentas; }
    public TilePane getPaneProductos() { return paneProductos; }
    public TilePane getTilePanePA() { return tilePanePA; }
    public TextField getBuscar() { return buscar; }
    public ComboBox<String> getFiltro() { return filtro; }
    public Button getBtnCerrar() { return btnCerrar; }
    public Button getBtnCerrarSeccion() { return btnCerrarSeccion; }
    public VBox getPanelProducto() { return panelProducto; }
    public VBox getPaneObjTemp() { return paneObjTemp; }
    public TilePane getTilePaneObjTemp() { return tilePaneObjTemp; }
    public Button getBtnAceptar() { return btnAceptar; }
    public Button getBtnCancelar() { return btnCancelar; }
    public VBox getPanelPA() { return panelPA; }
    public Label getLblNombreCuenta() { return lblNombreCuenta; }
    public VBox getPanelRetirarProducto() { return panelRetirarProducto; }
    public TextField getTxfCantidad() { return txfCantidad; }
    public RadioButton getRbHecho() { return rbHecho; }
    public RadioButton getRbNoHecho() { return rbNoHecho; }
    public TextArea getTxtAreaJustificacion() { return txtAreaJustificacion; }
    public Button getBtnAceptarEliminacion() { return btnAceptarEliminacion; }
    public Button getBtnCancelarEliminacion() { return btnCancelarEliminacion; }
}
