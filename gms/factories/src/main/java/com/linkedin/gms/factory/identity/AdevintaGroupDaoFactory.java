package com.linkedin.gms.factory.identity;

import com.linkedin.gms.factory.common.TopicConventionFactory;
import com.linkedin.common.urn.AdevintaGroupUrn;
import com.linkedin.metadata.aspect.AdevintaGroupAspect;
import com.linkedin.metadata.dao.EbeanLocalDAO;
import com.linkedin.metadata.dao.producer.KafkaMetadataEventProducer;
import com.linkedin.metadata.snapshot.AdevintaGroupSnapshot;
import com.linkedin.mxe.TopicConvention;
import io.ebean.config.ServerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Nonnull;


@Configuration
public class AdevintaGroupDaoFactory {
  @Autowired
  ApplicationContext applicationContext;

  @Bean(name = "adevintaGroupDao")
  @DependsOn({"gmsEbeanServiceConfig", "kafkaEventProducer", TopicConventionFactory.TOPIC_CONVENTION_BEAN})
  @Nonnull
  protected EbeanLocalDAO createInstance() {
    KafkaMetadataEventProducer<AdevintaGroupSnapshot, AdevintaGroupAspect, AdevintaGroupUrn> producer =
        new KafkaMetadataEventProducer(AdevintaGroupSnapshot.class, AdevintaGroupAspect.class,
            applicationContext.getBean(Producer.class), applicationContext.getBean(TopicConvention.class));
    return new EbeanLocalDAO<>(AdevintaGroupAspect.class, producer, applicationContext.getBean(ServerConfig.class),
        AdevintaGroupUrn.class);
  }
}