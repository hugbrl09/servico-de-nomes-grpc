package com.mycompany.atendimentorpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;

// Testes do Serviço de Nomes: botão direito no arquivo > Run File (Shift+F6)
public class TesteNomes {

    public static void main(String[] args) {
        // Um nome registrado e um que ninguém registrou
        localizar("atendimento");
        localizar("financeiro");

        // forTarget: o próprio gRPC resolve o nome DNS "localhost" em IP
        ManagedChannel canal = ManagedChannelBuilder
                .forTarget("dns:///localhost:50051")
                .usePlaintext()
                .build();
        try {
            Senha senha = AtendimentoServiceGrpc.newBlockingStub(canal)
                    .withDeadlineAfter(5, TimeUnit.SECONDS)
                    .pegarSenha(PedidoSenha.newBuilder()
                            .setNome("Teste DNS").setLocal("TesteNomes").build());
            System.out.println("forTarget dns:///localhost:50051 -> senha "
                    + senha.getCodigo());
        } catch (StatusRuntimeException e) {
            System.out.println("forTarget dns:///localhost:50051 -> "
                    + e.getStatus().getCode());
        } finally {
            canal.shutdown();
        }
    }

    private static void localizar(String nome) {
        try {
            Endereco endereco = ClienteNomes.localizar(nome);
            System.out.println("Localizar \"" + nome + "\" -> "
                    + endereco.getHost() + ":" + endereco.getPorta());
        } catch (StatusRuntimeException e) {
            System.out.println("Localizar \"" + nome + "\" -> "
                    + e.getStatus().getCode());
        }
    }
}