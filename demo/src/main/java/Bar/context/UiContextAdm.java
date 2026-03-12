package Bar.context;

import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.awt.*;

public class UiContextAdm {
    private VBox panelInfoGeneralIzq;
    private VBox panelInfoGeneralDer;
    private VBox panelInfoGeneralCen;
    private TilePane cuentasAtendidas;
    private TilePane productosAtendidos;
    private TilePane productosAlmacen;
    private TilePane insumosAlmacen;
    private TilePane productosDevueltos;
    private TextArea textAreaCausa;

    public UiContextAdm(VBox panelInfoGeneralIzq, VBox panelInfoGeneralDer, VBox panelInfoGeneralCen,
                        TilePane cuentasAtendidas, TilePane productosAtendidos, TilePane productosAlmacen,
                        TilePane insumosAlmacen, TilePane productosDevueltos, TextArea textAreaCausa) {

        this.panelInfoGeneralIzq = panelInfoGeneralIzq;
        this.panelInfoGeneralDer = panelInfoGeneralDer;
        this.panelInfoGeneralCen = panelInfoGeneralCen;
        this.cuentasAtendidas = cuentasAtendidas;
        this.productosAtendidos = productosAtendidos;
        this.productosAlmacen = productosAlmacen;
        this.insumosAlmacen = insumosAlmacen;
        this.productosDevueltos = productosDevueltos;
        this.textAreaCausa = textAreaCausa;
    }

    public VBox getPanelInfoGeneralIzq() {
        return panelInfoGeneralIzq;
    }

    public VBox getPanelInfoGeneralDer() {
        return panelInfoGeneralDer;
    }

    public VBox getPanelInfoGeneralCen() {
        return panelInfoGeneralCen;
    }

    public TilePane getCuentasAtendidas() {
        return cuentasAtendidas;
    }

    public TilePane getProductosAtendidos() {
        return productosAtendidos;
    }

    public TilePane getProductosAlmacen() {
        return productosAlmacen;
    }

    public TilePane getInsumosAlmacen() {
        return insumosAlmacen;
    }

    public TilePane getProductosDevueltos() {
        return productosDevueltos;
    }

    public TextArea getTextAreaCausa() {
        return textAreaCausa;
    }
}
