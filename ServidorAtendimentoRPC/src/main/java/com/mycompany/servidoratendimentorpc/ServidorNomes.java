package com.mycompany.servidoratendimentorpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServidorNomes {

    // Roda separado da Central: botão direito no arquivo > Run File (Shift+F6)
    public static void main(String[] args) throws Exception {
        Server servidor = ServerBuilder.forPort(50052)
                .addService(new ServicoNomesImpl())
                .build()
                .start();
        System.out.println("Serviço de Nomes no ar (porta 50052).");
        servidor.awaitTermination();
    }
}