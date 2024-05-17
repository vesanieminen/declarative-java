package org.samuliwritescode.declarativejava;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Route("")
public class MainRoute extends Div {
    public MainRoute() {
        setSizeFull();
        Component content = createContentWithDeclarativeStyle();
        Component content2 = createContentWithImperativeStyle();
        content.getElement().getStyle().set("width", "100%");
        content.getElement().getStyle().set("height", "50%");
        content2.getElement().getStyle().set("width", "100%");
        content2.getElement().getStyle().set("height", "50%");
        add(content);
        add(content2);
    }

    private Component createContentWithDeclarativeStyle() {
        return new Div(
                new Div(
                        new Span(
                                new Div(
                                        new NativeLabel("There is a tide in the affairs of men, Which taken at the flood, leads on to fortune. Omitted, all the voyage of their life is bound in shallows and in miseries. On such a full sea are we now afloat. And we must take the current when it serves, or lose our ventures.") {{
                                            addClassNames(LumoUtility.FontSize.MEDIUM);
                                        }},
                                        new NativeLabel(" -William Shakespeare") {{
                                            addClassNames(LumoUtility.FontWeight.BOLD);
                                        }}
                                ),
                                new Button("Open in a dialog", VaadinIcon.CODE.create(), e -> new Dialog() {{
                                    add(new Button("", VaadinIcon.CLOSE.create(), e -> close()) {{
                                        addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);
                                    }});
                                    add(createContentWithDeclarativeStyle());
                                    open();
                                }}) {{
                                    addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                                }}
                        ) {{
                            addClassNames(
                                    LumoUtility.Display.FLEX,
                                    LumoUtility.Gap.MEDIUM,
                                    LumoUtility.FlexDirection.COLUMN,
                                    LumoUtility.AlignItems.CENTER
                            );
                        }}
                ) {{
                    addClassNames(
                            LumoUtility.Display.FLEX,
                            LumoUtility.JustifyContent.CENTER,
                            LumoUtility.AlignItems.CENTER,
                            LumoUtility.Padding.LARGE,
                            LumoUtility.Margin.XLARGE,
                            LumoUtility.Border.ALL,
                            LumoUtility.BorderRadius.MEDIUM,
                            LumoUtility.BorderColor.CONTRAST_20,
                            LumoUtility.Background.CONTRAST_10,
                            LumoUtility.BoxShadow.SMALL
                    );
                }}
        ) {{
            addClassNames(
                    LumoUtility.Display.FLEX,
                    LumoUtility.JustifyContent.CENTER,
                    LumoUtility.AlignItems.CENTER,
                    LumoUtility.Background.CONTRAST_5
            );
        }};
    }

    private Component createContentWithImperativeStyle() {
        Button dialogOpenButton = new Button("Open in a dialog", VaadinIcon.CODE.create(), e -> {
            Dialog dialog = new Dialog();
            Button closeButton = new Button("", VaadinIcon.CLOSE.create(), e2 -> dialog.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);
            dialog.add(closeButton);
            dialog.add(createContentWithImperativeStyle());
            dialog.open();
        });

        dialogOpenButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        NativeLabel shakespeareLabel = new NativeLabel(" -William Shakespeare");
        shakespeareLabel.addClassNames(LumoUtility.FontWeight.BOLD);
        NativeLabel quoteLabel = new NativeLabel("There is a tide in the affairs of men, Which taken at the flood, leads on to fortune. Omitted, all the voyage of their life is bound in shallows and in miseries. On such a full sea are we now afloat. And we must take the current when it serves, or lose our ventures.");
        quoteLabel.addClassNames(LumoUtility.FontSize.MEDIUM);
        Div contentDiv = new Div(quoteLabel, shakespeareLabel);
        Span spanWithContent = new Span(contentDiv, dialogOpenButton);
        spanWithContent.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Gap.MEDIUM,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.AlignItems.CENTER
        );
        Div flexboxDiv = new Div(spanWithContent);
        flexboxDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.LARGE,
                LumoUtility.Margin.XLARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BorderColor.CONTRAST_20,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BoxShadow.SMALL
        );

        Div content = new Div(flexboxDiv);
        content.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Background.CONTRAST_5
        );
        return content;
    }

    @SpringBootApplication // <-- So that you may run this as a Spring Boot application
    public static class Application {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // <-- So that you may run this directly as a Java application
    }
}
