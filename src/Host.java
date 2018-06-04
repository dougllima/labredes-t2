import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Host {
    //Socket para entrada de arquivos desta maquina
    DatagramSocket server;
    //Socket para envio de arquivos desta maquina
    private DatagramSocket client;

    //Porta do roteador
    private int portaRoteador;

    //IP do host
    String ip;
    //Porta do Host
    int porta;

    Host(int portaRoteador, int porta, String ip) {
        System.out.println("Criando Host " + ip + ":" + porta);
        this.portaRoteador = portaRoteador;
        this.porta = porta;
        this.ip = ip;
    }

    public void aguardarArquivo() {
        new Thread(aguardarArquivo).start();
    }

    void enviarArquivos(Pacote pacote) {
        try {
            client = new DatagramSocket();
            byte[] sendData;
            InetAddress IPAddress = Inet6Address.getByName(pacote.getIpDestino());
            sendData = convertToBytes(pacote);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, pacote.getPortaDestino());
            System.out.println("Host " + porta + ": Enviardo Arquivo " + pacote.getNomeArquivo() + " para " + pacote.getIpDestino() + ":" + pacote.getPortaDestino());
            client.send(sendPacket);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void enviarArquivos(String ipDestino, int portaDestino, String nomeArquivo) {
        Pacote pacote = montaPacote(ipDestino, portaDestino, nomeArquivo);

        enviarArquivos(pacote);
    }


    private Runnable aguardarArquivo = new Runnable() {
        public void run() {
            try {
                server = new DatagramSocket(porta);
                byte[] receiveData = new byte[8192];

                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    System.out.println("Host " + porta + " aguardando arquivo");
                    server.receive(receivePacket);
                    System.out.println("Host " + porta + ": Recebendo Pacote");
                    Pacote pacote = convertFromBytes(receivePacket.getData());
                    System.out.println("Host " + porta + ": " + pacote.toString());

                    if (salvarArquivo(pacote.getDados(), pacote.getNomeArquivo())) {
                        System.out.println("Host " + porta + ": Arquivo salvo com sucesso!");
                    } else {
                        System.out.println("Host " + porta + ": Arquivo recebido, mas ocorreu um erro ao salva-lo.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Pacote montaPacote(String ipDestino, int portaDestino, String nomeArquivo) {
        Pacote pacote = null;
        try {
            byte[] arquivo = Files.readAllBytes(Paths.get(nomeArquivo));
            Pacote aux = new Pacote(ip, porta, ipDestino, portaDestino, nomeArquivo, arquivo);

            //Verifica se a maquina de destino não está na mesma rede da maquina atual
            if (!ipDestino.equals(ip))
                //Se está em uma rede diferente, adicona uma camada de cabeçalho e envia para o roteador
                pacote = new Pacote(ip, porta, ip, portaRoteador, nomeArquivo, aux);
            else
                //Se não só segue
                pacote = aux;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pacote;
    }

    //Métodos auxiliares

    // Pacote -> Byte[]
    private byte[] convertToBytes(Pacote obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.toByteArray();
        }
    }

    // Byte[] -> Pacote
    Pacote convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return (Pacote) in.readObject();
        }
    }

    //Escreve o arquivo em disco
    boolean salvarArquivo(byte[] dados, String nome) {
        try (FileOutputStream out = new FileOutputStream(porta + nome)) {
            out.write(dados);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}