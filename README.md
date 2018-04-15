Proton
======

A flexible text editor for editing text.

Why?
----

When it comes to text editors, there seems to be no compromise between 
performance and power. IDEs like Visual Studio and IntelliJ offer 
incredible features, but can barely keep up on a good day and will 
completely freeze on a bad one. Text editors like Visual Studio Code 
and Emacs are extremely fast, but even simple autocompletion with
Gradle is difficult for them. Proton is an attempt to unify these two 
worlds.

How?
----

- Proton is purely functional and as lazy as possible. All data is 
immutable and persistent, and all side effects are returned as IO
monads to allow their callee to decide how to run them. All expensive
computations are done asynchronously, and any large amount of data is
provided as a lazily evaluated stream.
- Instead of operating on text buffers, Proton operates on an actual
AST. Instead of using incredibly complicated tricks to efficiently
parse, format, and refactor text, language plugins can operate on an
internal data structure which exactly matches their semantics.
