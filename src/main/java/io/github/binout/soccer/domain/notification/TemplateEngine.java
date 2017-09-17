package io.github.binout.soccer.domain.notification;

import java.util.Map;

public interface TemplateEngine {
    
    String render(String templateName, Map<String, Object> params);
}
