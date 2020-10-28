package com.linkedin.metadata.builders.graph;

import com.linkedin.common.urn.AdevintaOrganisationUrn;
import com.linkedin.data.template.RecordTemplate;
//import com.linkedin.identity.AdevintaOrganisationInfo;
import com.linkedin.metadata.builders.graph.relationship.BaseRelationshipBuilder;
//import com.linkedin.metadata.builders.graph.relationship.ReportsToBuilderFromAdevintaOrganisationInfo;
//import com.linkedin.metadata.dao.utils.ModelUtils;
import com.linkedin.metadata.entity.AdevintaOrganisationEntity;
import com.linkedin.metadata.snapshot.AdevintaOrganisationSnapshot;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
//import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;


public class AdevintaOrganisationGraphBuilder extends BaseGraphBuilder<AdevintaOrganisationSnapshot> {

  private static final Set<BaseRelationshipBuilder> RELATIONSHIP_BUILDERS =
      Collections.unmodifiableSet(new HashSet<BaseRelationshipBuilder>() {
        {
          //add(new ReportsToBuilderFromAdevintaOrganisationInfo());
        }
      });

  public AdevintaOrganisationGraphBuilder() {
    super(AdevintaOrganisationSnapshot.class, RELATIONSHIP_BUILDERS);
  }

  @Nonnull
  @Override
  protected List<? extends RecordTemplate> buildEntities(@Nonnull AdevintaOrganisationSnapshot snapshot) {
    final AdevintaOrganisationUrn urn = snapshot.getUrn();
    final AdevintaOrganisationEntity entity = new AdevintaOrganisationEntity().setUrn(urn)
        .setRemoved(false)
        .setName(urn.getOrganisationNameEntity());

    return Collections.singletonList(entity);
  }
}
