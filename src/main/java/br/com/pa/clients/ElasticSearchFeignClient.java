package br.com.pa.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "elasticSearchFeignClient", url = "http://localhost:9200")
public interface ElasticSearchFeignClient {

    @GetMapping("")
    void searchTerm(String term);

    @PostMapping("")
    void salvarTextConsulta(String texto);

}
