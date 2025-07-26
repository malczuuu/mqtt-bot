package io.github.malczuuu.mqttbot.application.template;

import io.github.malczuuu.mqttbot.application.model.ContentModel;
import io.github.malczuuu.mqttbot.domain.TemplateEntity;
import io.github.malczuuu.mqttbot.domain.TemplateRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {

  private final TemplateRepository templateRepository;

  public TemplateService(TemplateRepository templateRepository) {
    this.templateRepository = templateRepository;
  }

  public ContentModel<TemplateModel> findAllTemplates(String brokerId) {
    return new ContentModel<>(
        templateRepository.findAllByBrokerUid(brokerId).stream()
            .map(this::toTemplateModel)
            .toList());
  }

  public Optional<TemplateModel> findTemplate(String brokerId, String id) {
    return templateRepository.findAllByBrokerUidAndUid(brokerId, id).map(this::toTemplateModel);
  }

  public TemplateModel createTemplate(String brokerId, TemplateCreateModel template) {
    TemplateEntity entity =
        new TemplateEntity(
            UUID.randomUUID().toString(), brokerId, template.name(), template.template());
    entity = templateRepository.save(entity);
    return toTemplateModel(entity);
  }

  public Optional<TemplateModel> updateTemplate(
      String brokerId, String id, TemplateUpdateModel template) {
    Optional<TemplateEntity> optionalEntity =
        templateRepository.findAllByBrokerUidAndUid(brokerId, id);
    if (optionalEntity.isEmpty()) {
      return Optional.empty();
    }

    TemplateEntity entity = optionalEntity.get();
    entity.setName(template.name());
    entity.setTemplate(template.template());
    entity.setVersion(template.version());

    entity = templateRepository.save(entity);

    return Optional.of(toTemplateModel(entity));
  }

  public void deleteTemplate(String brokerId, String id) {
    templateRepository.deleteByBrokerUidAndUid(brokerId, id);
  }

  private TemplateModel toTemplateModel(TemplateEntity template) {
    return new TemplateModel(
        template.getUid(), template.getName(), template.getTemplate(), template.getVersion());
  }
}
