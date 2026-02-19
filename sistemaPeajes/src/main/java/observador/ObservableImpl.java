package observador;

public interface ObservableImpl {
    void agregarObservador(Observador obs);
    void quitarObservador(Observador obs);
    void avisar(Object evento);
}
