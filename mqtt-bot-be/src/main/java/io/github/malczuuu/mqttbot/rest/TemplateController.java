package io.github.malczuuu.mqttbot.rest;

import io.github.malczuuu.mqttbot.application.model.ContentModel;
import io.github.malczuuu.mqttbot.application.template.TemplateCreateModel;
import io.github.malczuuu.mqttbot.application.template.TemplateModel;
import io.github.malczuuu.mqttbot.application.template.TemplateService;
import io.github.malczuuu.mqttbot.application.template.TemplateUpdateModel;
import io.github.malczuuu.mqttbot.rest.exception.TemplateNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/brokers/{brokerId}/templates")
@Tag(name = "messages")
public class TemplateController {

  private final TemplateService templateService;

  public TemplateController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ContentModel<TemplateModel> getTemplates(@PathVariable("brokerId") String brokerId) {
    return templateService.findAllTemplates(brokerId);
  }

  @GetMapping(path = "/{templateId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TemplateModel getTemplateById(
      @PathVariable("brokerId") String brokerId, @PathVariable("templateId") String templateId) {
    return templateService
        .findTemplate(brokerId, templateId)
        .orElseThrow(() -> new TemplateNotFoundException(templateId));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TemplateModel> createTemplate(
      @PathVariable("brokerId") String brokerId,
      @RequestBody @Valid TemplateCreateModel requestBody) {
    TemplateModel responseBody = templateService.createTemplate(brokerId, requestBody);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{messageId}")
            .buildAndExpand(responseBody.id())
            .toUri();
    return ResponseEntity.created(location).body(responseBody);
  }

  @PutMapping(
      path = "/{templateId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateTemplate(
      @PathVariable("brokerId") String brokerId,
      @PathVariable("templateId") String templateId,
      @RequestBody @Valid TemplateUpdateModel requestBody) {
    templateService
        .updateTemplate(brokerId, templateId, requestBody)
        .orElseThrow(() -> new TemplateNotFoundException(templateId));
  }

  @DeleteMapping(path = "/{templateId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTemplate(
      @PathVariable("brokerId") String brokerId, @PathVariable("templateId") String templateId) {
    templateService.deleteTemplate(brokerId, templateId);
  }
}
