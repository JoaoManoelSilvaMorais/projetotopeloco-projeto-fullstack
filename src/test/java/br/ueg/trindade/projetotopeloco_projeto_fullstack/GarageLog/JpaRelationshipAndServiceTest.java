package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum.CategoriaVeiculo;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum.TipoDespesa;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum.TipoServico;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.DespesaRecorrenteModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.ServicoManutencaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.DespesaRecorrenteRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.ServicoManutencaoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Security.JwtUtil;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.DespesaRecorrenteService;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.ServicoManutencaoService;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.VeiculoService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
public class JpaRelationshipAndServiceTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private ServicoManutencaoService servicoService;

    @Autowired
    private DespesaRecorrenteService despesaService;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ServicoManutencaoRepository servicoRepository;

    @Autowired
    private DespesaRecorrenteRepository despesaRepository;

    private MockMvc mockMvc;
    private String userToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        userToken = jwtUtil.generateToken("user", List.of("USER"));
    }

    // =========================================================================
    // 1. JSON Serialization & Circular Reference Tests
    // =========================================================================

    @Test
    @DisplayName("Serialização de VeiculoModel com Servico e Despesa anexados não gera recursão e omite veiculo nos filhos")
    void testVeiculoModelSerializationCleanWithoutRecursion() throws Exception {
        VeiculoModel veiculo = new VeiculoModel("ABC1D23", "Toyota", "Corolla",
                CategoriaVeiculo.CONVENCIONAL, "2.0 Flex", 2022);
        veiculo.setId(UUID.randomUUID());

        ServicoManutencaoModel servico = new ServicoManutencaoModel(
                TipoServico.MECANICA_PREVENTIVA, "Troca de óleo e filtros",
                LocalDate.of(2026, 5, 10), 45000, 55000,
                new BigDecimal("250.00"), new BigDecimal("120.00"), veiculo);
        servico.setId(UUID.randomUUID());

        DespesaRecorrenteModel despesa = new DespesaRecorrenteModel();
        despesa.setId(UUID.randomUUID());
        despesa.setTipo(TipoDespesa.PROTECAO_VEICULAR);
        despesa.setValorMensal(new BigDecimal("320.00"));
        despesa.setDiaVencimento(15);
        despesa.setStatusAtivo(true);
        despesa.setVeiculo(veiculo);

        veiculo.getHistoricoServicos().add(servico);
        veiculo.getDespesas().add(despesa);

        String json = assertDoesNotThrow(() -> objectMapper.writeValueAsString(veiculo));
        assertNotNull(json);

        JsonNode root = objectMapper.readTree(json);
        assertEquals("ABC1D23", root.get("placa").asText());
        assertEquals("Toyota", root.get("marca").asText());
        assertEquals("Corolla", root.get("modeloBase").asText());

        // historicoServicos is present
        JsonNode historico = root.get("historicoServicos");
        assertNotNull(historico);
        assertTrue(historico.isArray());
        assertEquals(1, historico.size());
        JsonNode servicoItem = historico.get(0);
        assertEquals("Troca de óleo e filtros", servicoItem.get("descricao").asText());
        // veiculo inside servico must be ignored to prevent circular reference
        assertNull(servicoItem.get("veiculo"));

        // despesas is present
        JsonNode despesas = root.get("despesas");
        assertNotNull(despesas);
        assertTrue(despesas.isArray());
        assertEquals(1, despesas.size());
        JsonNode despesaItem = despesas.get(0);
        assertEquals("PROTECAO_VEICULAR", despesaItem.get("tipo").asText());
        // veiculo inside despesa must be ignored to prevent circular reference
        assertNull(despesaItem.get("veiculo"));
    }

    @Test
    @DisplayName("Serialização de ServicoManutencaoModel inclui veiculo sem recursão infinita")
    void testServicoManutencaoSerializationIncludesVeiculoCleanly() throws Exception {
        VeiculoModel veiculo = new VeiculoModel("XYZ9K88", "Honda", "Civic",
                CategoriaVeiculo.MODIFICADO, "1.5 Turbo", 2021);
        veiculo.setId(UUID.randomUUID());

        ServicoManutencaoModel servico = new ServicoManutencaoModel(
                TipoServico.UPGRADE_PERFORMANCE, "Remap de ECU e escape",
                LocalDate.of(2026, 6, 1), 30000, 40000,
                new BigDecimal("1500.00"), new BigDecimal("800.00"), veiculo);
        servico.setId(UUID.randomUUID());

        veiculo.getHistoricoServicos().add(servico);

        String json = assertDoesNotThrow(() -> objectMapper.writeValueAsString(servico));
        assertNotNull(json);

        JsonNode root = objectMapper.readTree(json);
        assertEquals("Remap de ECU e escape", root.get("descricao").asText());

        // veiculo details must be serialized
        JsonNode veiculoNode = root.get("veiculo");
        assertNotNull(veiculoNode);
        assertFalse(veiculoNode.isNull());
        assertEquals("XYZ9K88", veiculoNode.get("placa").asText());
        assertEquals("Honda", veiculoNode.get("marca").asText());
        assertEquals("Civic", veiculoNode.get("modeloBase").asText());

        // veiculo's nested collections must be ignored to prevent recursion
        assertNull(veiculoNode.get("historicoServicos"));
        assertNull(veiculoNode.get("despesas"));
    }

    @Test
    @DisplayName("Serialização de DespesaRecorrenteModel inclui veiculo sem recursão infinita")
    void testDespesaRecorrenteSerializationIncludesVeiculoCleanly() throws Exception {
        VeiculoModel veiculo = new VeiculoModel("DEF4G56", "Chevrolet", "Cruze",
                CategoriaVeiculo.CONVENCIONAL, "1.4 Turbo", 2020);
        veiculo.setId(UUID.randomUUID());

        DespesaRecorrenteModel despesa = new DespesaRecorrenteModel();
        despesa.setId(UUID.randomUUID());
        despesa.setTipo(TipoDespesa.ASSISTENCIA_24H);
        despesa.setValorMensal(new BigDecimal("85.50"));
        despesa.setDiaVencimento(5);
        despesa.setStatusAtivo(true);
        despesa.setVeiculo(veiculo);

        veiculo.getDespesas().add(despesa);

        String json = assertDoesNotThrow(() -> objectMapper.writeValueAsString(despesa));
        assertNotNull(json);

        JsonNode root = objectMapper.readTree(json);
        assertEquals("ASSISTENCIA_24H", root.get("tipo").asText());

        // veiculo details must be serialized
        JsonNode veiculoNode = root.get("veiculo");
        assertNotNull(veiculoNode);
        assertFalse(veiculoNode.isNull());
        assertEquals("DEF4G56", veiculoNode.get("placa").asText());
        assertEquals("Chevrolet", veiculoNode.get("marca").asText());
        assertEquals("Cruze", veiculoNode.get("modeloBase").asText());

        // veiculo's nested collections must be ignored to prevent recursion
        assertNull(veiculoNode.get("historicoServicos"));
        assertNull(veiculoNode.get("despesas"));
    }

    // =========================================================================
    // 2. VeiculoService Validation & Business Logic Tests
    // =========================================================================

    @Test
    @DisplayName("VeiculoService.criar valida campos obrigatórios e não-nulos")
    void testVeiculoServiceValidationsMandatoryFields() {
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(null));

        // Placa nula ou em branco
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel(null, "Marca", "Modelo", CategoriaVeiculo.CONVENCIONAL, "1.0", 2020)));
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel("   ", "Marca", "Modelo", CategoriaVeiculo.CONVENCIONAL, "1.0", 2020)));

        // Marca nula ou em branco
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel("AAA1111", null, "Modelo", CategoriaVeiculo.CONVENCIONAL, "1.0", 2020)));
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel("AAA1111", "  ", "Modelo", CategoriaVeiculo.CONVENCIONAL, "1.0", 2020)));

        // Modelo nulo ou em branco
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel("AAA1111", "Marca", null, CategoriaVeiculo.CONVENCIONAL, "1.0", 2020)));

        // Categoria nula
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel("AAA1111", "Marca", "Modelo", null, "1.0", 2020)));

        // Ano de fabricação nulo ou inválido
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel("AAA1111", "Marca", "Modelo", CategoriaVeiculo.CONVENCIONAL, "1.0", null)));
        assertThrows(IllegalArgumentException.class, () -> veiculoService.criar(
                new VeiculoModel("AAA1111", "Marca", "Modelo", CategoriaVeiculo.CONVENCIONAL, "1.0", -5)));
    }

    @Test
    @DisplayName("VeiculoService.criar rejeita placa duplicada")
    void testVeiculoServiceRejectsDuplicatePlaca() {
        String placaUnica = "DUP" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel v1 = new VeiculoModel(placaUnica, "Marca", "Modelo",
                CategoriaVeiculo.CONVENCIONAL, "1.0", 2022);
        veiculoService.criar(v1);

        VeiculoModel v2 = new VeiculoModel(placaUnica, "OutraMarca", "OutroModelo",
                CategoriaVeiculo.CONVENCIONAL, "1.4", 2023);
        assertThrows(RuntimeException.class, () -> veiculoService.criar(v2));
    }

    @Test
    @DisplayName("VeiculoService.calcularGastos valida período de datas")
    void testVeiculoServiceCalcularGastosValidation() {
        String placa = "GAS" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "Fiat", "Pulse", CategoriaVeiculo.CONVENCIONAL, "1.0 Turbo", 2023));

        // Data de início posterior à data de fim deve lançar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () ->
                veiculoService.calcularGastos(veiculo.getId(), LocalDate.of(2026, 12, 31), LocalDate.of(2026, 1, 1)));

        // Período válido deve calcular sem erros
        BigDecimal gastos = veiculoService.calcularGastos(veiculo.getId(),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        assertNotNull(gastos);
    }

    // =========================================================================
    // 3. ServicoManutencaoService Validation & Business Logic Tests
    // =========================================================================

    @Test
    @DisplayName("ServicoManutencaoService valida campos obrigatórios e veículo ativo")
    void testServicoManutencaoServiceMandatoryValidations() {
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(null, UUID.randomUUID()));

        // Veículo nulo
        ServicoManutencaoModel servicoValido = new ServicoManutencaoModel(
                TipoServico.MECANICA_PREVENTIVA, "Alinhamento",
                LocalDate.now(), 10000, 20000, BigDecimal.TEN, BigDecimal.TEN, null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(servicoValido, null));

        // Veículo inexistente
        assertThrows(RuntimeException.class, () -> servicoService.criar(servicoValido, UUID.randomUUID()));

        // Criar veículo para testar campos do serviço
        String placa = "SRV" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "VW", "Golf", CategoriaVeiculo.MODIFICADO, "2.0 TSI", 2019));

        // Tipo nulo
        ServicoManutencaoModel servicoTipoNull = new ServicoManutencaoModel(
                null, "Desc", LocalDate.now(), 10000, 20000, BigDecimal.ZERO, BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(servicoTipoNull, veiculo.getId()));

        // Descrição nula ou vazia
        ServicoManutencaoModel servicoDescVazia = new ServicoManutencaoModel(
                TipoServico.MECANICA_CORRETIVA, "  ", LocalDate.now(), 10000, 20000, BigDecimal.ZERO, BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(servicoDescVazia, veiculo.getId()));

        // Data realização nula
        ServicoManutencaoModel servicoDataNull = new ServicoManutencaoModel(
                TipoServico.MECANICA_CORRETIVA, "Reparo", null, 10000, 20000, BigDecimal.ZERO, BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(servicoDataNull, veiculo.getId()));
    }

    @Test
    @DisplayName("ServicoManutencaoService valida valores não-negativos")
    void testServicoManutencaoServiceNonNegativeValidations() {
        String placa = "NEG" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "Ford", "Focus", CategoriaVeiculo.CONVENCIONAL, "2.0", 2018));

        // KM atual negativo
        ServicoManutencaoModel kmNegativo = new ServicoManutencaoModel(
                TipoServico.MECANICA_CORRETIVA, "Reparo", LocalDate.now(), -100, 20000, BigDecimal.ZERO, BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(kmNegativo, veiculo.getId()));

        // Próxima revisão KM negativa
        ServicoManutencaoModel proxKmNegativo = new ServicoManutencaoModel(
                TipoServico.MECANICA_CORRETIVA, "Reparo", LocalDate.now(), 1000, -200, BigDecimal.ZERO, BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(proxKmNegativo, veiculo.getId()));

        // Custo peças negativo
        ServicoManutencaoModel custoPecasNegativo = new ServicoManutencaoModel(
                TipoServico.MECANICA_CORRETIVA, "Reparo", LocalDate.now(), 1000, 2000, new BigDecimal("-50.00"), BigDecimal.ZERO, null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(custoPecasNegativo, veiculo.getId()));

        // Custo mão de obra negativo
        ServicoManutencaoModel custoMaoNegativo = new ServicoManutencaoModel(
                TipoServico.MECANICA_CORRETIVA, "Reparo", LocalDate.now(), 1000, 2000, BigDecimal.ZERO, new BigDecimal("-10.00"), null);
        assertThrows(IllegalArgumentException.class, () -> servicoService.criar(custoMaoNegativo, veiculo.getId()));
    }

    @Test
    @DisplayName("ServicoManutencaoService executa regras de negócio de custo total e alerta de revisão")
    void testServicoManutencaoBusinessCalculations() {
        String placa = "CAL" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "Renault", "Sandero", CategoriaVeiculo.CONVENCIONAL, "1.6", 2017));

        ServicoManutencaoModel servico = new ServicoManutencaoModel(
                TipoServico.MECANICA_PREVENTIVA, "Revisão dos 50k", LocalDate.now(),
                50000, 60000, new BigDecimal("350.00"), new BigDecimal("200.00"), null);
        ServicoManutencaoModel salvo = servicoService.criar(servico, veiculo.getId());

        // Custo total deve ser 350 + 200 = 550
        BigDecimal custoTotal = servicoService.calcularCustoTotal(salvo.getId());
        assertEquals(0, new BigDecimal("550.00").compareTo(custoTotal));

        // Alerta de revisão para próxima revisão em 60000
        assertFalse(servicoService.verificarAlertaRevisao(salvo.getId(), 58000));
        assertTrue(servicoService.verificarAlertaRevisao(salvo.getId(), 60000));
        assertTrue(servicoService.verificarAlertaRevisao(salvo.getId(), 61000));
    }

    // =========================================================================
    // 4. DespesaRecorrenteService Validation & Business Logic Tests
    // =========================================================================

    @Test
    @DisplayName("DespesaRecorrenteService valida campos obrigatórios e veículo ativo")
    void testDespesaRecorrenteServiceMandatoryValidations() {
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(null, UUID.randomUUID()));

        // Veículo nulo
        DespesaRecorrenteModel despesaValida = new DespesaRecorrenteModel();
        despesaValida.setTipo(TipoDespesa.PROTECAO_VEICULAR);
        despesaValida.setValorMensal(new BigDecimal("200.00"));
        despesaValida.setDiaVencimento(10);
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(despesaValida, null));

        // Veículo inexistente
        assertThrows(RuntimeException.class, () -> despesaService.criar(despesaValida, UUID.randomUUID()));

        String placa = "DSP" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "Nissan", "Kicks", CategoriaVeiculo.CONVENCIONAL, "1.6", 2021));

        // Tipo nulo
        DespesaRecorrenteModel semTipo = new DespesaRecorrenteModel();
        semTipo.setValorMensal(new BigDecimal("100.00"));
        semTipo.setDiaVencimento(10);
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(semTipo, veiculo.getId()));

        // Valor nulo
        DespesaRecorrenteModel semValor = new DespesaRecorrenteModel();
        semValor.setTipo(TipoDespesa.IMPOSTO);
        semValor.setDiaVencimento(10);
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(semValor, veiculo.getId()));

        // Dia vencimento nulo
        DespesaRecorrenteModel semDia = new DespesaRecorrenteModel();
        semDia.setTipo(TipoDespesa.IMPOSTO);
        semDia.setValorMensal(new BigDecimal("100.00"));
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(semDia, veiculo.getId()));
    }

    @Test
    @DisplayName("DespesaRecorrenteService valida valor mensal não-negativo e dia de vencimento entre 1 e 31")
    void testDespesaRecorrenteServiceValueAndDayValidations() {
        String placa = "VAL" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "Hyundai", "HB20", CategoriaVeiculo.CONVENCIONAL, "1.0", 2022));

        // Valor mensal negativo
        DespesaRecorrenteModel valorNegativo = new DespesaRecorrenteModel();
        valorNegativo.setTipo(TipoDespesa.PROTECAO_VEICULAR);
        valorNegativo.setValorMensal(new BigDecimal("-10.00"));
        valorNegativo.setDiaVencimento(15);
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(valorNegativo, veiculo.getId()));

        // Dia vencimento menor que 1
        DespesaRecorrenteModel diaZero = new DespesaRecorrenteModel();
        diaZero.setTipo(TipoDespesa.PROTECAO_VEICULAR);
        diaZero.setValorMensal(new BigDecimal("150.00"));
        diaZero.setDiaVencimento(0);
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(diaZero, veiculo.getId()));

        // Dia vencimento maior que 31
        DespesaRecorrenteModel dia32 = new DespesaRecorrenteModel();
        dia32.setTipo(TipoDespesa.PROTECAO_VEICULAR);
        dia32.setValorMensal(new BigDecimal("150.00"));
        dia32.setDiaVencimento(32);
        assertThrows(IllegalArgumentException.class, () -> despesaService.criar(dia32, veiculo.getId()));

        // Dias limites válidos (1 e 31)
        DespesaRecorrenteModel dia1 = new DespesaRecorrenteModel();
        dia1.setTipo(TipoDespesa.IMPOSTO);
        dia1.setValorMensal(new BigDecimal("150.00"));
        dia1.setDiaVencimento(1);
        DespesaRecorrenteModel salva1 = despesaService.criar(dia1, veiculo.getId());
        assertNotNull(salva1.getId());
        assertTrue(salva1.getStatusAtivo()); // default ativo = true

        DespesaRecorrenteModel dia31 = new DespesaRecorrenteModel();
        dia31.setTipo(TipoDespesa.IMPOSTO);
        dia31.setValorMensal(new BigDecimal("150.00"));
        dia31.setDiaVencimento(31);
        DespesaRecorrenteModel salva31 = despesaService.criar(dia31, veiculo.getId());
        assertNotNull(salva31.getId());
    }

    // =========================================================================
    // 5. REST Endpoints Serialization Verification via MockMvc
    // =========================================================================

    @Test
    @DisplayName("Endpoint GET /api/servicos-manutencao/{id} serializa dados do veiculo sem recursão")
    void testEndpointServicoManutencaoSerializesVeiculoDetails() throws Exception {
        String placa = "MNT" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "Jeep", "Renegade", CategoriaVeiculo.CONVENCIONAL, "1.3 T270", 2023));

        ServicoManutencaoModel servico = new ServicoManutencaoModel(
                TipoServico.MECANICA_PREVENTIVA, "Troca de pastilhas",
                LocalDate.now(), 25000, 35000, new BigDecimal("180.00"), new BigDecimal("90.00"), null);
        ServicoManutencaoModel salvo = servicoService.criar(servico, veiculo.getId());

        mockMvc.perform(get("/api/servicos-manutencao/" + salvo.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId().toString()))
                .andExpect(jsonPath("$.descricao").value("Troca de pastilhas"))
                .andExpect(jsonPath("$.veiculo").exists())
                .andExpect(jsonPath("$.veiculo.id").value(veiculo.getId().toString()))
                .andExpect(jsonPath("$.veiculo.placa").value(placa))
                .andExpect(jsonPath("$.veiculo.marca").value("Jeep"))
                .andExpect(jsonPath("$.veiculo.modeloBase").value("Renegade"))
                .andExpect(jsonPath("$.veiculo.historicoServicos").doesNotExist())
                .andExpect(jsonPath("$.veiculo.despesas").doesNotExist());
    }

    @Test
    @DisplayName("Endpoint GET /api/despesas-recorrentes/{id} serializa dados do veiculo sem recursão")
    void testEndpointDespesaRecorrenteSerializesVeiculoDetails() throws Exception {
        String placa = "DSP" + (int)(Math.random() * 9000 + 1000);
        VeiculoModel veiculo = veiculoService.criar(
                new VeiculoModel(placa, "Peugeot", "208", CategoriaVeiculo.CONVENCIONAL, "1.0", 2024));

        DespesaRecorrenteModel despesa = new DespesaRecorrenteModel();
        despesa.setTipo(TipoDespesa.PROTECAO_VEICULAR);
        despesa.setValorMensal(new BigDecimal("199.90"));
        despesa.setDiaVencimento(20);
        despesa.setStatusAtivo(true);
        DespesaRecorrenteModel salva = despesaService.criar(despesa, veiculo.getId());

        mockMvc.perform(get("/api/despesas-recorrentes/" + salva.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salva.getId().toString()))
                .andExpect(jsonPath("$.tipo").value("PROTECAO_VEICULAR"))
                .andExpect(jsonPath("$.veiculo").exists())
                .andExpect(jsonPath("$.veiculo.id").value(veiculo.getId().toString()))
                .andExpect(jsonPath("$.veiculo.placa").value(placa))
                .andExpect(jsonPath("$.veiculo.marca").value("Peugeot"))
                .andExpect(jsonPath("$.veiculo.modeloBase").value("208"))
                .andExpect(jsonPath("$.veiculo.historicoServicos").doesNotExist())
                .andExpect(jsonPath("$.veiculo.despesas").doesNotExist());
    }
}
