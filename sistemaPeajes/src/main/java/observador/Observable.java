package observador;

import java.util.ArrayList;

public class Observable implements ObservableImpl {
    private final ArrayList<Observador> observadores = new ArrayList<>();
    
    @Override
     public void agregarObservador(Observador obs){
        if(!observadores.contains(obs)){
            observadores.add(obs);
        }
    }

    @Override
    public void quitarObservador(Observador obs){
        observadores.remove(obs);
    }

    @Override
    public void avisar(Object evento){
        ArrayList<Observador> copia = new ArrayList<>(observadores);
        for(Observador obs:copia){
            obs.actualizar(evento, this);
        }
    }

}
