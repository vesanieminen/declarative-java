# declarative-java
Make java behave more like declarative programming language when defining Vaadin UIs

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
Div contentDiv = new Div(
    new NativeLabel("""
        There is a tide in the affairs of men, Which taken at the flood, 
        leads on to fortune. Omitted, all the voyage of their life is 
        bound in shallows and in miseries. 
        On such a full sea are we now afloat. 
        And we must take the current when it serves, 
        or lose our ventures.
    """) {{
        addClassNames(LumoUtility.FontSize.MEDIUM);
    }},
    new NativeLabel(" -William Shakespeare") {{
        addClassNames(LumoUtility.FontWeight.BOLD);
    }}
);
```

Pros
- Flow of the code follows the structure of the UI
- No need to invent names to variables that are being only added to a layout shortly after construction

Cons
- Style somewhat unfamiliar in developer community
- Refactoring the hierarchy is manual work without refactoring tool help

Larger [example](src/main/java/org/samuliwritescode/declarativejava/MainRoute.java:L30)