package com.solrion.core.internal.protocol.envelope;

import com.solrion.core.api.request.update.*;
import com.solrion.core.api.types.SolrDocument;
import com.solrion.core.internal.Validate;
import com.solrion.core.query.SolrDialectTranslator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/// Builds JSON bodies for Solr perBatchUpdate handler.
///
///
/// The perBatchUpdate handler supports JSON commands:
///  - add
///  - delete
///  - commit
///
public final class UpdateEnvelopeMapper {

  private final SolrDialectTranslator translator;

  public UpdateEnvelopeMapper(SolrDialectTranslator translator) {
    this.translator = Validate.notNull(translator, "translator");
  }

  public Map<String, Object> toAddEnvelope(AddDocumentsRequest req) {
    Validate.notNull(req, "req");

    List<Object> docs = new ArrayList<>();
    for (SolrDocument d : req.documents()) {
      if (d == null) {
        continue;
      }
      docs.add(d.fields());
    }

    return Map.of("add", docs);
  }

  public Map<String, Object> toDeleteByIdEnvelope(DeleteByIdRequest req) {
    Validate.notNull(req, "req");

    List<String> ids = req.ids().stream().filter(id -> !Validate.isBlank(id)).toList();

    return Map.of("delete", ids);
  }

  public Map<String, Object> toDeleteByQueryEnvelope(DeleteByQueryRequest req) {
    Validate.notNull(req, "req");
    String q = translator.render(req.query());
    Validate.require(!Validate.isBlank(q), "deleteByQuery queries rendered to blank");

    return Map.of("delete", Map.of("queries", q));
  }
}
