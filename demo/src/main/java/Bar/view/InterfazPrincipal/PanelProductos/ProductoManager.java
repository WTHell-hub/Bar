package Bar.view.InterfazPrincipal.PanelProductos;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContextDep;
import Bar.model.Producto;
import Bar.service.Trabajador.CardService;
import Bar.service.Trabajador.ProductoService;
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
    private final ProductoService productoService = new ProductoService();
    private final TilePane paneProductos;
    private final UIContextDep uiContextDep;
    private List<Producto> productos;
    private int idCuenta;
    private CuentaManager cuentaManager;

    public ProductoManager(UIContextDep uiContextDep) {
        this.paneProductos = uiContextDep.getPaneProductos();
        this.uiContextDep = uiContextDep;
        productos = productoService.CargarProductosDB();
    }

    public void CargarProductos(int id) throws IOException {
        //Esto es para traer él id de la cuenta que se está tratando
        idCuenta = id;

        productos.clear();
        productos = productoService.CargarProductosDB();

        for (Producto p: productos) {
            ProductoViewModel vm = new ProductoViewModel(p);
            MostrarProducto(vm);
        }
    }

    public void MostrarProducto(ProductoViewModel vm) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/Dependiente/cardProducto.fxml"));
        Pane pane = loader.load();

        ProductoController controller = loader.getController();

        controller.setData(vm, uiContextDep);

        paneProductos.getChildren().add(pane);

        AnimacionesUI.slideInFromRight(pane, 100, 200);
    }

    public void BorrarElementos() {
        paneProductos.getChildren().clear();
    }

    public void VincularProductos() {
        TilePane tilePaneObjTemp = uiContextDep.getTilePaneObjTemp();

        List<Producto> lista = new ArrayList<>();

        for (Node p: tilePaneObjTemp.getChildren()) {
            Label id = (Label) p.lookup("#ID");
            Label nombre = (Label) p.lookup("#lblNombre");
            Label cantidad = (Label) p.lookup("#lblCantidad");
            Label precio = (Label) p.lookup("#lblPrecio");
            Label categoria = (Label) p.lookup("#lblCategoria");

            lista.add(new Producto(Integer.parseInt(id.getText()), nombre.getText(), Double.parseDouble(cantidad.getText()), Double.parseDouble(precio.getText()), categoria.getText()));
        }

        Connection conn = productoService.VincularProductos(idCuenta, lista);
        productoService.RestarProductos(conn, lista);

        AnimacionesUI.slideOutToRight(uiContextDep.getPaneObjTemp(), 100, 200);
        uiContextDep.getPaneObjTemp().setManaged(false);
        AnimacionesUI.slideOutToRight(uiContextDep.getPanelProducto(), 300, 200);

        AnimacionesUI.slideOutToRight(uiContextDep.getPanelPA(), 100, -300);
    }

    public void ActualizarCuentas() {
        double total = productoService.CalcularTotal(idCuenta);

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

    public CuentaManager getCuentaManager() {
        return cuentaManager;
    }
}
