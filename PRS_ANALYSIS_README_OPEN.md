# Open Pull Requests Analysis for 2025

This directory contains analysis of **OPEN pull requests** submitted to the swagger-api/swagger-core repository since January 1, 2025.

## Files

### PRS_ANALYSIS_2025_OPEN.md ⭐
A comprehensive markdown report containing:
- Analysis of **25 OPEN PRs only**
- **Four different categorization approaches** for grouping PRs:
  1. **By PR Type** - Change type categorization (Bug Fix, Feature, Refactor, etc.)
  2. **By Functional Area** - Technical domain-based grouping
  3. **By Priority** - Impact and urgency-based prioritization
  4. **⭐ Combined: Functional Areas by Priority** - Merges approaches 2 and 3
- Detailed priority determination methodology for PRs
- Actionable recommendations for review priorities

### PRS_DATA_2025_OPEN.csv ⭐
A CSV file containing all **25 open PRs** with the following columns:
- PR Number
- Title
- Type (Bug Fix, Feature, Refactor, Test, Documentation, Chore, Other)
- Functional Areas (semicolon-separated list)
- Priority (High, Medium, Low)
- **Priority Score** (1-4, used for calculating averages)
- Author
- Created At
- URL

This CSV can be imported into Excel, Google Sheets, or other data analysis tools for custom filtering and visualization.

## Summary Statistics (OPEN PRS ONLY)

- **Total Open PRs:** 25
- **Analysis Date:** February 3, 2026
- **Date Range:** Since January 1, 2025

### By Type
- **Bug Fix:** 7 open
- **Feature:** 6 open
- **Refactor:** 1 open
- **Test:** 1 open
- **Other:** 10 open

### By Priority
- **High:** 21 open
- **Medium:** 3 open
- **Low:** 1 open

### Top Functional Areas (by priority)
1. **Jakarta Migration** - 5 PRs (Avg Priority: 4.00) - ALL HIGH PRIORITY
2. **Polymorphic Types** - 1 PR (Avg Priority: 4.00)
3. **Examples** - 4 PRs (Avg Priority: 4.00)
4. **Jackson** - 4 PRs (Avg Priority: 4.00) - includes Jackson 3 migration!
5. **Dependencies** - 3 PRs (Avg Priority: 4.00) - includes BOM module
6. **Annotation Processing** - 16 PRs (Avg Priority: 3.81)

## How Priorities Are Determined for PRs

### Priority Scoring System for Pull Requests

We use a **weighted scoring methodology** specific to PRs:

| Priority Level | Score | Criteria |
|---------------|-------|----------|
| **High** | 4 | Bug fixes, critical fixes, regressions |
| **Medium** | 3 | Features, refactors, improvements |
| **Low** | 2 | Tests, documentation |
| **Low** | 1 | Chores, build improvements |

**Average Priority Score** is calculated for each functional area to identify which areas have the most urgent PRs awaiting review.

## ⭐ Combined Approach: The Best Way to Prioritize Reviews

The **Combined Approach** merges functional areas (Approach 2) with priority levels (Approach 3) to answer:

**"Which functional area should we review first?"**

### How It Works

1. Each PR is assigned to one or more functional areas
2. Each PR receives a priority score (1-4)
3. For each functional area, we calculate:
   - Total PR count
   - Count by priority level (High, Medium, Low)
   - **Average priority score**
4. Areas are ranked by average priority score

### Example

| Functional Area | Total | Avg Score | High | Medium | Low |
|----------------|-------|-----------|------|--------|-----|
| Jakarta Migration | 5 | **4.00** | 5 | 0 | 0 |
| Jackson | 4 | **4.00** | 4 | 0 | 0 |

**Interpretation:** Jakarta Migration has 5 PRs, all high priority (4.00 avg), making it the top priority for review.

## How to Use

### For Quick Review Triage
Use the **Combined Approach** table in `PRS_ANALYSIS_2025_OPEN.md`:
1. Find the functional area with the highest average priority score
2. Within that area, review High priority PRs first
3. Move to the next functional area when done

### For Team Assignment
1. Assign specialized reviewers to high-priority functional areas
2. Examples:
   - Jakarta expert → Jakarta Migration (5 PRs, all high priority)
   - Jackson expert → Jackson (4 PRs, 4.00 avg, includes Jackson 3!)
   - Annotation expert → Annotation Processing (16 PRs, 3.81 avg)

### For Review Planning
1. Choose top 2-3 functional areas by average priority score
2. Schedule review sessions for High priority PRs
3. Plan capacity accordingly
4. Track weekly to monitor backlog

### For Custom Analysis
Use **PRS_DATA_2025_OPEN.csv** to:
- Create pivot tables by Priority + Functional Area
- Filter to specific combinations (e.g., "High + Jakarta Migration")
- Generate charts showing priority distribution
- Track review progress over time (re-run weekly)

## Recommendations

Based on the combined priority analysis:

### 🟠 Immediate Review Focus
1. **Jakarta Migration** (4.00 avg) - 5 PRs, all critical for ecosystem
2. **Jackson** (4.00 avg) - 4 PRs, includes Jackson 3 migration
3. **Examples** (4.00 avg) - 4 PRs, all bug fixes
4. **Dependencies** (4.00 avg) - 3 PRs, includes BOM module
5. **OAS 3.1** (4.00 avg) - 4 PRs, spec compliance fixes

### 🟡 Standard Priority
6. **Annotation Processing** (3.81 avg) - 16 PRs, highest volume
7. **Validation** (3.70 avg) - 10 PRs, 8 high priority

### Strategy
- Focus on areas with 4.00 avg score first (5 areas with perfect scores!)
- Within each area, prioritize: High → Medium → Low
- Re-run analysis weekly to track progress
- Cross-reference with open issues to prioritize PRs that close critical issues

## Key Differences from Issues Analysis

| Aspect | Issues | PRs |
|--------|--------|-----|
| Total (2025) | 92 open | 25 open |
| Bug/Fix % | 17% | 28% |
| Priority Distribution | More varied | Concentrated (84% high) |
| Top Area | Validation (3.64) | 5-way tie (4.00) |
| Review Action | Triage & assign | Review & merge |

**Key Insight:** PRs are more action-oriented (bug fixes) while issues are more exploratory. Many PRs likely address open issues.

## Alignment with Issues

Cross-reference PRs with issues to:
- **Prioritize PRs closing critical issues** - Review PRs that resolve high-priority open issues first
- **Identify coverage gaps** - High-priority issues without PRs need attention
- **Track functional area progress** - Monitor both issue count and PR review rate

## Analysis Methodology

1. **Data Collection:** GitHub API query for PRs since 2025-01-01 with state=open
2. **Categorization:** Keyword matching on titles and descriptions
3. **Priority Scoring:** Based on PR type (bug fix = high, feature = medium, etc.)
4. **Calculation:** Average priority scores per functional area
5. **Ranking:** Sort functional areas by average priority score

## Quick Start

**For immediate action:**
1. Open `PRS_ANALYSIS_2025_OPEN.md`
2. Jump to "Combined Approach" section
3. See which functional area is ranked #1
4. Review high priority PRs in that area
5. Start merging!

**Current Top Priority:** 5-way tie at 4.00 avg:
- Jakarta Migration (5 PRs)
- Polymorphic Types (1 PR)
- Examples (4 PRs)
- Jackson (4 PRs)
- Dependencies (3 PRs)

All areas have 100% high-priority PRs - focus on your area of expertise!

For questions or updates, please open an issue in the repository.
