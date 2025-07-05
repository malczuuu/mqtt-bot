package io.github.malczuuu.mqttbot.domain;

import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "broker")
public class BrokerEntity implements Broker {

  @MongoId(targetType = FieldType.OBJECT_ID)
  private String id;

  @Field(name = "uid")
  private String uid;

  @Field(name = "server_uri")
  private String serverUri;

  @Field(name = "username")
  private String username;

  @Field(name = "password")
  private String password;

  @Field(name = "ssl_verification_enabled")
  private Boolean sslVerificationEnabled;

  @Version
  @Field(name = "version")
  private Long version;

  public BrokerEntity() {}

  public BrokerEntity(
      String uid,
      String serverUri,
      String username,
      String password,
      Boolean sslVerificationEnabled) {
    this(null, uid, serverUri, username, password, sslVerificationEnabled, null);
  }

  public BrokerEntity(
      String id,
      String uid,
      String serverUri,
      String username,
      String password,
      Boolean sslVerificationEnabled,
      Long version) {
    this.id = id;
    this.uid = uid;
    this.serverUri = serverUri;
    this.username = username;
    this.password = password;
    this.sslVerificationEnabled = sslVerificationEnabled;
    this.version = version;
  }

  public String getId() {
    return id;
  }

  public String getUid() {
    return uid;
  }

  @Override
  public String getServerUri() {
    return serverUri;
  }

  public void setServerUri(String serverUri) {
    this.serverUri = serverUri;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean isSslVerificationEnabled() {
    return sslVerificationEnabled == null || sslVerificationEnabled;
  }

  public void setSslVerificationEnabled(boolean sslVerificationEnabled) {
    this.sslVerificationEnabled = sslVerificationEnabled;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
