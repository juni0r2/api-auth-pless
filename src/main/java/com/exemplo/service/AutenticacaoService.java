package com.exemplo.service;

import com.exemplo.dto.AutenticacaoRequest;
import com.exemplo.dto.AutenticacaoResponse;
import com.exemplo.dto.IdentificadorRequest;
import com.exemplo.dto.IdentificadorResponse;
import com.exemplo.dto.OAuthTokenRequest;
import com.exemplo.dto.OAuthTokenResponse;
import com.exemplo.dto.ValidacaoResponse;
import com.exemplo.dto.ValidacaoTokenSessionRequest;
import com.exemplo.dto.ValidacaoTokenSessionResponse;
import com.exemplo.exception.TokenInvalidoException;
import com.exemplo.model.AutenticacaoTransacao;
import com.exemplo.model.Registro;
import com.exemplo.repository.AutenticacaoTransacaoRepository;
import com.exemplo.repository.RegistroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {
    
    private final AutenticacaoTransacaoRepository autenticacaoTransacaoRepository;
    private final RegistroRepository registroRepository;
    private final JwtService jwtService;
    private final OAuthService oauthService;
    
    private static final SecureRandom random = new SecureRandom();
    
    public AutenticacaoResponse gerarToken(AutenticacaoRequest request) {
        // Gerar identificador único
        String identificador = gerarIdentificador(request.getCpf(), request.getVertical(), request.getJornada());
        
        // Verificar se já existe uma transação ativa para este identificador
        Optional<AutenticacaoTransacao> transacaoExistente = 
            autenticacaoTransacaoRepository.findByIdentificadorAndUtilizadoFalse(identificador);
        
        if (transacaoExistente.isPresent()) {
            // Se já existe uma transação ativa, retornar o token existente
            AutenticacaoTransacao transacao = transacaoExistente.get();
            return new AutenticacaoResponse(identificador, transacao.getToken());
        }
        
        // Gerar novo token de 6 dígitos
        String token = gerarTokenNumerico();
        
        // Criar nova transação
        AutenticacaoTransacao transacao = new AutenticacaoTransacao(
            identificador,
            token,
            request.getVertical(),
            request.getJornada(),
            request.getCpf(),
            request.getTipoContato()
        );
        
        // Salvar transação
        autenticacaoTransacaoRepository.save(transacao);
        
        return new AutenticacaoResponse(identificador, token);
    }
    
    public ValidacaoResponse validarToken(String identificador, String token) {
        Optional<AutenticacaoTransacao> transacao = 
            autenticacaoTransacaoRepository.findByIdentificadorAndTokenAndUtilizadoFalse(identificador, token);
        
        if (transacao.isPresent()) {
            // Marcar token como utilizado
            AutenticacaoTransacao transacaoEntity = transacao.get();
            transacaoEntity.setUtilizado(true);
            transacaoEntity.setDataUtilizacao(LocalDateTime.now());
            autenticacaoTransacaoRepository.save(transacaoEntity);
            
            // Gerar JWT com informações da sessão
            String tokenSessionId = jwtService.gerarTokenSession(
                transacaoEntity.getCpf(),
                transacaoEntity.getVertical(),
                transacaoEntity.getJornada(),
                LocalDateTime.now()
            );
            
            return new ValidacaoResponse("Token válido", tokenSessionId);
        }
        
        // Lançar exceção se token for inválido ou não existir
        throw new TokenInvalidoException("Token inválido");
    }
    
    private String gerarIdentificador(String cpf, String vertical, String jornada) {
        // Seguir a mesma lógica da classe Registro
        // Substituir espaços por hifens e converter para lowercase
        String verticalFormatada = vertical != null ? vertical.replace(" ", "-").toLowerCase() : "";
        String jornadaFormatada = jornada != null ? jornada.replace(" ", "-").toLowerCase() : "";
        String cpfFormatado = cpf != null ? cpf : "";
        
        // Concatenar na ordem especificada: vertical|jornada|cpf
        return verticalFormatada + "|" + jornadaFormatada + "|" + cpfFormatado;
    }
    
    private String gerarTokenNumerico() {
        // Gerar token de 6 dígitos numéricos
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }
    
    public ValidacaoTokenSessionResponse validarTokenSession(ValidacaoTokenSessionRequest request) {
        try {
            String token = request.getTokenSessionId();
            
            if (!jwtService.isTokenValido(token)) {
                return new ValidacaoTokenSessionResponse(false, "Token de sessão inválido");
            }
            
            String cpf = jwtService.extrairCpf(token);
            String vertical = jwtService.extrairVertical(token);
            String jornada = jwtService.extrairJornada(token);
            LocalDateTime dataValidacao = jwtService.extrairDataValidacao(token);
            
            return new ValidacaoTokenSessionResponse(
                true, cpf, vertical, jornada, dataValidacao, "Token de sessão válido"
            );
            
        } catch (Exception e) {
            return new ValidacaoTokenSessionResponse(false, "Token de sessão inválido");
        }
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
        return registroRepository.findByIdentificador(registroTemp.getIdentificador())
                .map(registro -> new IdentificadorResponse(registro.getEmail(), registro.getTelefone()));
    }
    
    /**
     * 🔐 Gera token OAuth 2.0 para autenticação
     * 
     * @param request Dados da requisição de autenticação
     * @return Resposta com token OAuth
     */
    public OAuthTokenResponse gerarTokenOAuth(AutenticacaoRequest request) {
        // Criar requisição OAuth
        OAuthTokenRequest oauthRequest = new OAuthTokenRequest();
        oauthRequest.setGrantType("password");
        oauthRequest.setClientId("api-auth-pless-client");
        oauthRequest.setClientSecret("secret");
        oauthRequest.setUsername(request.getCpf());
        oauthRequest.setPassword("password"); // Senha temporária
        oauthRequest.setScope("read write");
        
        // Gerar token via OAuth Service
        return oauthService.generateToken(oauthRequest);
    }
    
    /**
     * ✅ Valida token OAuth 2.0
     * 
     * @param token Token a ser validado
     * @return true se válido
     */
    public boolean validarTokenOAuth(String token) {
        return oauthService.validateToken(token);
    }
    
    /**
     * 🔄 Renova token OAuth 2.0
     * 
     * @param refreshToken Token de renovação
     * @return Novo token
     */
    public OAuthTokenResponse renovarTokenOAuth(String refreshToken) {
        return oauthService.refreshToken(refreshToken);
    }
}
