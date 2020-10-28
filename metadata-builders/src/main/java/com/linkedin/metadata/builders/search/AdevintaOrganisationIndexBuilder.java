package com.linkedin.metadata.builders.search;

import com.linkedin.common.urn.AdevintaOrganisationUrn;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.identity.AdevintaOrganisationInfo;
import com.linkedin.metadata.search.AdevintaOrganisationDocument;
import com.linkedin.metadata.snapshot.AdevintaOrganisationSnapshot;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AdevintaOrganisationIndexBuilder extends BaseIndexBuilder<AdevintaOrganisationDocument> {

  public AdevintaOrganisationIndexBuilder() {
    super(Collections.singletonList(AdevintaOrganisationSnapshot.class), AdevintaOrganisationDocument.class);
  }

  @Nonnull
  private AdevintaOrganisationDocument getDocumentToUpdateFromAspect(@Nonnull AdevintaOrganisationUrn urn,
      @Nonnull AdevintaOrganisationInfo adevintaOrganisationInfo) {
    return new AdevintaOrganisationDocument().setUrn(urn)
        .setName(adevintaOrganisationInfo.getName());
  }

  @Nonnull
  private List<AdevintaOrganisationDocument> getDocumentsToUpdateFromSnapshotType(@Nonnull AdevintaOrganisationSnapshot adevintaOrganisationSnapshot) {
    final AdevintaOrganisationUrn urn = adevintaOrganisationSnapshot.getUrn();
    return adevintaOrganisationSnapshot.getAspects().stream().map(aspect -> {
      if (aspect.isAdevintaOrganisationInfo()) {
        return getDocumentToUpdateFromAspect(urn, aspect.getAdevintaOrganisationInfo());
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  @Override
  @Nonnull
  public final List<AdevintaOrganisationDocument> getDocumentsToUpdate(@Nonnull RecordTemplate genericSnapshot) {
    if (genericSnapshot instanceof AdevintaOrganisationSnapshot) {
      return getDocumentsToUpdateFromSnapshotType((AdevintaOrganisationSnapshot) genericSnapshot);
    }
    return Collections.emptyList();
  }

  @Override
  @Nonnull
  public Class<AdevintaOrganisationDocument> getDocumentType() {
    return AdevintaOrganisationDocument.class;
  }
}