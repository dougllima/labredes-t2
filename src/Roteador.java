import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Roteador extends Host {
    Roteador(int porta, String ip) {
        super(porta, porta, ip);
    }

    @Override
    public void aguardarArquivo() {
        new Thread(aguardarArquivo).start();
    }

    private Runnable aguardarArquivo = () -> {
        try {
            server = new DatagramSocket(porta);
            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                System.out.println("Router " + porta + " aguardando arquivo");
                server.receive(receivePacket);
                System.out.println("Router " + porta + ": Recebendo Pacote");
                Pacote pacote = convertFromBytes(receivePacket.getData());
                System.out.println("Router " + porta + ": " + pacote.toString());

                //Verifica se o pacote já está na rede certa
                if (pacote.getIpDestino().equals(ip)) {
                    //Verifica se possui outro nivel de cabeçalho
                    if (pacote.getSubPacote() != null) {
                        //Possui mais uma camada de cabeçalho que indica quem é o destino real do pacote

                        //Remove o pacote de dentro do cabeçalho
                        Pacote aux = pacote.getSubPacote();

                        //Verifica se o pacote está na rede certa (deve sempre cair aqui)
                        if (aux.getIpDestino().equals(ip)) {
                            //Verifica se o pacote é para o roteador
                            if (pacote.getPortaDestino() == porta) {
                                if (salvarArquivo(pacote.getDados(), pacote.getNomeArquivo())) {
                                    System.out.println("Router " + porta + ": Arquivo salvo com sucesso!");
                                } else {
                                    System.out.println("Router " + porta + ": Arquivo recebido, mas ocorreu um erro ao salva-lo.");
                                }
                            //Caso o pacote não seja para o roteador, envia ao destinatario certo
                            } else {
                                enviarArquivos(aux);
                            }
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}
