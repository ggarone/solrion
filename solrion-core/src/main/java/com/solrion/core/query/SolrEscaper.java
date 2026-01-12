package com.solrion.core.query;

import com.solrion.core.internal.Validate;
import java.util.Set;

public final class SolrEscaper {
  private static final Set<Character> RESERVED =
      Set.of(
          '+', '-', '!', '(', ')', '{', '}', '[', ']', '^', '"', '~', '*', '?', ';', ':', '\\',
          '/');

  private SolrEscaper() {}

  public static String escapeField(String field) {
    if (Validate.isBlank(field)) {
      return "";
    }
    return escape(field);
  }

  public static String escapeTerm(String value) {
    if (value == null) {
      return "";
    }
    return escape(value);
  }

  public static String escapePhrase(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  private static String escape(String s) {
    if (s == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder(s.length() + 8);
    for (char c : s.toCharArray()) {
      if (RESERVED.contains(c) || Character.isWhitespace(c)) {
        sb.append('\\');
      }
      sb.append(c);
    }
    return sb.toString();
  }
}
