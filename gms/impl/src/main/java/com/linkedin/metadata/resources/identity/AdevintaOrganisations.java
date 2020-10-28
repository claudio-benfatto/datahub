package com.linkedin.metadata.resources.identity;

import com.linkedin.common.urn.AdevintaOrganisationUrn;
import com.linkedin.identity.AdevintaOrganisation;
import com.linkedin.identity.AdevintaOrganisationInfo;
import com.linkedin.identity.AdevintaOrganisationKey;
import com.linkedin.metadata.aspect.AdevintaOrganisationAspect;
import com.linkedin.metadata.dao.BaseLocalDAO;
import com.linkedin.metadata.dao.BaseSearchDAO;
import com.linkedin.metadata.dao.utils.ModelUtils;
import com.linkedin.metadata.query.AutoCompleteResult;
import com.linkedin.metadata.query.Filter;
import com.linkedin.metadata.query.SearchResultMetadata;
import com.linkedin.metadata.query.SortCriterion;
import com.linkedin.metadata.restli.BackfillResult;
import com.linkedin.metadata.restli.BaseSearchableEntityResource;
import com.linkedin.metadata.search.AdevintaOrganisationDocument;
import com.linkedin.metadata.snapshot.AdevintaOrganisationSnapshot;
import com.linkedin.parseq.Task;
import com.linkedin.restli.common.ComplexResourceKey;
import com.linkedin.restli.common.EmptyRecord;
import com.linkedin.restli.server.CollectionResult;
import com.linkedin.restli.server.PagingContext;
import com.linkedin.restli.server.annotations.Action;
import com.linkedin.restli.server.annotations.ActionParam;
import com.linkedin.restli.server.annotations.Finder;
import com.linkedin.restli.server.annotations.Optional;
import com.linkedin.restli.server.annotations.PagingContextParam;
import com.linkedin.restli.server.annotations.QueryParam;
import com.linkedin.restli.server.annotations.RestLiCollection;
import com.linkedin.restli.server.annotations.RestMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import static com.linkedin.metadata.restli.RestliConstants.*;


