#!/usr/bin/python

import sys

UNRELEASED_HEADING = "## [Unreleased]"


def closeUnreleasedSection(changelogPath, version, date):
    with open(changelogPath, "r") as f:
        lines = f.readlines()

    try:
        unreleasedIndex = next(i for i, l in enumerate(lines) if l.strip() == UNRELEASED_HEADING)
    except StopIteration:
        print("No '%s' heading found in %s, nothing to do." % (UNRELEASED_HEADING, changelogPath))
        return

    # collect the section content: everything after the heading up to the next "## " heading (or EOF)
    sectionEnd = len(lines)
    for i in range(unreleasedIndex + 1, len(lines)):
        if lines[i].startswith("## "):
            sectionEnd = i
            break

    sectionHasContent = any(l.strip() for l in lines[unreleasedIndex + 1:sectionEnd])

    if not sectionHasContent:
        print("'%s' section is empty, skipping changelog entry for %s." % (UNRELEASED_HEADING, version))
        return

    newHeading = "## [%s] - %s\n" % (version, date)
    lines[unreleasedIndex + 1:unreleasedIndex + 1] = ["\n", newHeading]

    with open(changelogPath, "w") as f:
        f.writelines(lines)

    print("Closed '%s' section under %s" % (UNRELEASED_HEADING, newHeading.strip()))


# main
def main(changelogPath, version, date):
    closeUnreleasedSection(changelogPath, version, date)


# here start main
main(sys.argv[1], sys.argv[2], sys.argv[3])
