package es.daw01.savex.service;

import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class MarkdownService {
    private MutableDataSet options = new MutableDataSet()
            .set(HtmlRenderer.SOFT_BREAK, "<br />\n");

    private final Parser parser = Parser.builder(options)
            .extensions(List.of(YamlFrontMatterExtension.create()))
            .build();

    private final HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    /**
     * Renders a markdown input to HTML
     * @param markdown Markdown input to render
     * @return HTML output
     */
    public String renderMarkdown(String markdown) {
        Document document = parser.parse(markdown);
        return this.renderer.render(document);
    }

    /**
     * Extracts the YAML front matter from a markdown input
     * @param markdown Markdown input to extract from
     * @return YAML front matter as a Map
    */
    public Map<String, List<String>> extractYamlFrontMatter(String markdown) {
        Map<String, List<String>> yamlFrontMatter = new HashMap<>();

        // Split the markdown content into sections
        String[] sections = markdown.split("---");

        if (sections.length > 1) {
            String yamlSection = sections[1].trim();

            // Split the YAML section into lines
            String[] lines = yamlSection.split("\n");
            for (String line : lines) {
                // Split each line into key and value
                String[] keyValue = line.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    List<String> value = List.of(keyValue[1].trim().split(","));
                    yamlFrontMatter.put(key, value);
                }
            }
        }

        return yamlFrontMatter;
    }

    /**
     * Extracts the markdown content from a markdown input
     * @param markdown Markdown input to extract from
     * @return Markdown content
    */
    public String extractMarkdownContent(String markdown) {
        String[] sections = markdown.split("---");
        if (sections.length > 1) {
            return sections[2].trim();
        }
        return markdown;
    }
}
