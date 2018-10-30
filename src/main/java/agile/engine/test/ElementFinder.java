package agile.engine.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maksym Cherniak on 30.10.2018.
 */
public class ElementFinder {
    private static final String BUTTON_TAG = "a";
    private static final String BUTTON_CLASS = "btn btn-success";
    private static final String BUTTON_TITLE = "Make-Button";
    private static final String TITLE = "title";

    public static void main(String[] args) {
        String text = "everything ok";
        List<Document> docs = new ArrayList<>();
        Arrays.asList(args).forEach(arg -> {
            try {
                docs.add(Jsoup.connect(arg).get());
            } catch (Exception ex) {
                docs.forEach(document -> findElementById(document, arg));
                System.exit(0);
            }
        });
        docs.forEach(document -> findButtonElement(document, text));
    }

    public static void findButtonElement(Document doc, String text) {
        doc.getElementsByTag(BUTTON_TAG).stream().filter(element -> element.className().contains(BUTTON_CLASS) ||
                element.attr(TITLE).contains(BUTTON_TITLE))
                .filter(element -> element.hasText() && element.text().toLowerCase().contains(text))
                .findFirst()
                .ifPresent((element) -> printElement(element));
    }

    private static void findElementById(Document doc, String id) {
        Element result = doc.getElementById(id);
        if (result == null)
            System.out.println("Element with id: \'" + id + "\' not found. For path=" + doc.location());
        else
            printElement(result);
    }

    private static void printElement(Element result) {
        System.out.println();
        System.out.println("Path to the element for= " + result.ownerDocument().location());
        System.out.println(buildPath(result, new StringBuilder()));
        System.out.println();
    }

    private static StringBuilder buildPath(Element element, StringBuilder path) {
        if (!element.hasParent())
            return path;
        path.insert(0, " > " + element.tagName() + "(class=" + element.className() + ")");
        return buildPath(element.parent(), path);
    }
}
