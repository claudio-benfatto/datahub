package com.linkedin.identity.client;

import com.linkedin.common.urn.AdevintaGroupUrn;
import com.linkedin.data.template.StringArray;
import com.linkedin.identity.AdevintaGroup;
import com.linkedin.identity.AdevintaGroupKey;
import com.linkedin.identity.AdevintaGroupsDoAutocompleteRequestBuilder;
import com.linkedin.identity.AdevintaGroupsFindBySearchRequestBuilder;
import com.linkedin.identity.AdevintaGroupsRequestBuilders;
import com.linkedin.metadata.configs.AdevintaGroupSearchConfig;
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

public class AdevintaGroups extends BaseSearchableClient<AdevintaGroup> {
  private static final AdevintaGroupsRequestBuilders ADEVINTA_GROUPS_REQUEST_BUILDERS = new AdevintaGroupsRequestBuilders();
  private static final AdevintaGroupSearchConfig ADEVINTA_GROUP_SEARCH_CONFIG = new AdevintaGroupSearchConfig();

  public AdevintaGroups(@Nonnull Client restliClient) {
    super(restliClient);
  }

  /**
   * Gets {@link CorpGroup} model of the corp group
   *
   * @param urn corp group urn
   * @return {@link CorpGroup} model of the corp group
   * @throws RemoteInvocationException
   */
  @Nonnull
  public AdevintaGroup get(@Nonnull AdevintaGroupUrn urn)
      throws RemoteInvocationException {
    GetRequest<AdevintaGroup> getRequest = ADEVINTA_GROUPS_REQUEST_BUILDERS.get()
        .id(new ComplexResourceKey<>(toAdevintaGroupKey(urn), new EmptyRecord()))
        .build();

    return _client.sendRequest(getRequest).getResponse().getEntity();
  }

  /**
   * Batch gets list of {@link CorpGroup} models of the corp groups
   *
   * @param urns list of corp group urn
   * @return map of {@link CorpGroup} models of the corp groups
   * @throws RemoteInvocationException
   */
  @Nonnull
  public Map<AdevintaGroupUrn, AdevintaGroup> batchGet(@Nonnull Set<AdevintaGroupUrn> urns)
      throws RemoteInvocationException {
    BatchGetEntityRequest<ComplexResourceKey<AdevintaGroupKey, EmptyRecord>, AdevintaGroup> batchGetRequest
        = ADEVINTA_GROUPS_REQUEST_BUILDERS.batchGet()
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
  public CollectionResponse<AdevintaGroup> search(@Nonnull String input, @Nullable StringArray aspectNames,
      @Nullable Map<String, String> requestFilters, @Nullable SortCriterion sortCriterion, int start, int count)
      throws RemoteInvocationException {
    final Filter filter = (requestFilters != null) ? newFilter(requestFilters) : null;
    final AdevintaGroupsFindBySearchRequestBuilder requestBuilder = ADEVINTA_GROUPS_REQUEST_BUILDERS.findBySearch()
        .inputParam(input)
        .aspectsParam(aspectNames)
        .filterParam(filter)
        .sortParam(sortCriterion)
        .paginate(start, count);
    return _client.sendRequest(requestBuilder.build()).getResponse().getEntity();
  }

  @Nonnull
  public CollectionResponse<AdevintaGroup> search(@Nonnull String input, @Nullable Map<String, String> requestFilters,
      int start, int count) throws RemoteInvocationException {
    return search(input, requestFilters, null, start, count);
  }

  @Nonnull
  public CollectionResponse<AdevintaGroup> search(@Nonnull String input, int start, int count)
      throws RemoteInvocationException {
    return search(input, null, null, start, count);
  }

  @Nonnull
  public AutoCompleteResult autocomplete(@Nonnull String query, @Nullable String field,
      @Nullable Map<String, String> requestFilters, int limit) throws RemoteInvocationException {
    final String autocompleteField = (field != null) ? field : ADEVINTA_GROUP_SEARCH_CONFIG.getDefaultAutocompleteField();
    final Filter filter = (requestFilters != null) ? newFilter(requestFilters) : null;
    AdevintaGroupsDoAutocompleteRequestBuilder requestBuilder = ADEVINTA_GROUPS_REQUEST_BUILDERS
        .actionAutocomplete()
        .queryParam(query)
        .fieldParam(autocompleteField)
        .filterParam(filter)
        .limitParam(limit);
    return _client.sendRequest(requestBuilder.build()).getResponse().getEntity();
  }

  @Nonnull
  private AdevintaGroupKey toAdevintaGroupKey(@Nonnull AdevintaGroupUrn urn) {
    return new AdevintaGroupKey().setName(urn.getGroupNameEntity());
  }

  @Nonnull
  private ComplexResourceKey<AdevintaGroupKey, EmptyRecord> getKeyFromUrn(@Nonnull AdevintaGroupUrn urn) {
    return new ComplexResourceKey<>(toAdevintaGroupKey(urn), new EmptyRecord());
  }

  @Nonnull
  private AdevintaGroupUrn toAdevintaGroupUrn(@Nonnull AdevintaGroupKey key) {
    return new AdevintaGroupUrn(key.getName());
  }

  @Nonnull
  private AdevintaGroupUrn getUrnFromKey(@Nonnull ComplexResourceKey<AdevintaGroupKey, EmptyRecord> key) {
    return toAdevintaGroupUrn(key.getKey());
  }
}