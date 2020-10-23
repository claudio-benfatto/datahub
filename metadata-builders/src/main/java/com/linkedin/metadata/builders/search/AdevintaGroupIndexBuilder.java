package com.linkedin.metadata.builders.search;

import com.linkedin.common.urn.AdevintaGroupUrn;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.identity.AdevintaGroupInfo;
import com.linkedin.metadata.search.AdevintaGroupDocument;
import com.linkedin.metadata.snapshot.AdevintaGroupSnapshot;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AdevintaGroupIndexBuilder extends BaseIndexBuilder<AdevintaGroupDocument> {

  public AdevintaGroupIndexBuilder() {
    super(Collections.singletonList(AdevintaGroupSnapshot.class), AdevintaGroupDocument.class);
  }

  @Nonnull
  private AdevintaGroupDocument getDocumentToUpdateFromAspect(@Nonnull AdevintaGroupUrn urn,
      @Nonnull AdevintaGroupInfo adevintaGroupInfo) {
    return new AdevintaGroupDocument().setUrn(urn)
        .setAdmins(BuilderUtils.getAdevintaUsernames(adevintaGroupInfo.getAdmins()))
        .setMembers(BuilderUtils.getAdevintaUsernames(adevintaGroupInfo.getMembers()))
        .setGroups(BuilderUtils.getAdevintaGroupnames(adevintaGroupInfo.getGroups()))
        .setEmail(adevintaGroupInfo.getEmail());
  }

  @Nonnull
  private List<AdevintaGroupDocument> getDocumentsToUpdateFromSnapshotType(@Nonnull AdevintaGroupSnapshot adevintaGroupSnapshot) {
    final AdevintaGroupUrn urn = adevintaGroupSnapshot.getUrn();
    return adevintaGroupSnapshot.getAspects().stream().map(aspect -> {
      if (aspect.isAdevintaGroupInfo()) {
        return getDocumentToUpdateFromAspect(urn, aspect.getAdevintaGroupInfo());
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  @Override
  @Nonnull
  public final List<AdevintaGroupDocument> getDocumentsToUpdate(@Nonnull RecordTemplate genericSnapshot) {
    if (genericSnapshot instanceof AdevintaGroupSnapshot) {
      return getDocumentsToUpdateFromSnapshotType((AdevintaGroupSnapshot) genericSnapshot);
    }
    return Collections.emptyList();
  }

  @Override
  @Nonnull
  public Class<AdevintaGroupDocument> getDocumentType() {
    return AdevintaGroupDocument.class;
  }
}