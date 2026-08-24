#!/usr/bin/env python3
"""Generates a 'diamond' DAG of POJOs that mimics the JCR API's fan-out shape:
a small handful of classes (Node, NodeType, PropertyDefinition, ...) referenced
from many different, uniquely-named getters scattered across many other classes.

Two variants of an identical topology are produced:
  - "Unique": each edge into a given target class uses a name unique to that
    edge (as naturally happens when unrelated methods like getPrimaryItem(),
    getParent(), nextNode() all happen to return the same javax.jcr.Node type).
  - "Shared": the exact same topology, but every edge into a given target
    class uses one canonical name for that target, regardless of who declares it.

F = fan-out per layer, so class count stays O(F) per layer/variant but the
number of *distinct* (type, propertyName) resolution keys in the "Unique"
variant grows as O(F^depth).

Usage: gen_jcr_diamond.py <F> <outputDir> [midLayers]
Writes Leaves.java, RootUnique.java, RootShared.java into outputDir.
midLayers = number of intermediate layers between L1 and the leaves (default 1, i.e. just L2).
"""
import sys
import os

F = int(sys.argv[1]) if len(sys.argv) > 1 else 8
outdir = sys.argv[2] if len(sys.argv) > 2 else "."
MID_LAYERS = int(sys.argv[3]) if len(sys.argv) > 3 else 1

HEADER = "package io.swagger.v3.core.oas.models;\n\n"


def write(filename, content):
    with open(os.path.join(outdir, filename), "w") as f:
        f.write(HEADER)
        f.write(content)


# Leaf layer: shared between both variants (no outgoing edges), package-private.
leaves = []
for k in range(F):
    leaves.append("class Leaf%d {" % k)
    leaves.append("    private String name;")
    leaves.append("    private int index;")
    leaves.append("    public String getName() { return name; }")
    leaves.append("    public int getIndex() { return index; }")
    leaves.append("}")
    leaves.append("")
write("Leaves.java", "\n".join(leaves))

for variant, unique in (("Unique", True), ("Shared", False)):
    out = []

    def emit_layer(class_prefix, points_at_prefix, layer_index):
        # class_prefix<j> has F getters, one per target <points_at_prefix><k>.
        for j in range(F):
            cls = "%s%s%d" % (class_prefix, variant, j)
            out.append("class %s {" % cls)
            for k in range(F):
                getter = ("get%s%dFrom%d" % (points_at_prefix, k, j)) if unique \
                    else ("get%s%d" % (points_at_prefix, k))
                field = getter[3].lower() + getter[4:]
                target = "%s%s%d" % (points_at_prefix, variant, k) if points_at_prefix != "Leaf" else "Leaf%d" % k
                out.append("    private %s %s;" % (target, field))
            for k in range(F):
                getter = ("get%s%dFrom%d" % (points_at_prefix, k, j)) if unique \
                    else ("get%s%d" % (points_at_prefix, k))
                field = getter[3].lower() + getter[4:]
                target = "%s%s%d" % (points_at_prefix, variant, k) if points_at_prefix != "Leaf" else "Leaf%d" % k
                out.append("    public %s %s() { return %s; }" % (target, getter, field))
            out.append("}")
            out.append("")

    # Mid layers: M1 -> M2 -> ... -> M{MID_LAYERS} -> Leaf
    prev_prefix = "Leaf"
    for layer in range(MID_LAYERS, 0, -1):
        cur_prefix = "M%d" % layer
        emit_layer(cur_prefix, prev_prefix, layer)
        prev_prefix = cur_prefix

    # L1 layer: points at the outermost mid layer (or Leaf if MID_LAYERS == 0).
    emit_layer("L1", prev_prefix, 0)

    # Root layer: public (referenced from the timing test in another package).
    cls = "Root%s" % variant
    out.append("public class %s {" % cls)
    for i in range(F):
        getter = "getL1_%d" % i
        field = "l1_%d" % i
        out.append("    private L1%s%d %s;" % (variant, i, field))
    for i in range(F):
        getter = "getL1_%d" % i
        field = "l1_%d" % i
        out.append("    public L1%s%d %s() { return %s; }" % (variant, i, getter, field))
    out.append("}")
    out.append("")

    write("Root%s.java" % variant, "\n".join(out))

print("wrote Leaves.java, RootUnique.java, RootShared.java to %s (F=%d)" % (outdir, F))