@RestLiCollection(name = "adevintaOrganisations", namespace = "com.linkedin.identity", keyName = "adevintaOrganisation")
public final class AdevintaOrganisations extends BaseSearchableEntityResource<
    // @formatter:off
    AdevintaOrganisationKey,
    AdevintaOrganisation,
    AdevintaOrganisationUrn,
    AdevintaOrganisationSnapshot,
    AdevintaOrganisationAspect,
    AdevintaOrganisationDocument> {
    // @formatter:on

  @Inject
  @Named("adevintaOrganisationDao")
  private BaseLocalDAO<AdevintaOrganisationAspect, AdevintaOrganisationUrn> _localDAO;

  @Inject
  @Named("adevintaOrganisationSearchDAO")
  private BaseSearchDAO _esSearchDAO;

  public AdevintaOrganisations() {
    super(AdevintaOrganisationSnapshot.class, AdevintaOrganisationAspect.class);
  }

  @Override
  @Nonnull
  protected BaseLocalDAO<AdevintaOrganisationAspect, AdevintaOrganisationUrn> getLocalDAO() {
    return _localDAO;
  }

  @Override
  @Nonnull
  protected AdevintaOrganisationUrn createUrnFromString(@Nonnull String urnString) throws Exception {
    return AdevintaOrganisationUrn.deserialize(urnString);
  }

  @Override
  @Nonnull
  protected BaseSearchDAO getSearchDAO() {
    return _esSearchDAO;
  }

  @Override
  @Nonnull
  protected AdevintaOrganisationUrn toUrn(@Nonnull AdevintaOrganisationKey adevintaOrganisationKey) {
    return new AdevintaOrganisationUrn(adevintaOrganisationKey.getName());
  }

  @Override
  @Nonnull
  protected AdevintaOrganisationKey toKey(@Nonnull AdevintaOrganisationUrn urn) {
    return new AdevintaOrganisationKey().setName(urn.getOrganisationNameEntity());
  }

  @Override
  @Nonnull
  protected AdevintaOrganisation toValue(@Nonnull AdevintaOrganisationSnapshot snapshot) {
    final AdevintaOrganisation value = new AdevintaOrganisation().setName(snapshot.getUrn().getOrganisationNameEntity());
    ModelUtils.getAspectsFromSnapshot(snapshot).forEach(aspect -> {
      if (aspect instanceof AdevintaOrganisationInfo) {
        value.setInfo(AdevintaOrganisationInfo.class.cast(aspect));
      }
    });
    return value;
  }

  @Override
  @Nonnull
  protected AdevintaOrganisationSnapshot toSnapshot(@Nonnull AdevintaOrganisation adevintaOrganisation, @Nonnull AdevintaOrganisationUrn urn) {
    final List<AdevintaOrganisationAspect> aspects = new ArrayList<>();
    if (adevintaOrganisation.hasInfo()) {
      aspects.add(ModelUtils.newAspectUnion(AdevintaOrganisationAspect.class, adevintaOrganisation.getInfo()));
    }
    return ModelUtils.newSnapshot(AdevintaOrganisationSnapshot.class, urn, aspects);
  }

  @RestMethod.Get
  @Override
  @Nonnull
  public Task<AdevintaOrganisation> get(@Nonnull ComplexResourceKey<AdevintaOrganisationKey, EmptyRecord> key,
      @QueryParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames) {
    return super.get(key, aspectNames);
  }

  @RestMethod.BatchGet
  @Override
  @Nonnull
  public Task<Map<ComplexResourceKey<AdevintaOrganisationKey, EmptyRecord>, AdevintaOrganisation>> batchGet(
      @Nonnull Set<ComplexResourceKey<AdevintaOrganisationKey, EmptyRecord>> keys,
      @QueryParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames) {
    return super.batchGet(keys, aspectNames);
  }

  @RestMethod.GetAll
  @Nonnull
  public Task<List<AdevintaOrganisation>> getAll(@PagingContextParam @Nonnull PagingContext pagingContext,
      @QueryParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames,
      @QueryParam(PARAM_FILTER) @Optional @Nullable Filter filter,
      @QueryParam(PARAM_SORT) @Optional @Nullable SortCriterion sortCriterion) {
    return super.getAll(pagingContext, aspectNames, filter, sortCriterion);
  }

  @Finder(FINDER_SEARCH)
  @Override
  @Nonnull
  public Task<CollectionResult<AdevintaOrganisation, SearchResultMetadata>> search(@QueryParam(PARAM_INPUT) @Nonnull String input,
      @QueryParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames,
      @QueryParam(PARAM_FILTER) @Optional @Nullable Filter filter,
      @QueryParam(PARAM_SORT) @Optional @Nullable SortCriterion sortCriterion,
      @PagingContextParam @Nonnull PagingContext pagingContext) {
    return super.search(input, aspectNames, filter, sortCriterion, pagingContext);
  }

  @Action(name = ACTION_AUTOCOMPLETE)
  @Override
  @Nonnull
  public Task<AutoCompleteResult> autocomplete(@ActionParam(PARAM_QUERY) @Nonnull String query,
      @ActionParam(PARAM_FIELD) @Nullable String field, @ActionParam(PARAM_FILTER) @Nullable Filter filter,
      @ActionParam(PARAM_LIMIT) int limit) {
    return super.autocomplete(query, field, filter, limit);
  }

  @Action(name = ACTION_INGEST)
  @Override
  @Nonnull
  public Task<Void> ingest(@ActionParam(PARAM_SNAPSHOT) @Nonnull AdevintaOrganisationSnapshot snapshot) {
    return super.ingest(snapshot);
  }

  @Action(name = ACTION_GET_SNAPSHOT)
  @Override
  @Nonnull
  public Task<AdevintaOrganisationSnapshot> getSnapshot(@ActionParam(PARAM_URN) @Nonnull String urnString,
      @ActionParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames) {
    return super.getSnapshot(urnString, aspectNames);
  }

  @Action(name = ACTION_BACKFILL)
  @Override
  @Nonnull
  public Task<BackfillResult> backfill(@ActionParam(PARAM_URN) @Nonnull String urnString,
      @ActionParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames) {
    return super.backfill(urnString, aspectNames);
  }
}
