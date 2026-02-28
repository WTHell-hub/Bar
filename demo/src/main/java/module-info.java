module src.fromcontroller.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires transitive javafx.graphics;
    requires transitive javafx.base;

    exports Bar.db;
    exports Bar.app;
    opens Bar.app to javafx.fxml;
    exports Bar.viewModel;
    exports Bar.model;
    exports Bar.service;
    exports Bar.context;
    exports Bar.view.InterfazPrincipal;
    opens Bar.view.InterfazPrincipal to javafx.fxml;
    exports Bar.view.InterfazAlmacen;
    opens Bar.view.InterfazAlmacen to javafx.fxml;
    exports Bar.view.InterfazPrincipal.PanelProductos;
    opens Bar.view.InterfazPrincipal.PanelProductos to javafx.fxml;
    exports Bar.view.InterfazPrincipal.StagePrincipal;
    opens Bar.view.InterfazPrincipal.StagePrincipal to javafx.fxml;
    exports Bar.view.InterfazPrincipal.Cuentas;
    opens Bar.view.InterfazPrincipal.Cuentas to javafx.fxml;
}