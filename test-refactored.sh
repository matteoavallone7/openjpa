#!/bin/bash

REPO="$(pwd)"

# -----------------------------
# LRUMap
# -----------------------------
ORIGINAL_LRU="$REPO/openjpa-lib/src/main/java/org/apache/openjpa/lib/util/LRUMap.java"
VARIANTS_LRU="$REPO/refactoring/lru_map"

echo "===== LRUMap ====="

for variant in "$VARIANTS_LRU"/C*; do

    version=$(basename "$variant")
    variant_file="$variant/LRUMap.java"

    echo ""
    echo ">>> Testing LRUMap $version"

    cp "$ORIGINAL_LRU" "$ORIGINAL_LRU.backup"

    cp "$variant_file" "$ORIGINAL_LRU"

    mvn -pl openjpa-lib -Dcheckstyle.skip=true -Dtest=*Test,*_ESTest test

    if [ $? -eq 0 ]; then
        echo ">>> LRUMap $version: PASS"
    else
        echo ">>> LRUMap $version: FAIL"
    fi

    mv "$ORIGINAL_LRU.backup" "$ORIGINAL_LRU"
done


# -----------------------------
# Filters
# -----------------------------
ORIGINAL_FILTERS="$REPO/openjpa-kernel/src/main/java/org/apache/openjpa/kernel/Filters.java"
VARIANTS_FILTERS="$REPO/refactoring/filters"

echo ""
echo "===== Filters ====="

for variant in "$VARIANTS_FILTERS"/C*; do

    version=$(basename "$variant")
    variant_file="$variant/Filters.java"

    echo ""
    echo ">>> Testing Filters $version"

    cp "$ORIGINAL_FILTERS" "$ORIGINAL_FILTERS.backup"

    cp "$variant_file" "$ORIGINAL_FILTERS"

    mvn -pl openjpa-kernel -Dtest=FiltersTest test

    if [ $? -eq 0 ]; then
        echo ">>> Filters $version: PASS"
    else
        echo ">>> Filters $version: FAIL"
    fi

    mv "$ORIGINAL_FILTERS.backup" "$ORIGINAL_FILTERS"
done