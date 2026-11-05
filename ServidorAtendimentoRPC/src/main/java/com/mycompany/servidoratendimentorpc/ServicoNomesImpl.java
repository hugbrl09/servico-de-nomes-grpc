package com.mycompany.servidoratendimentorpc;

import com.mycompany.atendimentorpc.Endereco;
import com.mycompany.atendimentorpc.Nome;
import com.mycompany.atendimentorpc.Registro;
import com.mycompany.atendimentorpc.ServicoNomesGrpc;
import com.mycompany.atendimentorpc.Vazio;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServicoNomesImpl
        extends ServicoNomesGrpc.ServicoNomesImplBase {

    // O registro: nome -> endereço, compartilhado por todas as chamadas
    // ConcurrentHashMap: várias chamadas podem mexer no mapa ao mesmo tempo
    private final Map<String, Endereco> registro = new ConcurrentHashMap<>();

    @Override
    public void registrar(Registro req, StreamObserver<Vazio> observador) {
        Endereco endereco = Endereco.newBuilder()
                .setHost(req.getHost())
                .setPorta(req.getPorta())
                .build();
        registro.put(req.getNome(), endereco);
        System.out.println("Registrado: " + req.getNome() + " -> "
                + req.getHost() + ":" + req.getPorta());
        observador.onNext(Vazio.getDefaultInstance());
        observador.onCompleted();
    }

    @Override
    public void localizar(Nome req, StreamObserver<Endereco> observador) {
        Endereco endereco = registro.get(req.getNome());
        if (endereco == null) {
            // Ninguém registrou esse nome: a chamada termina com NOT_FOUND
            System.out.println("Localizar " + req.getNome() + ": NOT_FOUND");
            observador.onError(Status.NOT_FOUND
                    .withDescription("Serviço não registrado: " + req.getNome())
                    .asRuntimeException());
            return;
        }
        System.out.println("Localizado: " + req.getNome() + " -> "
                + endereco.getHost() + ":" + endereco.getPorta());
        observador.onNext(endereco);  // devolve host e porta ao totem
        observador.onCompleted();
    }

    @Override
    public void remover(Nome req, StreamObserver<Vazio> observador) {
        registro.remove(req.getNome());
        System.out.println("Removido: " + req.getNome());
        observador.onNext(Vazio.getDefaultInstance());
        observador.onCompleted();
    }
}