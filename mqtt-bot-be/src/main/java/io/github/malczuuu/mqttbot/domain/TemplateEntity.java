package io.github.malczuuu.mqttbot.domain;

import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "templates")
public class TemplateEntity {

  @MongoId(targetType = FieldType.OBJECT_ID)
  private String id;

  @Field(name = "uid")
  private String uid;

  @Field(name = "broker_uid")
  private String brokerUid;

  @Field(name = "name")
  private String name;

  @Field(name = "template")
  private String template;

  @Version
  @Field(name = "version")
  private Long version;

  public TemplateEntity() {}

  public TemplateEntity(String uid, String brokerUid, String name, String template) {
    this(null, uid, brokerUid, name, template, null);
  }

  public TemplateEntity(
      String id, String uid, String brokerUid, String name, String template, Long version) {
    this.id = id;
    this.uid = uid;
    this.brokerUid = brokerUid;
    this.name = name;
    this.template = template;
    this.version = version;
  }

  public String getId() {
    return id;
  }

  public String getUid() {
    return uid;
  }

  public String getBrokerUid() {
    return brokerUid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
