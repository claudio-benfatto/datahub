package com.linkedin.metadata.configs;

import com.linkedin.metadata.dao.search.BaseSearchConfig;
import com.linkedin.metadata.dao.utils.SearchUtils;
import com.linkedin.metadata.search.AdevintaGroupDocument;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;


public class AdevintaGroupSearchConfig extends BaseSearchConfig<AdevintaGroupDocument> {
  @Override
  @Nonnull
  public Set<String> getFacetFields() {
    return Collections.emptySet();
  }

  @Override
  @Nonnull
  public Class<AdevintaGroupDocument> getSearchDocument() {
    return AdevintaGroupDocument.class;
  }

  @Override
  @Nonnull
  public String getDefaultAutocompleteField() {
    return "email";
  }

  @Override
  @Nonnull
  public String getSearchQueryTemplate() {
    return SearchUtils.readResourceFile(getClass(), "adevintaGroupESSearchQueryTemplate.json");
  }

  @Override
  @Nonnull
  public String getAutocompleteQueryTemplate() {
    return SearchUtils.readResourceFile(getClass(), "adevintaGroupESAutocompleteQueryTemplate.json");
  }
}