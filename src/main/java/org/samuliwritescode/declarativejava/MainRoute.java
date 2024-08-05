package org.samuliwritescode.declarativejava;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Route("")
@Theme("declarative")
public class MainRoute extends Div implements AppShellConfigurator {

    private Map<Integer, Entity> database;
    private ConfigurableFilterDataProvider<GridDTO, Void, GridFilter> dataProvider;
    private Binder<FormBean> binder;
    private Binder<GridFilter> filterBinder;

    static class GridFilter {
        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    record Entity(String name, String description) {

    }

    record GridDTO(Integer id, String name, String description) {
    }

    static class FormBean {
        private final Integer id;
        private String name;
        private String description;

        public FormBean(GridDTO dto) {
            this.id = dto.id();
            this.name = dto.name();
            this.description = dto.description();
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public MainRoute() {
        setSizeFull();
        database = new HashMap<>();
        IntStream.range(0, 1000).forEach(
                index -> database.put(index, new Entity(
                        "Shakespeare %d".formatted(index),
                        "For %d, there is a tide in the affairs of men, Which taken at the flood, leads on to fortune. Omitted, all the voyage of their life is bound in shallows and in miseries. On such a full sea are we now afloat. And we must take the current when it serves, or lose our ventures.".formatted(index)
                )));

        dataProvider = new CallbackDataProvider<GridDTO, GridFilter>(
                q -> database.entrySet().stream()
                        .filter(entry -> q.getFilter().map(filter -> entry.getValue().name().contains(filter.getName())).orElse(true))
                        .filter(entry -> q.getFilter().map(filter -> entry.getValue().description().contains(filter.getDescription())).orElse(true))
                        .skip(q.getOffset())
                        .limit(q.getLimit())
                        .map(entry -> new GridDTO(entry.getKey(), entry.getValue().name(), entry.getValue().description())),
                q -> Math.toIntExact(database.entrySet().stream()
                        .filter(entry -> q.getFilter().map(filter -> entry.getValue().name().contains(filter.getName())).orElse(true))
                        .filter(entry -> q.getFilter().map(filter -> entry.getValue().description().contains(filter.getDescription())).orElse(true))
                        .count())
        ).withConfigurableFilter();

        binder = new Binder<>(FormBean.class);
        filterBinder = new Binder<>(GridFilter.class);
        filterBinder.setBean(new GridFilter());
        filterBinder.addValueChangeListener(e -> dataProvider.refreshAll());
        dataProvider.setFilter(filterBinder.getBean());

        Component content = createContentWithDeclarativeStyle();
        Component content2 = createContentWithImperativeStyle();
        content.getElement().getStyle().set("width", "100%");
        content.getElement().getStyle().set("height", "50%");
        content2.getElement().getStyle().set("width", "100%");
        content2.getElement().getStyle().set("height", "50%");
        add(content);
        add(content2);

        binder.setBean(null);
    }

    private Div div(HasComponents parent, Consumer<Div> divConsumer) {
        final var div = new Div();
        parent.add(div);
        divConsumer.accept(div);
        return div;
    }

    private <T extends HtmlContainer> T add(HasComponents parent, Class<T> elementClass, Consumer<T> elementConsumer) {
        final T element;
        try {
            element = elementClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        elementConsumer.accept(element);
        parent.add(element);
        return element;
    }

    private Component createContentWithDeclarativeStyle() {
        div(this, divParent -> {
            divParent.setId("parent");
            div(divParent, div -> {
                div.setId("child 1");
            });
            div(divParent, div -> {
                div.setId("child 2");
            });
        });

        add(this, Div.class, div -> {
            div.addClassNames(LumoUtility.Display.FLEX);
            add(div, H1.class, h1 -> {
                h1.setText("Header 1");
                h1.addClassNames(
                        LumoUtility.TextColor.PRIMARY,
                        LumoUtility.FontWeight.EXTRABOLD
                );
            });
            add(div, Div.class, content -> {
                content.addClassNames(
                        LumoUtility.Display.FLEX,
                        LumoUtility.FlexDirection.COLUMN,
                        LumoUtility.Gap.LARGE
                );
                add(content, Span.class, span -> {
                    span.setText("Content");
                    span.addClassNames(LumoUtility.FontWeight.BOLD);
                });
            });
        });

        return new Div() {{
            add(new Div() {{
                add(new Div() {{
                    add(new Div() {{
                        setText("A simple CRUD editor instantiated with declarative manner");
                        addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
                    }});
                    add(new Div() {{
                        addClassNames(LumoUtility.Flex.GROW);
                    }});
                    add(new Button() {{
                        setText("New");
                        addClickListener(e -> onCreateBean());
                    }});
                    addClassNames(
                            LumoUtility.Display.FLEX,
                            LumoUtility.AlignContent.START
                    );
                }});
                add(new Grid<GridDTO>() {{
                    setDataProvider(dataProvider);
                    addClassNames(LumoUtility.Flex.GROW);
                    addColumn(d -> "").setHeader("").setWidth("0").setResizable(false).setFlexGrow(0).setHeaderPartName("column-selector-header").setHeader(getColumnSelector(this));
                    addColumn(GridDTO::id).setResizable(true).setFlexGrow(1).setHeader("ID");
                    addColumn(GridDTO::name).setResizable(true).setFlexGrow(1).setHeader(new TextField() {{
                        addThemeVariants(TextFieldVariant.LUMO_SMALL);
                        setLabel("Name");
                        filterBinder.forField(this).bind(GridFilter::getName, GridFilter::setName);
                    }});
                    addColumn(GridDTO::description).setResizable(true).setFlexGrow(1).setHeader(new TextField() {{
                        addThemeVariants(TextFieldVariant.LUMO_SMALL);
                        setLabel("Description");
                        filterBinder.forField(this).bind(GridFilter::getDescription, GridFilter::setDescription);
                    }});
                    binder.addStatusChangeListener(e -> onBinderStatusChanged(this));
                    addSelectionListener(MainRoute.this::onGridSelected);
                }});

                add(new Div() {{
                    add(new Div() {{
                        add(new Div() {{
                            add(new IntegerField() {{
                                setLabel("ID");
                                setReadOnly(true);
                                binder.forField(this).bind(FormBean::getId, (a, b) -> {
                                });
                            }});

                            add(new TextField() {{
                                setLabel("Name");
                                binder.forField(this).bind(FormBean::getName, FormBean::setName);
                            }});

                            addClassNames(
                                    LumoUtility.Display.FLEX,
                                    LumoUtility.FlexDirection.COLUMN,
                                    LumoUtility.JustifyContent.BETWEEN
                            );
                        }});

                        add(new TextArea() {{
                            addClassNames(LumoUtility.Flex.GROW);
                            setLabel("Description");
                            binder.forField(this).bind(FormBean::getDescription, FormBean::setDescription);
                        }});

                        addClassNames(
                                LumoUtility.Display.FLEX,
                                LumoUtility.Gap.MEDIUM,
                                LumoUtility.Flex.GROW
                        );
                    }});

                    add(new Div() {{
                        add(new Button() {{
                            setEnabled(false);
                            binder.addStatusChangeListener(e -> setEnabled(isCrudControlsEnabled()));
                            addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                            addClickListener(e -> onSaveFormBean());
                            setText("Save");
                        }});

                        add(new Button() {{
                            setEnabled(false);
                            binder.addStatusChangeListener(e -> setEnabled(isCrudControlsEnabled()));
                            addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                            addClickListener(e -> onClearFormBean());
                            setText("Cancel");
                        }});

                        add(new Div() {{
                            addClassNames(LumoUtility.Flex.GROW);
                        }});

                        add(new Button() {{
                            setEnabled(false);
                            binder.addStatusChangeListener(e -> setEnabled(isCrudControlsEnabled()));
                            addThemeVariants(ButtonVariant.LUMO_ERROR);
                            addClickListener(e -> onDeleteFormBean());
                            setText("Delete");
                        }});

                        addClassNames(
                                LumoUtility.Display.FLEX,
                                LumoUtility.Gap.LARGE
                        );
                    }});

                    addClassNames(
                            LumoUtility.Display.FLEX,
                            LumoUtility.Gap.MEDIUM,
                            LumoUtility.FlexDirection.COLUMN
                    );
                }});

                addClassNames(
                        LumoUtility.Display.FLEX,
                        LumoUtility.FlexDirection.COLUMN,
                        LumoUtility.Gap.MEDIUM,
                        LumoUtility.Flex.GROW,
                        LumoUtility.Padding.LARGE,
                        LumoUtility.Margin.XLARGE,
                        LumoUtility.Border.ALL,
                        LumoUtility.BorderRadius.MEDIUM,
                        LumoUtility.BorderColor.CONTRAST_20,
                        LumoUtility.Background.CONTRAST_10,
                        LumoUtility.BoxShadow.SMALL
                );
            }});

            addClassNames(
                    LumoUtility.Display.FLEX,
                    LumoUtility.Background.CONTRAST_5
            );
        }};
    }

    private void onCreateBean() {
        binder.setBean(new FormBean(new GridDTO(database.keySet().stream().mapToInt(id -> id).max().orElse(0) + 1, "", "")));
    }

    private void onBinderStatusChanged(Grid<GridDTO> grid) {
        if (binder.getBean() == null) {
            grid.deselectAll();
        }
    }

    private void onGridSelected(SelectionEvent<Grid<GridDTO>, GridDTO> e) {
        e.getFirstSelectedItem().ifPresentOrElse(dto -> binder.setBean(new FormBean(dto)), () -> binder.setBean(null));
    }

    private boolean isCrudControlsEnabled() {
        return binder.getBean() != null && binder.isValid();
    }

    private void onDeleteFormBean() {
        new ConfirmDialog() {{
            setHeader("Dangerous operation!");
            setText("Please confirm that you want to delete a row");
            setCancelable(true);
            addConfirmListener(e -> {
                FormBean bean = binder.getBean();
                database.remove(bean.getId());
                dataProvider.refreshAll();
                binder.setBean(null);
            });
        }}.open();
    }

    private void onClearFormBean() {
        binder.setBean(null);
    }

    private void onSaveFormBean() {
        FormBean bean = binder.getBean();
        database.remove(bean.getId());
        database.put(bean.getId(), new Entity(bean.getName(), bean.getDescription()));
        dataProvider.refreshAll();
        binder.setBean(null);
    }

    private Component getColumnSelector(final Grid<GridDTO> grid) {
        return new MenuBar() {
            {
                addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE, MenuBarVariant.LUMO_SMALL);
                addClassNames("column-selector-menu");
            }

            @Override
            protected void onAttach(AttachEvent attachEvent) {
                super.onAttach(attachEvent);
                var submenu = addItem(VaadinIcon.MENU.create()).getSubMenu();
                for (Grid.Column<GridDTO> gridColumn : grid.getColumns()) {
                    if (!(gridColumn.getHeaderComponent() instanceof MenuBar)) {
                        String headerText = gridColumn.getHeaderText();
                        if (gridColumn.getHeaderComponent() instanceof HasLabel hasLabel) {
                            headerText = hasLabel.getLabel();
                        }
                        var item = submenu.addItem(headerText);
                        item.setCheckable(true);
                        item.setChecked(gridColumn.isVisible());
                        item.addClickListener(e -> gridColumn.setVisible(item.isChecked()));
                    }
                }
            }
        };
    }

    private Component createContentWithImperativeStyle() {
        Div topLevelDiv = new Div();
        Div borderAndShadowDiv = new Div();
        Div headerDiv = new Div();
        Div headerTextDiv = new Div();
        headerTextDiv.setText("A simple CRUD editor instantiated with imperative manner");
        headerTextDiv.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
        headerDiv.add(headerTextDiv);
        Div headerSpacer = new Div();
        headerSpacer.addClassNames(LumoUtility.Flex.GROW);
        headerDiv.add(headerSpacer);
        Button newButton = new Button();
        newButton.setText("New");
        newButton.addClickListener(e -> onCreateBean());
        headerDiv.add(newButton);
        headerDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.AlignContent.START
        );

        borderAndShadowDiv.add(headerDiv);
        Grid<GridDTO> grid = new Grid<>();
        grid.setDataProvider(dataProvider);
        grid.addClassNames(LumoUtility.Flex.GROW);
        grid.addColumn(d -> "").setHeader("").setWidth("0").setResizable(false).setFlexGrow(0).setHeaderPartName("column-selector-header").setHeader(getColumnSelector(grid));
        grid.addColumn(GridDTO::id).setResizable(true).setFlexGrow(1).setHeader("ID");
        TextField headerFilterName = new TextField();
        headerFilterName.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        headerFilterName.setLabel("Name");
        filterBinder.forField(headerFilterName).bind(GridFilter::getName, GridFilter::setName);
        grid.addColumn(GridDTO::name).setResizable(true).setFlexGrow(1).setHeader(headerFilterName);
        TextField headerFilterDescription = new TextField();
        headerFilterDescription.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        headerFilterDescription.setLabel("Description");
        filterBinder.forField(headerFilterDescription).bind(GridFilter::getDescription, GridFilter::setDescription);
        grid.addColumn(GridDTO::description).setResizable(true).setFlexGrow(1).setHeader(headerFilterDescription);
        binder.addStatusChangeListener(e -> onBinderStatusChanged(grid));
        grid.addSelectionListener(MainRoute.this::onGridSelected);
        borderAndShadowDiv.add(grid);
        Div outerEditorDiv = new Div();
        Div fieldsDiv = new Div();
        Div idAndNameVerticalDiv = new Div();
        IntegerField idField = new IntegerField();
        idField.setLabel("ID");
        idField.setReadOnly(true);
        binder.forField(idField).bind(FormBean::getId, (a, b) -> {
        });
        idAndNameVerticalDiv.add(idField);

        TextField nameField = new TextField();
        nameField.setLabel("Name");
        binder.forField(nameField).bind(FormBean::getName, FormBean::setName);

        idAndNameVerticalDiv.add(nameField);

        idAndNameVerticalDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.JustifyContent.BETWEEN
        );

