package org.batfish.representation.aws;

import static org.batfish.representation.aws.Utils.checkNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.batfish.common.Warnings;
import org.batfish.datamodel.Configuration;
import org.batfish.datamodel.DeviceModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@ParametersAreNonnullByDefault
public class DirectConnectGateway implements AwsVpcEntity, Serializable {

  @Nonnull protected final String _id;

  @Nonnull protected final String _name;

  @Nonnull protected final long _amazonSideAsn;

  @Nonnull protected final String _ownerAccount;

  @Nonnull protected final String _state;

  @JsonCreator
  private static DirectConnectGateway create(
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_ID) String directConnectGatewayId,
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_NAME) String directConnectGatewayName,
     @Nullable @JsonProperty(JSON_KEY_AMAZON_SIDE_ASN) long amazonSideAsn,
     @Nullable @JsonProperty(JSON_KEY_OWNER_ACCOUNT) String ownerAccount,
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_STATE) String directConnectGatewayState) {
    checkNonNull(directConnectGatewayId, JSON_KEY_DIRECT_CONNECT_GATEWAY_ID, "Direct Connect Gateway");
    checkNonNull(directConnectGatewayName, JSON_KEY_DIRECT_CONNECT_GATEWAY_NAME, "Direct Connect Gateway");
    checkNonNull(amazonSideAsn, JSON_KEY_AMAZON_SIDE_ASN, "Direct Connect Gateway");
    checkNonNull(ownerAccount, JSON_KEY_OWNER_ACCOUNT, "Direct Connect Gateway");
    checkNonNull(directConnectGatewayState, JSON_KEY_DIRECT_CONNECT_GATEWAY_STATE, "Direct Connect Gateway");

    return new DirectConnectGateway(
        directConnectGatewayId,
        directConnectGatewayName,
        amazonSideAsn,
        ownerAccount,
        directConnectGatewayState);
  }

  public DirectConnectGateway(String id, String name, long amazonSideAsn, String ownerAccount, String state) {
    _id = id;
    _name = name;
    _amazonSideAsn = amazonSideAsn;
    _ownerAccount = ownerAccount;
    _state = state;
  }

  @Nonnull
  @Override
  public String getId() {
    return _id;
  }

  Configuration toConfigurationNode(
      ConvertedConfiguration awsConfiguration, Region region, Warnings warnings) {
    Configuration cfgNode = Utils.newAwsConfiguration(_id, "aws", DeviceModel.AWS_DIRECT_CONNECT_GATEWAY);
    return cfgNode;
  }
}
