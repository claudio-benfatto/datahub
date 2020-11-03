package com.linkedin.common.urn;

import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AdevintaOrganisationUrn extends Urn {

  public static final String ENTITY_TYPE = "adevintaOrganisation";

  private static final Pattern URN_PATTERN = Pattern.compile("^" + URN_PREFIX + ENTITY_TYPE + ":([\\-\\w]+)$");

  private final String organisationNameEntity;

  public AdevintaOrganisationUrn(String organisationName) {
    super(ENTITY_TYPE, organisationName);
    this.organisationNameEntity = organisationName;
  }

  public String getOrganisationNameEntity() {
    return organisationNameEntity;
  }

  public static AdevintaOrganisationUrn createFromString(String rawUrn) throws URISyntaxException {
    String organisationName = new Urn(rawUrn).getContent();
    return new AdevintaOrganisationUrn(organisationName);
  }

  public static AdevintaOrganisationUrn createFromUrn(Urn urn) throws URISyntaxException {
    if (!ENTITY_TYPE.equals(urn.getEntityType())) {
      throw new URISyntaxException(urn.toString(), "Can't cast URN to AdevintaOrganisationUrn, not same ENTITY");
    }

    Matcher matcher = URN_PATTERN.matcher(urn.toString());
    if (matcher.find()) {
      return new AdevintaOrganisationUrn(matcher.group(1));
    } else {
      throw new URISyntaxException(urn.toString(), "AdevintaOrganisationUrn syntax error");
    }
  }

  public static AdevintaOrganisationUrn deserialize(String rawUrn) throws URISyntaxException {
    return createFromString(rawUrn);
  }

  static {
    Custom.registerCoercer(new DirectCoercer<AdevintaOrganisationUrn>() {
      public Object coerceInput(AdevintaOrganisationUrn object) throws ClassCastException {
        return object.toString();
      }

      public AdevintaOrganisationUrn coerceOutput(Object object) throws TemplateOutputCastException {
        try {
          return AdevintaOrganisationUrn.createFromString((String) object);
        } catch (URISyntaxException e) {
          throw new TemplateOutputCastException("Invalid URN syntax: " + e.getMessage(), e);
        }
      }
    }, AdevintaOrganisationUrn.class);
  }
}
