# Issues Analysis for 2025

This directory contains analysis of GitHub issues reported to the swagger-api/swagger-core repository since January 1, 2025.

## Files

### ISSUES_ANALYSIS_2025.md
A comprehensive markdown report containing:
- Executive summary of all issues
- **Three different categorization approaches** for grouping issues:
  1. **By Issue Type and Status** - Traditional categorization (Bug, Feature, Question, etc.)
  2. **By Functional Area** - Technical domain-based grouping (Annotations, Validation, Jackson Integration, etc.)
  3. **By Priority/Severity** - Impact and urgency-based prioritization (Critical, High, Medium, Low)
- Detailed breakdowns for each category
- Recommendations on when to use each approach

### ISSUES_DATA_2025.csv
A CSV file containing all issues with the following columns:
- Issue Number
- State (OPEN/CLOSED)
- Title
- Type (Bug, Feature, Question, Other)
- Functional Areas (semicolon-separated list)
- Priority (Critical/Regression, High, Medium, Low/Enhancement, Question)
- Labels
- URL

This CSV can be imported into Excel, Google Sheets, or other data analysis tools for custom filtering and visualization.

## Summary Statistics

- **Total Issues Analyzed:** 155
- **Open Issues:** 92
- **Closed Issues:** 63

### By Type
- **Bugs:** 31 (16 open, 15 closed)
- **Features:** 11 (10 open, 1 closed)
- **Questions:** 5 (5 open, 0 closed)
- **Other:** 108 (59 open, 47 closed)

### Top Functional Areas
1. **Annotation Processing** - 101 issues
2. **Validation & Required Fields** - 44 issues
3. **Dependencies/Build** - 84 issues
4. **Examples & Documentation** - 57 issues
5. **Jackson Integration** - 30 issues
6. **Polymorphic Types** - 18 issues

## How to Use

### For Project Management
Use **Approach 1 (By Type and Status)** from ISSUES_ANALYSIS_2025.md to:
- Plan sprints and balance work types
- Track closure rates by category
- Understand the health of the project

### For Team Assignment
Use **Approach 2 (By Functional Area)** to:
- Assign issues to team members with relevant expertise
- Identify areas requiring architectural attention
- Plan focused improvement initiatives

### For Prioritization
Use **Approach 3 (By Priority/Severity)** to:
- Decide what to work on next
- Plan releases and hotfixes
- Manage technical debt vs. new features

### For Custom Analysis
Use the **ISSUES_DATA_2025.csv** file to:
- Create custom pivot tables
- Generate charts and visualizations
- Perform your own filtering and grouping
- Integrate with project management tools

## Recommendations

Based on the analysis, here are key recommendations:

1. **Immediate Focus Areas:**
   - Address regressions (several critical issues identified)
   - Review annotation processing architecture (highest issue count)
   - Improve validation and required field handling (frequent pain point)

2. **Medium-Term Initiatives:**
   - Complete Jakarta migration
   - Improve Kotlin support
   - Enhance Jackson integration
   - Update dependencies (commons-lang3, Jackson 3.x)

3. **Long-Term Improvements:**
   - Better documentation for common questions
   - Feature requests (BOM module, Lombok support, etc.)
   - Community contributions for lower-priority enhancements

## Analysis Methodology

This analysis was performed on February 3, 2026, using the GitHub API to fetch all issues created or updated since January 1, 2025. Issues were categorized using keyword matching and label analysis.

For questions or updates to this analysis, please open an issue in the repository.
