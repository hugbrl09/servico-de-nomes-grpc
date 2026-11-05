package com.mycompany.atendimentorpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class ClienteNomes {

    // O único endereço fixo que o totem conhece: o do Serviço de Nomes
    private static final String HOST_NOMES = "localhost";
    private static final int PORTA_NOMES = 50052;

    // Pergunta ao Serviço de Nomes onde está o serviço com esse nome.
    // Nome não registrado: a chamada falha com o código NOT_FOUND.
    public static Endereco localizar(String nome) {
        ManagedChannel canal = ManagedChannelBuilder
                .forAddress(HOST_NOMES, PORTA_NOMES)
                .usePlaintext()   // sem TLS: só no laboratório
                .build();
        try {
            ServicoNomesGrpc.ServicoNomesBlockingStub stub =
                    ServicoNomesGrpc.newBlockingStub(canal);
            return stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .localizar(Nome.newBuilder().setNome(nome).build());
        } finally {
            canal.shutdown();   // a consulta acabou: fecha a conexão
        }
    }
}