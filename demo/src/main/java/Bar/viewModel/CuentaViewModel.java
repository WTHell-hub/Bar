package Bar.viewModel;

import Bar.model.Card;
import javafx.beans.property.*;

public class CuentaViewModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty fecha = new SimpleStringProperty();
    private final DoubleProperty total = new SimpleDoubleProperty();

    public CuentaViewModel(Card card) {
        id.set(card.getId());
        nombre.set(card.getNombre());
        fecha.set(card.getFecha());
        total.set(card.getTotal());
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty fechaProperty() {
        return fecha;
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
