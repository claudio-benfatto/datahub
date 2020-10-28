package com.linkedin.metadata.dao;

import com.linkedin.common.urn.AdevintaOrganisationUrn;
import com.linkedin.metadata.snapshot.AdevintaOrganisationSnapshot;


/**
 * An action request builder for corp group info entities.
 */
public class AdevintaOrganisationActionRequestBuilder extends BaseActionRequestBuilder<AdevintaOrganisationSnapshot, AdevintaOrganisationUrn> {

  private static final String BASE_URI_TEMPLATE = "adevintaOrganisations";

  public AdevintaOrganisationActionRequestBuilder() {
    super(AdevintaOrganisationSnapshot.class, AdevintaOrganisationUrn.class, BASE_URI_TEMPLATE);
  }
}