        fieldsDiv.add(idAndNameVerticalDiv);

        TextArea descriptionField = new TextArea();
        descriptionField.addClassNames(LumoUtility.Flex.GROW);
        descriptionField.setLabel("Description");
        binder.forField(descriptionField).bind(FormBean::getDescription, FormBean::setDescription);

        fieldsDiv.add(descriptionField);

        fieldsDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Gap.MEDIUM,
                LumoUtility.Flex.GROW
        );

        outerEditorDiv.add(fieldsDiv);

        Div buttonsDiv = new Div();
        Button saveButton = new Button();
        saveButton.setEnabled(false);
        binder.addStatusChangeListener(e -> saveButton.setEnabled(isCrudControlsEnabled()));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> onSaveFormBean());
        saveButton.setText("Save");

        buttonsDiv.add(saveButton);

        Button cancelButton = new Button();
        cancelButton.setEnabled(false);
        binder.addStatusChangeListener(e -> cancelButton.setEnabled(isCrudControlsEnabled()));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(e -> onClearFormBean());
        cancelButton.setText("Cancel");

        buttonsDiv.add(cancelButton);

        Div buttonSpacer = new Div();
        buttonSpacer.addClassNames(LumoUtility.Flex.GROW);
        buttonsDiv.add(buttonSpacer);

        Button deleteButton = new Button();
        deleteButton.setEnabled(false);
        binder.addStatusChangeListener(e -> deleteButton.setEnabled(isCrudControlsEnabled()));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> onDeleteFormBean());
        deleteButton.setText("Delete");

        buttonsDiv.add(deleteButton);

        buttonsDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Gap.LARGE
        );

        outerEditorDiv.add(buttonsDiv);

        outerEditorDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Gap.MEDIUM,
                LumoUtility.FlexDirection.COLUMN
        );

        borderAndShadowDiv.add(outerEditorDiv);

        borderAndShadowDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.MEDIUM,
                LumoUtility.Flex.GROW,
                LumoUtility.Padding.LARGE,
                LumoUtility.Margin.XLARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BorderColor.CONTRAST_20,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BoxShadow.SMALL
        );

        topLevelDiv.add(borderAndShadowDiv);
        topLevelDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.Background.CONTRAST_5
        );

        return topLevelDiv;
    }

    @SpringBootApplication // <-- So that you may run this as a Spring Boot application
    public static class Application {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // <-- So that you may run this directly as a Java application
    }
}
