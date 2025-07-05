package io.github.malczuuu.mqttbot.domain;

import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

public class MessageEntity {

  @MongoId(targetType = FieldType.OBJECT_ID)
  private String id;

  @Field(name = "uid")
  private String uid;

  @Field(name = "broker_uid")
  private String brokerUid;

  @Field(name = "topic")
  private String topic;

  @Field(name = "body")
  private String body;

  @Field("message_time")
  private Long messageTime;

  @Field(name = "publish_time")
  private Long publishTime;

  @Field(name = "error")
  private String error;

  @Version
  @Field(name = "version")
  private Long version;

  public MessageEntity() {}

  public MessageEntity(String uid, String brokerUid, String topic, String body, Long messageTime) {
    this(null, uid, brokerUid, topic, body, messageTime, null, null, null);
  }

  public MessageEntity(
      String id,
      String uid,
      String brokerUid,
      String topic,
      String body,
      Long messageTime,
      Long publishTime,
      String error,
      Long version) {
    this.id = id;
    this.uid = uid;
    this.brokerUid = brokerUid;
    this.topic = topic;
    this.body = body;
    this.messageTime = messageTime;
    this.publishTime = publishTime;
    this.error = error;
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

  public String getTopic() {
    return topic;
  }

  public String getBody() {
    return body;
  }

  public Long getMessageTime() {
    return messageTime;
  }

  public Long getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Long publishTime) {
    this.publishTime = publishTime;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
