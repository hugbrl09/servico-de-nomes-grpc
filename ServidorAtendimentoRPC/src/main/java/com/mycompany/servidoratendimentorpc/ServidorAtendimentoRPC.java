package com.mycompany.servidoratendimentorpc;

import com.mycompany.atendimentorpc.Nome;
import com.mycompany.atendimentorpc.Registro;
import com.mycompany.atendimentorpc.ServicoNomesGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.Scanner;

public class ServidorAtendimentoRPC {

    public static void main(String[] args) throws Exception {
        Server servidor = ServerBuilder.forPort(50051)
                .addService(new AtendimentoServiceImpl())
                .build()
                .start();
        System.out.println("Central de Senhas no ar (porta 50051).");
        
        // Ao subir: a Central se registra no Serviço de Nomes como "atendimento"
        ManagedChannel canalNomes = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()   // sem TLS: só no laboratório
                .build();
        ServicoNomesGrpc.ServicoNomesBlockingStub nomes =
                ServicoNomesGrpc.newBlockingStub(canalNomes);
        nomes.registrar(Registro.newBuilder()
                .setNome("atendimento")
                .setHost("localhost")
                .setPorta(50051)
                .build());
        System.out.println("Registrada como \"atendimento\" no Serviço de Nomes.");

        // Ao encerrar: sai do registro, para nenhum totem procurar uma Central desligada
        System.out.println("Pressione ENTER para encerrar a Central.");
        new Scanner(System.in).nextLine();
        nomes.remover(Nome.newBuilder().setNome("atendimento").build());
        System.out.println("Removida do Serviço de Nomes. Encerrando...");
        canalNomes.shutdown();
        servidor.shutdown();
        servidor.awaitTermination();
    }
}
