import java.util.Hashtable;

class Rede {
    //Porta onde ficara o roteador, será incrementada para gerar os outros hosts;
    private int portaBase;
    //IP da maquina (deve ser configurado na mão)
    String ip;

    Roteador router;
    Hashtable<Integer, Host> hosts = new Hashtable<>();

    Rede(int portaBase, String ip, int qntHosts) {
        //Transforma a porta base para termine sempre com 0
        portaBase = portaBase - (portaBase % 10);

        this.portaBase = portaBase;
        this.ip = ip;

        //Criar o roteador
        router = new Roteador(portaBase, ip);

        router.aguardarArquivo();

        //Criar os clientes
        for (int i = 1; i <= qntHosts; i++) {
            int portaAtual = portaBase + i;

            //Adiciona o cliente a um map feito a partir da porta dele (mais facil para localizar ele depois)
            hosts.put(portaAtual, new Host(portaBase, portaAtual, ip));

            //Inicial o socket que vai aguardar o arquivo
            hosts.get(portaAtual).aguardarArquivo();
        }
    }

    void enviarArquivo(int portaOrigem, String ipDestino, int portaDestino, String nomeArquivo) {
        if (portaOrigem == portaBase)
            router.enviarArquivos(ipDestino, portaDestino, nomeArquivo);
        else {
            Host host = hosts.get(portaOrigem);

            if (host != null) {
                host.enviarArquivos(ipDestino, portaDestino, nomeArquivo);
            }
        }
    }

    String hostsToString() {
        StringBuilder s = new StringBuilder();

        s.append("Router ").append(portaBase).append('\n');

        hosts.forEach((e, i) -> s.append("Host ").append(e.toString()).append('\n'));

        return s.toString();
    }
}
