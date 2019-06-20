package com.springer.hack.exambuddy.external.dbpediaspotlight;


import com.springer.hack.exambuddy.external.BaseRestService;
import com.springer.hack.exambuddy.sementity.SemEntity;
import com.springer.hack.exambuddy.sementity.SemEntityRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(DBPediaSpotlightProperties.class)
public class DBPediaSpotlightService extends BaseRestService {

    private RestTemplate restTemplate = new RestTemplate();

    private static Logger log = LoggerFactory.getLogger(DBPediaSpotlightService.class);

    private final SemEntityRepository semEntityRepository;

    private final DBPediaSpotlightProperties properties;

    @Autowired
    public DBPediaSpotlightService(SemEntityRepository semEntityRepository, DBPediaSpotlightProperties properties) {
        super(properties);
        this.semEntityRepository = semEntityRepository;
        this.properties = properties;
    }

    @Async
    public CompletableFuture<List<SemEntity>> extractSemEntities(String value, Language language) {

        var result = new CompletableFuture<List<SemEntity>>();
        List<SemEntity> semEntities = new ArrayList<>();
        if(!StringUtils.isNotEmpty(value)){
            result.complete(semEntities);
        }
        else{
            var url = buildUrl("/" + language.name() + "/annotate/" , Map.of("text", value, "confidence", properties.getConfidence()));

            try {
                log.info("Calling url {}", url);
                var response = restTemplate.exchange(url, HttpMethod.GET, getRequest(), DBPediaSpotlightResponse.class);
                semEntities = Objects.requireNonNull(response.getBody()).getResources().stream()
                        .map(this::toSemEntity)
                        .collect(Collectors.toList());
                log.info("Got entities {}", semEntities.size());
                result.complete(semEntities);
            } catch (Exception e) {
                log.error("Got error from url {} {}", url, e.getMessage());
                result.completeExceptionally(e);
            }
        }
        return result;
    }

    private Map<String, SemEntity> synchronizedEntityMap = Collections.synchronizedMap(new HashMap<>());

    private SemEntity toSemEntity(Resource resource) {
        SemEntity semEntity = synchronizedEntityMap.get(resource.getURI());
        if(semEntity != null){
            return semEntity;
        }
        else{
            semEntity = semEntityRepository.findOneByValueAndType(resource.getURI(), SemEntity.SemEntityType.DBPEDIA)
                    .orElseGet(() -> createSemEntityFromResource(resource));
            synchronizedEntityMap.put(resource.getURI(), semEntity);
            return semEntity;
        }
    }

    private SemEntity createSemEntityFromResource(Resource resource) {
        SemEntity.SemEntityType semEntityType = SemEntity.SemEntityType.DBPEDIA;
        BigDecimal confidence = new BigDecimal(resource.getSimilarityScore());
        String surfaceForm = resource.getSurfaceForm();
        String resourceUri = resource.getURI();
        String value = resourceUri.replace("http://dbpedia.org/resource/", ":");
        SemEntity semEntity = new SemEntity();
        semEntity.setType(semEntityType);
        semEntity.setSurfaceForm(surfaceForm);
        semEntity.setValue(value);
        semEntity.setUri(resourceUri);
        semEntity.setConfidence(confidence.doubleValue());
        return semEntityRepository.save(semEntity);
    }

    protected HttpEntity<String> getRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(headers);
    }

    public enum Language {
        de, en
    }
}
