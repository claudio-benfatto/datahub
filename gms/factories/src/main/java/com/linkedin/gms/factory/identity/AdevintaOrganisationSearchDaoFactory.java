package com.linkedin.gms.factory.identity;

import com.linkedin.metadata.configs.AdevintaOrganisationSearchConfig;
import com.linkedin.metadata.dao.search.ESSearchDAO;
import com.linkedin.metadata.search.AdevintaOrganisationDocument;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Nonnull;

@Configuration
public class AdevintaOrganisationSearchDaoFactory {
  @Autowired
  ApplicationContext applicationContext;

  @Bean(name = "adevintaOrganisationSearchDAO")
  @DependsOn({"elasticSearchRestHighLevelClient"})
  @Nonnull
  protected ESSearchDAO createInstance() {
    return new ESSearchDAO(applicationContext.getBean(RestHighLevelClient.class), AdevintaOrganisationDocument.class,
        new AdevintaOrganisationSearchConfig());
  }
}