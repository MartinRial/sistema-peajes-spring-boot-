package obligatorio.da.sistemaPeajes.utils;

import java.util.ArrayList;
import java.util.List;

public class Respuesta {
    private String id;
    private Object parametro;

    public Respuesta(String id, Object parametro) {
        this.id = id;
        this.parametro = parametro;
    }

    public Respuesta() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getParametro() {
        return parametro;
    }

    public void setParametro(Object parametro) {
        this.parametro = parametro;
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public static List<Respuesta> lista(Respuesta... respuestas) {
        List<Respuesta> retorno = new ArrayList<>();
        for (Respuesta r : respuestas) {
            retorno.add(r);
        }
        return retorno;
    }
}