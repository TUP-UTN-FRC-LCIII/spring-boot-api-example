package ar.edu.utn.frc.tup.lciii;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.lciii.controllers.PingController;
import ar.edu.utn.frc.tup.lciii.services.MatchService;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import ar.edu.utn.frc.tup.lciii.services.impl.MatchServiceImpl;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frc.tup.lciii.controllers.PlayerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmokeTest {

    @Autowired
    private PlayerController playerController;

    @Autowired
    private PingController pingController;

    @Autowired
    private PlayerService playerService;

    @Bean
    private MatchService matchService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(playerController).isNotNull();
        assertThat(pingController).isNotNull();
        assertThat(playerService).isNotNull();
        assertThat(matchService).isNotNull();
    }
}
