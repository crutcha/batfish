package org.batfish.representation.aws;

import static org.batfish.representation.aws.Utils.checkNonNull;

import com.google.common.collect.ImmutableList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.batfish.common.Warnings;
import org.batfish.datamodel.Configuration;

@JsonIgnoreProperties(ignoreUnknown = true)
@ParametersAreNonnullByDefault
public class DirectConnectGatewayAssociation implements AwsVpcEntity, Serializable {

  @Nonnull protected final String _directConnectGatewayId;

  @Nonnull protected final String _directConnectGatewayOwnerAccount;

  @Nonnull protected final String _associationState;

  @Nonnull protected final String _associationId;

  @Nonnull protected final List<String> _allowedPrefixes;

  @ParametersAreNonnullByDefault
  private static final class AllowedPrefix {
    @Nonnull private final String _cidr;

    @JsonCreator
    private static AllowedPrefix create(@Nullable @JsonProperty(JSON_KEY_CIDR) String cidr) {
      //checkArgument()
      // TODO: validations?
      return new AllowedPrefix(cidr);
    }

    private AllowedPrefix(String cidr) {
      _cidr = cidr;
    }

    @Nonnull
    String getCidr() { return _cidr; }
  }

  @JsonCreator
  private static DirectConnectGatewayAssociation create(
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_ID) String directConnectGatewayId,
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_OWNER_ACCOUNT) String directConnectGatewayOwnerAccount,
     @Nullable @JsonProperty(JSON_KEY_ASSOCIATION_STATE) String associationState,
     @Nullable @JsonProperty(JSON_KEY_ASSOCIATION_ID) String associationId,
     @Nullable @JsonProperty(JSON_KEY_ALLOWED_PREFIXES_TO_DIRECT_CONNECT_GATEWAY) List<AllowedPrefix> allowedPrefixes) {

    checkNonNull(directConnectGatewayId, JSON_KEY_DIRECT_CONNECT_GATEWAY_ID, "Direct Connect Gateway Association");
    checkNonNull(directConnectGatewayOwnerAccount, JSON_KEY_DIRECT_CONNECT_GATEWAY_OWNER_ACCOUNT, "Direct Connect Gateway Association");
    checkNonNull(associationState, JSON_KEY_ASSOCIATION_STATE, "Direct Connect Gateway Association");
    checkNonNull(associationId, JSON_KEY_ASSOCIATION_ID, "Direct Connect Gateway Association");
    checkNonNull(allowedPrefixes, JSON_KEY_ALLOWED_PREFIXES_TO_DIRECT_CONNECT_GATEWAY, "Direct Connect Gateway Association");

    return new DirectConnectGatewayAssociation(
        directConnectGatewayId,
        directConnectGatewayOwnerAccount,
        associationState,
        associationId,
        allowedPrefixes.stream().map(AllowedPrefix::getCidr).collect(ImmutableList.toImmutableList()));
  }

  public DirectConnectGatewayAssociation(
      String directConnectGatewayId,
      String directConnectGatewayOwnerAccount,
      String associationState,
      String associationId,
      List<String> allowedPrefixes) {
    _directConnectGatewayId = directConnectGatewayId;
    _directConnectGatewayOwnerAccount = directConnectGatewayOwnerAccount;
    _associationState = associationState;
    _associationId = associationId;
    _allowedPrefixes = allowedPrefixes;
  }

  @Nonnull
  @Override
  public String getId() {
    return _associationId;
  }

  Configuration toConfigurationNode(
      ConvertedConfiguration awsConfiguration, Region region, Warnings warnings) {
    // TODO: does this really need it's own device type??
    Configuration cfgNode = Utils.newAwsConfiguration(_associationId, "aws");
    return cfgNode;
  }
}
