package com.linkedin.common.urn;

import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AdevintaGroupUrn extends Urn {

  public static final String ENTITY_TYPE = "adevintaGroup";

  private static final Pattern URN_PATTERN = Pattern.compile("^" + URN_PREFIX + ENTITY_TYPE + ":([\\-\\w]+)$");

  private final String groupNameEntity;

  public AdevintaGroupUrn(String groupName) {
    super(ENTITY_TYPE, groupName);
    this.groupNameEntity = groupName;
  }

  public String getGroupNameEntity() {
    return groupNameEntity;
  }

  public static AdevintaGroupUrn createFromString(String rawUrn) throws URISyntaxException {
    String groupName = new Urn(rawUrn).getContent();
    return new AdevintaGroupUrn(groupName);
  }

  public static AdevintaGroupUrn createFromUrn(Urn urn) throws URISyntaxException {
    if (!ENTITY_TYPE.equals(urn.getEntityType())) {
      throw new URISyntaxException(urn.toString(), "Can't cast URN to CorpGroupUrn, not same ENTITY");
    }

    Matcher matcher = URN_PATTERN.matcher(urn.toString());
    if (matcher.find()) {
      return new AdevintaGroupUrn(matcher.group(1));
    } else {
      throw new URISyntaxException(urn.toString(), "CorpGroupUrn syntax error");
    }
  }

  public static AdevintaGroupUrn deserialize(String rawUrn) throws URISyntaxException {
    return createFromString(rawUrn);
  }

  static {
    Custom.registerCoercer(new DirectCoercer<AdevintaGroupUrn>() {
      public Object coerceInput(AdevintaGroupUrn object) throws ClassCastException {
        return object.toString();
      }

      public AdevintaGroupUrn coerceOutput(Object object) throws TemplateOutputCastException {
        try {
          return AdevintaGroupUrn.createFromString((String) object);
        } catch (URISyntaxException e) {
          throw new TemplateOutputCastException("Invalid URN syntax: " + e.getMessage(), e);
        }
      }
    }, AdevintaGroupUrn.class);
  }
}
