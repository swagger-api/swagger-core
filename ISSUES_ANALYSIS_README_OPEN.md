# Open Issues Analysis for 2025

This directory contains analysis of **OPEN GitHub issues** reported to the swagger-api/swagger-core repository since January 1, 2025.

## Files

### ISSUES_ANALYSIS_2025_OPEN.md ⭐ **NEW**
A comprehensive markdown report containing:
- Analysis of **92 OPEN issues only** (closed issues excluded)
- **Four different categorization approaches** for grouping issues:
  1. **By Issue Type** - Traditional categorization (Bug, Feature, Question, etc.)
  2. **By Functional Area** - Technical domain-based grouping
  3. **By Priority/Severity** - Impact and urgency-based prioritization
  4. **⭐ Combined: Functional Areas by Priority** - Merges approaches 2 and 3
- Detailed priority determination methodology
- Actionable recommendations based on combined analysis

### ISSUES_DATA_2025_OPEN.csv ⭐ **NEW**
A CSV file containing all **92 open issues** with the following columns:
- Issue Number
- Title
- Type (Bug, Feature, Question, Other)
- Functional Areas (semicolon-separated list)
- Priority (Critical, High, Medium, Low)
- **Priority Score** (1-5, used for calculating averages)
- Labels
- URL

This CSV can be imported into Excel, Google Sheets, or other data analysis tools for custom filtering and visualization.

## Summary Statistics (OPEN ISSUES ONLY)

- **Total Open Issues:** 92
- **Analysis Date:** February 3, 2026
- **Date Range:** Since January 1, 2025

### By Type
- **Bugs:** 16 open
- **Features:** 10 open
- **Questions:** 5 open
- **Other:** 61 open

### By Priority
- **Critical:** 9 open
- **High:** 27 open
- **Medium:** 46 open
- **Low:** 10 open

### Top Functional Areas (by priority)
1. **Validation** - 25 issues (Avg Priority: 3.64)
2. **Jackson** - 17 issues (Avg Priority: 3.59)
3. **Dependencies** - 21 issues (Avg Priority: 3.57)
4. **Jakarta Migration** - 20 issues (Avg Priority: 3.55)
5. **Annotation Processing** - 63 issues (Avg Priority: 3.33)

## How Priorities Are Determined

### Priority Scoring System

We use a **weighted scoring methodology** that analyzes issue content and labels:

| Priority Level | Score | Criteria |
|---------------|-------|----------|
| **Critical** | 5 | Regressions, breaking changes, P0/P1 labels |
| **High** | 4 | Exceptions, crashes, bugs |
| **Medium** | 3 | Standard issues, general improvements |
| **Low** | 2 | Feature requests, enhancements |
| **Low** | 1 | Questions, documentation |

**Average Priority Score** is calculated for each functional area to identify which areas need the most urgent attention.

## ⭐ Combined Approach: The Best Way to Prioritize

The **Combined Approach** merges functional areas (Approach 2) with priority levels (Approach 3) to answer:

**"Which functional area should we focus on first?"**

### How It Works

1. Each issue is assigned to one or more functional areas
2. Each issue receives a priority score (1-5)
3. For each functional area, we calculate:
   - Total issue count
   - Count by priority level (Critical, High, Medium, Low)
   - **Average priority score**
4. Areas are ranked by average priority score

### Example

| Functional Area | Total | Avg Score | Critical | High | Medium | Low |
|----------------|-------|-----------|----------|------|--------|-----|
| Validation | 25 | **3.64** | 5 | 9 | 8 | 3 |
| Jackson | 17 | **3.59** | 2 | 7 | 7 | 1 |

**Interpretation:** Validation has more issues (25 vs 17) AND a higher average priority (3.64 vs 3.59), making it the top priority area.

## How to Use

### For Quick Triage
Use the **Combined Approach** table in `ISSUES_ANALYSIS_2025_OPEN.md`:
1. Find the functional area with the highest average priority score
2. Within that area, tackle Critical issues first, then High
3. Move to the next functional area when done

### For Team Assignment
1. Assign specialized team members to high-priority functional areas
2. Examples:
   - Annotation expert → Annotation Processing (63 issues, 3.33 avg)
   - Jakarta expert → Jakarta Migration (20 issues, 3.55 avg)
   - Jackson expert → Jackson (17 issues, 3.59 avg)

### For Sprint Planning
1. Choose top 2-3 functional areas by average priority score
2. Pull Critical and High priority issues from those areas
3. Plan capacity accordingly

### For Custom Analysis
Use **ISSUES_DATA_2025_OPEN.csv** to:
- Create pivot tables by Priority + Functional Area
- Filter to specific combinations (e.g., "Critical + Validation")
- Generate charts showing priority distribution
- Track trends over time (re-run monthly)

## Recommendations

Based on the combined priority analysis:

### 🔴 Immediate Focus
1. **Validation** (3.64 avg) - 5 critical issues need fixes
2. **Jackson** (3.59 avg) - 2 critical issues
3. **Dependencies** (3.57 avg) - 5 critical issues

### 🟠 High Priority
4. **Jakarta Migration** (3.55 avg) - Complete migration effort
5. **Annotation Processing** (3.33 avg) - Highest volume (63 issues)

### 🟡 Medium Priority
6. **Kotlin**, **Polymorphic Types**, **Examples** - Address after above

### Strategy
- Focus on top-ranked areas first
- Within each area, prioritize Critical → High → Medium → Low
- Re-run analysis monthly to track improvement

## Key Differences from Previous Analysis

| Previous (All Issues) | New (Open Only) |
|----------------------|-----------------|
| 155 total issues | 92 open issues |
| Included closed issues | Open issues only |
| 3 approaches | 4 approaches (added Combined) |
| Basic priority labels | Weighted priority scoring |
| No priority methodology | Detailed scoring system |
| Separate approaches | Combined Approach 2+3 |

## Analysis Methodology

1. **Data Collection:** GitHub API query for issues since 2025-01-01 with state=OPEN
2. **Categorization:** Keyword matching on titles and bodies
3. **Priority Scoring:** Multi-factor analysis (labels, keywords, issue type)
4. **Calculation:** Average priority scores per functional area
5. **Ranking:** Sort functional areas by average priority score

For questions or updates, please open an issue in the repository.
