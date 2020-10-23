package com.linkedin.metadata.resources.identity;

import com.linkedin.common.urn.AdevintaGroupUrn;
import com.linkedin.identity.AdevintaGroup;
import com.linkedin.identity.AdevintaGroupInfo;
import com.linkedin.identity.AdevintaGroupKey;
import com.linkedin.metadata.aspect.AdevintaGroupAspect;
import com.linkedin.metadata.dao.BaseLocalDAO;
import com.linkedin.metadata.dao.BaseSearchDAO;
import com.linkedin.metadata.dao.utils.ModelUtils;
import com.linkedin.metadata.query.AutoCompleteResult;
import com.linkedin.metadata.query.Filter;
import com.linkedin.metadata.query.SearchResultMetadata;
import com.linkedin.metadata.query.SortCriterion;
import com.linkedin.metadata.restli.BackfillResult;
import com.linkedin.metadata.restli.BaseSearchableEntityResource;
import com.linkedin.metadata.search.AdevintaGroupDocument;
import com.linkedin.metadata.snapshot.AdevintaGroupSnapshot;
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


@RestLiCollection(name = "adevintaGroups", namespace = "com.linkedin.identity", keyName = "adevintaGroup")
public final class AdevintaGroups extends BaseSearchableEntityResource<
    // @formatter:off
    AdevintaGroupKey,
    AdevintaGroup,
    AdevintaGroupUrn,
    AdevintaGroupSnapshot,
    AdevintaGroupAspect,
    AdevintaGroupDocument> {
    // @formatter:on

  @Inject
  @Named("adevintaGroupDao")
  private BaseLocalDAO<AdevintaGroupAspect, AdevintaGroupUrn> _localDAO;

  @Inject
  @Named("adevintaGroupSearchDAO")
  private BaseSearchDAO _esSearchDAO;

  public AdevintaGroups() {
    super(AdevintaGroupSnapshot.class, AdevintaGroupAspect.class);
  }

  @Override
  @Nonnull
  protected BaseLocalDAO<AdevintaGroupAspect, AdevintaGroupUrn> getLocalDAO() {
    return _localDAO;
  }

  @Override
  @Nonnull
  protected AdevintaGroupUrn createUrnFromString(@Nonnull String urnString) throws Exception {
    return AdevintaGroupUrn.deserialize(urnString);
  }

  @Override
  @Nonnull
  protected BaseSearchDAO getSearchDAO() {
    return _esSearchDAO;
  }

  @Override
  @Nonnull
  protected AdevintaGroupUrn toUrn(@Nonnull AdevintaGroupKey adevintaGroupKey) {
    return new AdevintaGroupUrn(adevintaGroupKey.getName());
  }

  @Override
  @Nonnull
  protected AdevintaGroupKey toKey(@Nonnull AdevintaGroupUrn urn) {
    return new AdevintaGroupKey().setName(urn.getGroupNameEntity());
  }

  @Override
  @Nonnull
  protected AdevintaGroup toValue(@Nonnull AdevintaGroupSnapshot snapshot) {
    final AdevintaGroup value = new AdevintaGroup().setName(snapshot.getUrn().getGroupNameEntity());
    ModelUtils.getAspectsFromSnapshot(snapshot).forEach(aspect -> {
      if (aspect instanceof AdevintaGroupInfo) {
        value.setInfo(AdevintaGroupInfo.class.cast(aspect));
      }
    });
    return value;
  }

  @Override
  @Nonnull
  protected AdevintaGroupSnapshot toSnapshot(@Nonnull AdevintaGroup adevintaGroup, @Nonnull AdevintaGroupUrn urn) {
    final List<AdevintaGroupAspect> aspects = new ArrayList<>();
    if (adevintaGroup.hasInfo()) {
      aspects.add(ModelUtils.newAspectUnion(AdevintaGroupAspect.class, adevintaGroup.getInfo()));
    }
    return ModelUtils.newSnapshot(AdevintaGroupSnapshot.class, urn, aspects);
  }

  @RestMethod.Get
  @Override
  @Nonnull
  public Task<AdevintaGroup> get(@Nonnull ComplexResourceKey<AdevintaGroupKey, EmptyRecord> key,
      @QueryParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames) {
    return super.get(key, aspectNames);
  }

  @RestMethod.BatchGet
  @Override
  @Nonnull
  public Task<Map<ComplexResourceKey<AdevintaGroupKey, EmptyRecord>, AdevintaGroup>> batchGet(
      @Nonnull Set<ComplexResourceKey<AdevintaGroupKey, EmptyRecord>> keys,
      @QueryParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames) {
    return super.batchGet(keys, aspectNames);
  }

  @RestMethod.GetAll
  @Nonnull
  public Task<List<AdevintaGroup>> getAll(@PagingContextParam @Nonnull PagingContext pagingContext,
      @QueryParam(PARAM_ASPECTS) @Optional @Nullable String[] aspectNames,
      @QueryParam(PARAM_FILTER) @Optional @Nullable Filter filter,
      @QueryParam(PARAM_SORT) @Optional @Nullable SortCriterion sortCriterion) {
    return super.getAll(pagingContext, aspectNames, filter, sortCriterion);
  }

  @Finder(FINDER_SEARCH)
  @Override
  @Nonnull
  public Task<CollectionResult<AdevintaGroup, SearchResultMetadata>> search(@QueryParam(PARAM_INPUT) @Nonnull String input,
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
  public Task<Void> ingest(@ActionParam(PARAM_SNAPSHOT) @Nonnull AdevintaGroupSnapshot snapshot) {
    return super.ingest(snapshot);
  }

  @Action(name = ACTION_GET_SNAPSHOT)
  @Override
  @Nonnull
  public Task<AdevintaGroupSnapshot> getSnapshot(@ActionParam(PARAM_URN) @Nonnull String urnString,
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
