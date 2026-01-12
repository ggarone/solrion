package com.solrion.core.api.request.update;

/** Options common to update operations. */
public record UpdateOptions(
    Boolean commit,
    Boolean softCommit,
    Boolean overwrite,
    Long commitWithinMs,
    Boolean waitSearcher,
    Boolean expungeDeletes,
    Boolean openSearcher) {
  public boolean hasCommit() {
    return commit != null && commit
        || softCommit != null && softCommit
        || commitWithinMs != null && commitWithinMs > 0;
  }

  public static UpdateOptions defaults() {
    return new UpdateOptions(null, null, null, null, null, null, null);
  }
}
