# Declarative Java

### Make Java behave more like declarative programming language when defining Vaadin UIs

Writing UIs for Vaadin Flow can easily lead into messy code when there are lots of component hierarchy being defined.
Java is an imperative programming language, while user interface definitions usually are more suited for declarative
programming languages. There are workarounds like using fluid API that for example Viritin add-on is doing, but this is
now added dependency. Also you could consider using some other JVM language, like Kotlin, but that is not vanilla Java
anymore.

If you are limited to plain Java, then you could consider less commonly used, but part of the language syntax of
anonymous class with initializer block: {{ ... }}

Do you prefer this

```java
NativeLabel quoteLabel = new NativeLabel("""
        There is a tide in the affairs of men, Which taken at the flood, 
        leads on to fortune. Omitted, all the voyage of their life is 
        bound in shallows and in miseries. 
        On such a full sea are we now afloat. 
        And we must take the current when it serves, 
        or lose our ventures.
        """);
quoteLabel.addClassNames(LumoUtility.FontSize.MEDIUM);
NativeLabel shakespeareLabel = new NativeLabel(" -William Shakespeare");
shakespeareLabel.addClassNames(LumoUtility.FontWeight.BOLD);
Div contentDiv = new Div(quoteLabel, shakespeareLabel);
```

or this

```java
Div contentDiv = new Div() {{
    add(new NativeLabel() {{
        setText("""
                There is a tide in the affairs of men, Which taken at the flood, 
                leads on to fortune. Omitted, all the voyage of their life is 
                bound in shallows and in miseries. 
                On such a full sea are we now afloat. 
                And we must take the current when it serves, 
                or lose our ventures.
                """);
        addClassNames(LumoUtility.FontSize.MEDIUM);
    }});
    add(new NativeLabel() {{
        setText(" -William Shakespeare");
        addClassNames(LumoUtility.FontWeight.BOLD);
    }});
}};
```

Setters are being moved inside the anonymous instantiation and thus not requiring referencing instances of NativeLabels
by variables, leading into call structure that is more align on resulting HTML hierarchy and with bonus of NativeLabel
instances kept off the variable scope.

### Pros

- Flow of the code follows the structure of the UI
- No need to invent names to variables that are being only added to a layout shortly after construction
- Less chance of accidentally calling a setter on wrong variable because they pollute the same scope

### Cons

- Style somewhat unfamiliar in developer community
- Refactoring the hierarchy is manual work without refactoring tool help
- Slight overhead on memory because of anonymous classes being instantiated

Larger [example](src/main/java/org/samuliwritescode/declarativejava/MainRoute.java#L30)

There are no absolute right or wrongs here and if you search the internet about this topic, it is going to be
opinionated. You can decide what is yours and use or not use this style. 

## Alternatives
### Fluent API
### Functional style