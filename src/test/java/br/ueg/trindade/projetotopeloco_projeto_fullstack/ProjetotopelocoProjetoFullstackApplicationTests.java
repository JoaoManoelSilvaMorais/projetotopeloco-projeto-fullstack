package br.ueg.trindade.projetotopeloco_projeto_fullstack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@SpringBootTest
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
class ProjetotopelocoProjetoFullstackApplicationTests {

	@Test
	void contextLoads() {
	}

}
