package Bar.view.InterfazPrincipal.PanelProductos;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContext;
import Bar.model.Producto;
import Bar.service.CardService;
import Bar.service.ProductoService;
import Bar.view.InterfazPrincipal.Cuentas.CuentaManager;
import Bar.viewModel.ProductoViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ProductoManager {
    private final ProductoService service = new ProductoService();
    private final TilePane paneProductos;
    private final UIContext uiContext;
    private List<Producto> productos;
    private int idCuenta;
    private CuentaManager cuentaManager;

    public ProductoManager(UIContext uiContext) {
        this.paneProductos = uiContext.getPaneProductos();
        this.uiContext = uiContext;
        productos = service.CargarProductosDB();
    }

    public void CargarProductos(int id) throws IOException {
        //Esto es para traer él id de la cuenta que se está tratando
        idCuenta = id;

        productos.clear();
        productos = service.CargarProductosDB();

        for (Producto p: productos) {
            ProductoViewModel vm = new ProductoViewModel(p);
            MostrarProducto(vm);
        }
    }

    public void MostrarProducto(ProductoViewModel vm) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/cardProducto.fxml"));
        Pane pane = loader.load();

        ProductoController controller = loader.getController();

        controller.setData(vm, uiContext);

        paneProductos.getChildren().add(pane);

        AnimacionesUI.slideInFromRight(pane, 100, 200);
    }

    public void BorrarElementos() {
        paneProductos.getChildren().clear();
    }

    public void VincularProductos() {
        TilePane tilePaneObjTemp = uiContext.getTilePaneObjTemp();

        List<Producto> lista = new ArrayList<>();

        for (Node p: tilePaneObjTemp.getChildren()) {
            Label id = (Label) p.lookup("#ID");
            Label nombre = (Label) p.lookup("#lblNombre");
            Label cantidad = (Label) p.lookup("#lblCantidad");
            Label precio = (Label) p.lookup("#lblPrecio");
            Label categoria = (Label) p.lookup("#lblCategoria");

            lista.add(new Producto(Integer.parseInt(id.getText()), nombre.getText(), Double.parseDouble(cantidad.getText()), Double.parseDouble(precio.getText()), categoria.getText()));
        }

        AnimacionesUI.slideOutToRight(uiContext.getPaneObjTemp(), 100, 200);
        AnimacionesUI.slideOutToRight(uiContext.getPanelProducto(), 300, 200);

        Connection conn = service.VincularProductos(idCuenta, lista);
        service.RestarProductos(conn, lista);

        AnimacionesUI.slideOutToRight(uiContext.getPanelPA(), 100, -300);
    }

    public void ActualizarCuentas() {
        double total = service.CalcularTotal(idCuenta);

        //Actualizamos la cuenta total en la base de datos, usando el service perteneciente
        CardService cardService = new CardService();
        cardService.ActualizarTotal(idCuenta, total);

        cuentaManager.ActualizarTotal(idCuenta, total);
    }

    public void setCuentaManager(CuentaManager cuentaManager) {
        this.cuentaManager = cuentaManager;
    }

    public void FiltrarProductos(String filtro) {
        paneProductos.getChildren().clear();

        for (Producto p: productos) {
            if (p.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                ProductoViewModel vm = new ProductoViewModel(p);
                try {
                    MostrarProducto(vm);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void MostrarTodosLosProductos() {
        paneProductos.getChildren().clear();

        for (Producto p: productos) {
            ProductoViewModel vm = new ProductoViewModel(p);
            try {
                MostrarProducto(vm);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void MostrarConFiltro(String categoria) {
        paneProductos.getChildren().clear();

        for (Producto p: productos) {
            if (p.getCategoria().equals(categoria)) {
                ProductoViewModel vm = new ProductoViewModel(p);
                try {
                    MostrarProducto(vm);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
