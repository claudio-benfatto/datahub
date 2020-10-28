package com.linkedin.identity.client;

import com.linkedin.common.urn.AdevintaOrganisationUrn;
import com.linkedin.data.template.StringArray;
import com.linkedin.identity.AdevintaOrganisation;
import com.linkedin.identity.AdevintaOrganisationKey;
import com.linkedin.identity.AdevintaOrganisationsDoAutocompleteRequestBuilder;
import com.linkedin.identity.AdevintaOrganisationsFindBySearchRequestBuilder;
import com.linkedin.identity.AdevintaOrganisationsRequestBuilders;
import com.linkedin.metadata.configs.AdevintaOrganisationSearchConfig;
import com.linkedin.metadata.query.AutoCompleteResult;
import com.linkedin.metadata.query.Filter;
import com.linkedin.metadata.query.SortCriterion;
import com.linkedin.metadata.restli.BaseSearchableClient;
import com.linkedin.r2.RemoteInvocationException;
import com.linkedin.restli.client.BatchGetEntityRequest;
import com.linkedin.restli.client.Client;
import com.linkedin.restli.client.GetRequest;
import com.linkedin.restli.common.CollectionResponse;
import com.linkedin.restli.common.ComplexResourceKey;
import com.linkedin.restli.common.EmptyRecord;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.linkedin.metadata.dao.utils.QueryUtils.*;

public class AdevintaOrganisations extends BaseSearchableClient<AdevintaOrganisation> {
  private static final AdevintaOrganisationsRequestBuilders ADEVINTA_ORGANISATIONS_REQUEST_BUILDERS = new AdevintaOrganisationsRequestBuilders();
  private static final AdevintaOrganisationSearchConfig ADEVINTA_ORGANISATION_SEARCH_CONFIG = new AdevintaOrganisationSearchConfig();

  public AdevintaOrganisations(@Nonnull Client restliClient) {
    super(restliClient);
  }

  /**
   * Gets {@link AdevintaOrganisation} model of the adevinta organisation
   *
   * @param urn adevinta organisation urn
   * @return {@link AdevintaOrganisation} model of the adevinta organisation
   * @throws RemoteInvocationException
   */
  @Nonnull
  public AdevintaOrganisation get(@Nonnull AdevintaOrganisationUrn urn)
      throws RemoteInvocationException {
    GetRequest<AdevintaOrganisation> getRequest = ADEVINTA_ORGANISATIONS_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(toAdevintaOrganisationKey(urn), new EmptyRecord()))
        .build();

    return _client.sendRequest(getRequest).getResponse().getEntity();
  }

  /**
   * Batch gets list of {@link AdevintaOrganisation} models of the adevinta organisations
   *
   * @param urns list of adevinta organisation urn
   * @return map of {@link AdevintaOrganisation} models of the adevinta organisations
   * @throws RemoteInvocationException
   */
  @Nonnull
  public Map<AdevintaOrganisationUrn, AdevintaOrganisation> batchGet(@Nonnull Set<AdevintaOrganisationUrn> urns)
      throws RemoteInvocationException {
    BatchGetEntityRequest<ComplexResourceKey<AdevintaOrganisationKey, EmptyRecord>, AdevintaOrganisation> batchGetRequest
        = ADEVINTA_ORGANISATIONS_REQUEST_BUILDERS.batchGet()
        .ids(urns.stream().map(this::getKeyFromUrn).collect(Collectors.toSet()))
        .build();

    return _client.sendRequest(batchGetRequest).getResponseEntity().getResults()
        .entrySet().stream().collect(Collectors.toMap(
            entry -> getUrnFromKey(entry.getKey()),
            entry -> entry.getValue().getEntity())
        );
  }

  @Override
  @Nonnull
  public CollectionResponse<AdevintaOrganisation> search(@Nonnull String input, @Nullable StringArray aspectNames,
      @Nullable Map<String, String> requestFilters, @Nullable SortCriterion sortCriterion, int start, int count)
      throws RemoteInvocationException {
    final Filter filter = (requestFilters != null) ? newFilter(requestFilters) : null;
    final AdevintaOrganisationsFindBySearchRequestBuilder requestBuilder = ADEVINTA_ORGANISATIONS_REQUEST_BUILDERS.findBySearch()
        .inputParam(input)
        .aspectsParam(aspectNames)
        .filterParam(filter)
        .sortParam(sortCriterion)
        .paginate(start, count);
    return _client.sendRequest(requestBuilder.build()).getResponse().getEntity();
  }

  @Nonnull
  public CollectionResponse<AdevintaOrganisation> search(@Nonnull String input, @Nullable Map<String, String> requestFilters,
      int start, int count) throws RemoteInvocationException {
    return search(input, requestFilters, null, start, count);
  }

  @Nonnull
  public CollectionResponse<AdevintaOrganisation> search(@Nonnull String input, int start, int count)
      throws RemoteInvocationException {
    return search(input, null, null, start, count);
  }

  @Nonnull
  public AutoCompleteResult autocomplete(@Nonnull String query, @Nullable String field,
      @Nullable Map<String, String> requestFilters, int limit) throws RemoteInvocationException {
    final String autocompleteField = (field != null) ? field : ADEVINTA_ORGANISATION_SEARCH_CONFIG.getDefaultAutocompleteField();
    final Filter filter = (requestFilters != null) ? newFilter(requestFilters) : null;
    AdevintaOrganisationsDoAutocompleteRequestBuilder requestBuilder = ADEVINTA_ORGANISATIONS_REQUEST_BUILDERS
        .actionAutocomplete()
        .queryParam(query)
        .fieldParam(autocompleteField)
        .filterParam(filter)
        .limitParam(limit);
    return _client.sendRequest(requestBuilder.build()).getResponse().getEntity();
  }

  @Nonnull
  private AdevintaOrganisationKey toAdevintaOrganisationKey(@Nonnull AdevintaOrganisationUrn urn) {
    return new AdevintaOrganisationKey().setName(urn.getOrganisationNameEntity());
  }

  @Nonnull
  private ComplexResourceKey<AdevintaOrganisationKey, EmptyRecord> getKeyFromUrn(@Nonnull AdevintaOrganisationUrn urn) {
    return new ComplexResourceKey<>(toAdevintaOrganisationKey(urn), new EmptyRecord());
  }

  @Nonnull
  private AdevintaOrganisationUrn toAdevintaOrganisationUrn(@Nonnull AdevintaOrganisationKey key) {
    return new AdevintaOrganisationUrn(key.getName());
  }

  @Nonnull
  private AdevintaOrganisationUrn getUrnFromKey(@Nonnull ComplexResourceKey<AdevintaOrganisationKey, EmptyRecord> key) {
    return toAdevintaOrganisationUrn(key.getKey());
  }
}