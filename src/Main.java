import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Main {

    private static Scanner in = new Scanner(System.in);

    public static void main(String args[]) {
        int porta;
        int qntHosts;

        String ip = "";

        boolean repeat = true;
        do {
            try {
                System.out.println("Informe seu IPV6: ");
                ip = in.next();

                InetAddress ipv6 = Inet6Address.getByName(ip);
                repeat = false;
            } catch (UnknownHostException e) {
                System.out.println("Opa! IP inválido, tente novamente.");
                repeat = true;
                e.printStackTrace();
            }
        } while (repeat);

        do {
            System.out.println("Informe que será usado no roteador (final 0): ");
            porta = in.nextInt();

            if (porta > 4000)
                repeat = false;
            else {
                repeat = true;
                System.out.println("Opa! Porta inválida, tente novamente");
            }
        } while (repeat);

        do {
            System.out.println("Informe a quantidade de hosts na rede (sem contar o roteador)");
            qntHosts = in.nextInt();

            if (qntHosts > 0)
                repeat = false;
            else {
                repeat = true;
                System.out.println("Opa! Quantidade inválida, tente novamente");
            }
        } while (repeat);

        System.out.println("Aguarde, criando rede. \n");

        Rede rede = new Rede(porta, ip, qntHosts);

        repeat = true;
        do{
            int opt;
            System.out.println("Escolha uma opção para continuar ou aguarde o recebimento de algum arquivo: \n");
            System.out.println("1 - Enviar um aquivo");
            System.out.println("0 - Sair");

            opt = in.nextInt();

            if(opt == 0){
                System.out.println("Tchau!");
                repeat = false;
            }
            if(opt == 1){
                int portaOrigem;
                int portaDestino;
                String ipDestino;
                String nomeArquivo;

                System.out.println(rede.hostsToString());
                System.out.println("Informe a porta da maquina que deve enviar o arquivo: ");
                portaOrigem = in.nextInt();

                System.out.println("Informe o IP destino: ");
                ipDestino = in.next();

                System.out.println("Informe a porta do destino: ");
                portaDestino = in.nextInt();

                System.out.println("Informe o nome do arquivo a ser enviado: ");
                nomeArquivo = in.next();

                rede.enviarArquivo(portaOrigem, ipDestino, portaDestino, nomeArquivo);
            }
        }while (repeat);
    }
}
