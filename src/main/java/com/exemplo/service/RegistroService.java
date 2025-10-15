package com.exemplo.service;

import com.exemplo.dto.IdentificadorRequest;
import com.exemplo.dto.IdentificadorResponse;
import com.exemplo.model.Registro;
import com.exemplo.repository.RegistroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    public List<Registro> listarTodos() {
        return registroRepository.findAll();
    }

    public Optional<Registro> buscarPorId(Long id) {
        return registroRepository.findById(id);
    }
    
    public Optional<Registro> buscarPorIdentificador(String identificador) {
        return registroRepository.findByIdentificador(identificador);
    }

    public Registro salvar(Registro registro) {
        return registroRepository.save(registro);
    }

    public void excluir(Long id) {
        registroRepository.deleteById(id);
    }
    
    /**
     * Busca um registro por identificador gerado a partir dos dados fornecidos
     * @param request Dados para gerar o identificador (CPF, vertical, jornada)
     * @return Optional contendo a resposta com email e telefone mascarados, ou empty se não encontrado
     */
    public Optional<IdentificadorResponse> buscarPorIdentificadorComMascaramento(IdentificadorRequest request) {
        // Criar um objeto Registro temporário usando o construtor com os três parâmetros
        Registro registroTemp = new Registro(request.getCpf(), request.getVertical(), request.getJornada());
        
        // Gerar o identificador usando o método da entidade
        registroTemp.gerarIdentificador();
        
        // Buscar no banco de dados pelo identificador gerado
        return buscarPorIdentificador(registroTemp.getIdentificador())
                .map(registro -> new IdentificadorResponse(registro.getEmail(), registro.getTelefone()));
    }
}