package com.linkedin.gms.factory.identity;

import com.linkedin.metadata.configs.AdevintaGroupSearchConfig;
import com.linkedin.metadata.dao.search.ESSearchDAO;
import com.linkedin.metadata.search.AdevintaGroupDocument;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Nonnull;

@Configuration
public class AdevintaGroupSearchDaoFactory {
  @Autowired
  ApplicationContext applicationContext;

  @Bean(name = "adevintaGroupSearchDAO")
  @DependsOn({"elasticSearchRestHighLevelClient"})
  @Nonnull
  protected ESSearchDAO createInstance() {
    return new ESSearchDAO(applicationContext.getBean(RestHighLevelClient.class), AdevintaGroupDocument.class,
        new AdevintaGroupSearchConfig());
  }
}