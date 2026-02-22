package Bar.Animaciones;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimacionesUI {

    // Hace que el nodo aparezca suavemente aumentando su opacidad de 0 a 1.
    // Consejo: ideal para mostrar paneles o ventanas sin que aparezcan de golpe.
    public static void fadeIn(Node node, int millis) {
        node.setVisible(true);
        FadeTransition fade = new FadeTransition(Duration.millis(millis), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    // Hace que el nodo desaparezca suavemente reduciendo su opacidad de 1 a 0.
    // Consejo: perfecto para ocultar paneles sin que desaparezcan bruscamente.
    public static void fadeOut(Node node, int millis) {
        FadeTransition fade = new FadeTransition(Duration.millis(millis), node);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> node.setVisible(false));
        fade.play();
    }

    // Desliza el nodo desde la derecha hacia su posición original.
    // Consejo: útil para paneles laterales o menús que entran desde fuera de la pantalla.
    public static void slideInFromRight(Node node, int millis, double distance) {
        node.setVisible(true);
        node.setTranslateX(distance);
        TranslateTransition slide = new TranslateTransition(Duration.millis(millis), node);
        slide.setFromX(distance);
        slide.setToX(0);
        slide.play();
    }

    // Desliza el nodo hacia la derecha y lo oculta al terminar.
    // Consejo: complementa a slideInFromRight para cerrar paneles con elegancia.
    public static void slideOutToRight(Node node, int millis, double distance) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(millis), node);
        slide.setFromX(0);
        slide.setToX(distance);
        slide.setOnFinished(e -> node.setVisible(false));
        slide.play();
    }

    // Mueve el nodo rápidamente a izquierda y derecha simulando un temblor.
    // Consejo: úsalo para indicar errores o campos inválidos (feedback visual).
    public static void shake(Node node, int millis) {
        node.setVisible(true);
        TranslateTransition tt = new TranslateTransition(Duration.millis(millis), node);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    // Aumenta y disminuye el tamaño del nodo suavemente como un latido.
    // Consejo: ideal para llamar la atención sobre botones o elementos interactivos.
    public static void pulse(Node node, int millis) {
        node.setVisible(true);
        ScaleTransition st = new ScaleTransition(Duration.millis(millis), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.1);
        st.setToY(1.1);
        st.setCycleCount(4);
        st.setAutoReverse(true);
        st.play();
    }

    // Rota el nodo en el eje Y simulando que se voltea como una carta.
    // Consejo: muy útil para tarjetas o vistas que cambian de cara.
    public static void flip(Node node, int millis) {
        node.setVisible(true);
        RotateTransition rt = new RotateTransition(Duration.millis(millis), node);
        rt.setAxis(javafx.geometry.Point3D.ZERO.add(0,1,0)); // eje Y
        rt.setFromAngle(0);
        rt.setToAngle(180);
        rt.setCycleCount(2);
        rt.setAutoReverse(true);
        rt.play();
    }

    // Hace zoom acercando el nodo (escala mayor).
    // Consejo: úsalo para destacar un panel o elemento al abrirlo.
    public static void zoomIn(Node node, int millis) {
        node.setVisible(true);
        ScaleTransition st = new ScaleTransition(Duration.millis(millis), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.5);
        st.setToY(1.5);
        st.play();
    }

    // Hace zoom alejando el nodo (escala menor).
    // Consejo: complementa a zoomIn para devolver el nodo a su tamaño normal.
    public static void zoomOut(Node node, int millis) {
        node.setVisible(true);
        ScaleTransition st = new ScaleTransition(Duration.millis(millis), node);
        st.setFromX(1.5);
        st.setFromY(1.5);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    // Rota el nodo ligeramente hacia los lados como un columpio.
    // Consejo: da un efecto juguetón o de atención, útil para destacar elementos.
    public static void swing(Node node, int millis) {
        node.setVisible(true);
        RotateTransition rt = new RotateTransition(Duration.millis(millis), node);
        rt.setFromAngle(-15);
        rt.setToAngle(15);
        rt.setCycleCount(4);
        rt.setAutoReverse(true);
        rt.play();
    }

    // Combina traslación y rotación para simular que el nodo tambalea.
    // Consejo: efecto cómico o de alerta, útil para llamar la atención.
    public static void wobble(Node node, int millis) {
        node.setVisible(true);
        TranslateTransition tt = new TranslateTransition(Duration.millis(millis), node);
        tt.setFromX(0);
        tt.setByX(5);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);

        RotateTransition rt = new RotateTransition(Duration.millis(millis), node);
        rt.setFromAngle(-5);
        rt.setToAngle(5);
        rt.setCycleCount(6);
        rt.setAutoReverse(true);

        tt.play();
        rt.play();
    }

    // Hace que el nodo aparezca deslizándose desde la derecha y desvaneciéndose a la vez.
    // Consejo: muy elegante para mostrar paneles laterales o menús contextuales.
    public static void fadeSlideIn(Node node, int millis, double distance) {
        node.setVisible(true);
        node.setTranslateX(distance);

        TranslateTransition slide = new TranslateTransition(Duration.millis(millis), node);
        slide.setFromX(distance);
        slide.setToX(0);

        FadeTransition fade = new FadeTransition(Duration.millis(millis), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        new ParallelTransition(slide, fade).play();
    }
}

/*
🔹 Consejos rápidos de uso
Para mostrar/ocultar paneles: usa fadeIn, fadeOut, slideInFromRight, slideOutToRight, fadeSlideIn.

Para feedback visual (errores o atención): usa shake, pulse, swing, wobble.

Para efectos especiales: usa flip (tarjetas), zoomIn/zoomOut (destacar elementos).

Todas las animaciones de entrada ya hacen setVisible(true).

Todas las animaciones de salida ocultan el nodo con setVisible(false) al terminar.

Las animaciones decorativas no ocultan el nodo, solo lo animan.
 */