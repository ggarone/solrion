package com.solrion.core.query;

import com.solrion.core.types.Range;
import com.solrion.core.internal.Validate;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Renders {@link Expr} into Solr queries syntax.
 *
 * - Parentheses are applied based on operator precedence to preserve semantics.
 * - Empty expressions render to "" (caller decides whether to omit or default).
 */
public final class SolrDialectTranslator implements ExprVisitor<String, SolrDialectTranslator.Context> {
    public static final SolrDialectTranslator INSTANCE = new SolrDialectTranslator();

    private static final int PREC_OR = 1;
    private static final int PREC_AND = 2;
    private static final int PREC_NOT = 3;
    private static final int PREC_ATOM = 4;

    public static final class Context {
        private final Deque<Integer> parentPrec = new ArrayDeque<>();

        public void pushPrec(int prec) {
            parentPrec.push(prec);
        }

        public int popPrec() {
            return parentPrec.pop();
        }

        public int parentPrec() {
            return parentPrec.isEmpty() ? 0 : parentPrec.peek();
        }

        public static Context root() {
            return new Context();
        }
    }

    private SolrDialectTranslator() {}

    public List<String> renderAll(Collection<? extends Expr> exprs) {
        if(exprs == null || exprs.isEmpty()) return List.of();
        return exprs.stream()
                .filter(expr -> expr != null && !expr.isEmpty())
                .map(this::render)
                .filter(s -> !Validate.isBlank(s))
                .toList();
    }

    public String render(Expr expr) {
        if (expr == null || expr.isEmpty()) return "";
        return render(expr, 0, Context.root());
    }

    private String render(Expr expr, int parent, Context ctx) {
        ctx.pushPrec(parent);
        try {
            return expr.accept(this, ctx);
        } finally {
            ctx.popPrec();
        }
    }

    private String maybeWrap(int myPrec, Context ctx, String rendered) {
        if (Validate.isBlank(rendered)) return "";
        if (ctx.parentPrec() > myPrec) return "(" + rendered + ")";
        return rendered;
    }

    @Override
    public String visitEmpty(Expr.Empty empty, Context ctx) {
        return "";
    }

    @Override
    public String visitRaw(Expr.Raw raw, Context ctx) {
        if (raw == null || raw.isEmpty()) return "";
        return maybeWrap(PREC_ATOM, ctx, raw.data());
    }

    @Override
    public String visitGroup(Expr.Group group, Context ctx) {
        if (group == null || group.isEmpty()) return "";
        String inner = render(group.expr(), 0, ctx);
        if (Validate.isBlank(inner)) return "";
        return "(" + inner + ")";
    }

    @Override
    public String visitLocalParams(Expr.LocalParams params, Context ctx) {
        if (params == null || params.isEmpty()) return "";
        return params.params().entrySet().stream()
                .filter(e -> !Validate.isBlank(e.getKey()) && e.getValue() != null)
                .map(e -> e.getKey() + "=" + SolrEscaper.escapeTerm(e.getValue()))
                .collect(Collectors.joining(" ", "{!", "}"));
    }

    @Override
    public String visitField(Expr.Field field, Context ctx) {
        if (field == null || field.isEmpty()) return "";

        String lp = field.localParams() != null ? render(field.localParams(), PREC_ATOM, ctx) : "";
        String fn = SolrEscaper.escapeField(field.fieldName());
        String v = render(field.value(), PREC_ATOM, ctx);

        if (Validate.isBlank(fn) || Validate.isBlank(v)) return "";
        return maybeWrap(PREC_ATOM, ctx, lp + fn + ":" + v);
    }

    @Override
    public String visitAnd(Expr.And and, Context ctx) {
        if (and == null || and.isEmpty()) return "";
        String left = render(and.left(), PREC_AND, ctx);
        String right = render(and.right(), PREC_AND, ctx);
        if (Validate.isBlank(left) || Validate.isBlank(right)) return "";
        return maybeWrap(PREC_AND, ctx, left + " AND " + right);
    }

