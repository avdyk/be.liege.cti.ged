package be.liege.cti.ged.api.impl;

import be.liege.cti.ged.api.AlfredConfiguration;
import be.liege.cti.ged.api.AlfredServiceFactory;
import be.liege.cti.ged.api.AlfredService;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlfredServiceBuilderTest {

    @Autowired
    private AlfredServiceFactory builder;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Before
    public void before() {
        reset(restTemplateBuilder);
        final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        restTemplate.getInterceptors();
        when(restTemplateBuilder.build())
                .thenReturn(restTemplate);
    }

    @After
    public void after() {
        Mockito.verify(restTemplateBuilder);
    }

    @Test
    public void buildAlfredService() {
        final AlfredService alfredService = builder.createAlfredServiceBuilder()
                .url("http://localhost/foo")
                .username("username")
                .password("password");
        Assertions.assertThat(alfredService.getUrl()).isEqualTo("http://localhost/foo");
    }

    @SpringBootApplication
    @Import(AlfredConfiguration.class)
    static class TestServiceBuilder {
        @Bean
        @Profile("dev")
        public RestTemplateBuilder restTemplateBuilder() {
            return Mockito.mock(RestTemplateBuilder.class);
        }

    }
}
