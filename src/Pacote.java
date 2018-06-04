import java.io.Serializable;

public class Pacote implements Serializable {
    private String ipOrigem;
    private int portaOrigem;

    private String ipDestino;
    private int portaDestino;

    private String nomeArquivo;
    private byte[] dados;

    //sub pacote utilizado no redirecionamento quando necessário
    private Pacote subPacote;

    public Pacote(String ipOrigem, int portaOrigem, String ipDestino, int portaDestino, String nomeArquivo, byte[] dados) {
        this.ipOrigem = ipOrigem;
        this.portaOrigem = portaOrigem;
        this.ipDestino = ipDestino;
        this.portaDestino = portaDestino;
        this.nomeArquivo = nomeArquivo;
        this.dados = dados;
    }

    public Pacote(String ipOrigem, int portaOrigem, String ipDestino, int portaDestino, String nomeArquivo, Pacote subPacote) {
        this.ipOrigem = ipOrigem;
        this.portaOrigem = portaOrigem;
        this.ipDestino = ipDestino;
        this.portaDestino = portaDestino;
        this.nomeArquivo = nomeArquivo;
        this.subPacote = subPacote;
    }

    //GETs
    public String getIpOrigem() {
        return ipOrigem;
    }

    public int getPortaOrigem() {
        return portaOrigem;
    }

    public String getIpDestino() {
        return ipDestino;
    }

    public int getPortaDestino() {
        return portaDestino;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public byte[] getDados() {
        return dados;
    }

    public Pacote getSubPacote() {
        return subPacote;
    }

    @Override
    public String toString() {
        return "Pacote: " +
                " Origem -> " + ipOrigem + ':' + portaOrigem + '\n' +
                " Destino -> " + ipDestino + ':' + portaDestino + '\n' +
                (nomeArquivo.isEmpty() ? "" : "Arquivo -> " + nomeArquivo + '\n');
    }
}
