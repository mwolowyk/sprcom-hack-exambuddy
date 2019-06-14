package com.springer.hack.exambuddy.external.dbpediaspotlight;

import com.springer.hack.exambuddy.external.BaseRestServiceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties("dbpediaspotlight")
class DBPediaSpotlightProperties extends BaseRestServiceProperties {

    private BigDecimal confidence;

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }
}
