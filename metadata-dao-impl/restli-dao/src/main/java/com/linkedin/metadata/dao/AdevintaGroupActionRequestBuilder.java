package com.linkedin.metadata.dao;

import com.linkedin.common.urn.AdevintaGroupUrn;
import com.linkedin.metadata.snapshot.AdevintaGroupSnapshot;


/**
 * An action request builder for corp group info entities.
 */
public class AdevintaGroupActionRequestBuilder extends BaseActionRequestBuilder<AdevintaGroupSnapshot, AdevintaGroupUrn> {

  private static final String BASE_URI_TEMPLATE = "corpGroups";

  public AdevintaGroupActionRequestBuilder() {
    super(AdevintaGroupSnapshot.class, AdevintaGroupUrn.class, BASE_URI_TEMPLATE);
  }
}