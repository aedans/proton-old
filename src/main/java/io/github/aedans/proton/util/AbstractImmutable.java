package io.github.aedans.proton.util;

import org.immutables.value.Value;

@Value.Style(
        typeAbstract = "Abstract*",
        typeImmutable = "*"
)
public @interface AbstractImmutable {
}