    @Override
    public String visitOr(Expr.Or or, Context ctx) {
        if (or == null || or.isEmpty()) return "";
        String left = render(or.left(), PREC_OR, ctx);
        String right = render(or.right(), PREC_OR, ctx);
        if (Validate.isBlank(left) || Validate.isBlank(right)) return "";
        return maybeWrap(PREC_OR, ctx, left + " OR " + right);
    }

    @Override
    public String visitNot(Expr.Not not, Context ctx) {
        if (not == null || not.isEmpty()) return "";
        String inner = render(not.expr(), PREC_NOT, ctx);
        if (Validate.isBlank(inner)) return "";
        return maybeWrap(PREC_NOT, ctx, "NOT " + inner);
    }

    @Override
    public String visitEmptyValue(Expr.EmptyValue empty, Context ctx) {
        return "";
    }

    @Override
    public String visitTerm(Expr.Term term, Context ctx) {
        if (term == null || term.isEmpty()) return "";

        ValueModifiers m = term.modifiers();
        String base = term.term();

        if (m != null && Boolean.TRUE.equals(m.regex())) {
            base = "/" + base + "/";

        } else if (m != null && Boolean.TRUE.equals(m.phrase())) {
            base = "\"" + SolrEscaper.escapePhrase(base) + "\"";

        } else if (m == null || !Boolean.TRUE.equals(m.wildcard())) {
            base = SolrEscaper.escapeTerm(base);
        }

        if (m != null && m.fuzzyDistance() != null) {
            base += "~" + m.fuzzyDistance();
        }
        if (m != null && m.proximity() != null) {
            base += "~" + m.proximity();
        }
        if (m != null && m.boost() != null) {
            base += "^" + m.boost();
        }

        return maybeWrap(PREC_ATOM, ctx, base);
    }

    @Override
    public String visitRangeValue(Expr.RangeValue<?> value, Context ctx) {
        if (value == null || value.isEmpty()) return "";

        Range.Bound<?> lo = value.range().lower();
        Range.Bound<?> hi = value.range().upper();

        String loV = (lo == null || !lo.bounded()) ? "*" : SolrEscaper.escapeTerm(String.valueOf(lo.value()));
        String hiV = (hi == null || !hi.bounded()) ? "*" : SolrEscaper.escapeTerm(String.valueOf(hi.value()));

        char l = (lo != null && lo.bounded() && !lo.inclusive()) ? '{' : '[';
        char r = (hi != null && hi.bounded() && !hi.inclusive()) ? '}' : ']';

        return maybeWrap(PREC_ATOM, ctx, l + loV + " TO " + hiV + r);
    }

    @Override
    public String visitRawValue(Expr.RawValue value, Context ctx) {
        if (value == null || value.isEmpty()) return "";
        return maybeWrap(PREC_ATOM, ctx, value.raw());
    }

    @Override
    public String visitSortField(Expr.SortField field, Context ctx) {
        if (field == null || field.isEmpty()) return "";
        return SolrEscaper.escapeField(field.fieldName()) + " " + field.direction().value();
    }

    @Override
    public String visitSort(Expr.Sort sort, Context ctx) {
        if (sort == null || sort.isEmpty()) return "";
        return sort.fields().stream()
                .filter(f -> f != null && !f.isEmpty())
                .map(f -> render(f, PREC_ATOM, ctx))
                .filter(s -> !Validate.isBlank(s))
                .collect(Collectors.joining(","));
    }

    @Override
    public String visitFieldList(Expr.FieldList fl, Context ctx) {
        if(fl == null || fl.isEmpty()) return "";
        if(fl.includeAll()) {
            return fl.includeScore() ? "*,score" : "*";
        }
        String base = fl.fields().stream()
                .filter(f -> !Validate.isBlank(f))
                .map(SolrEscaper::escapeField)
                .collect(Collectors.joining(","));

        if(Validate.isBlank(base)) {
            return fl.includeScore() ? "score" : "";
        }
        return fl.includeScore() ? base + ",score" : base;
    }
}
