## Continuous integration

### Build, test and deploy
Swagger Core uses GitHub Actions to build, test and deploy snapshots on push and PR events.

Workflows in `.github/workflows`:

* `build.yml` – Builds and tests the project on **every push to `master`** and on **pull requests** against `master`.
    - On **push to `master`** and **Java 17**, it deploys `SNAPSHOT` artifacts to Maven Central.
    - The same workflow runs for both events, avoiding duplication.
* `release.yml` – Manually triggered workflow for releasing new versions (see details below).
* `codeql-analysis.yml` – Runs CodeQL security analysis on schedule and on PRs/pushes.
* `dependency-review.yml` – Checks pull requests for vulnerable dependencies.

### Release

Releases are fully automated through a single manually triggered workflow.
The workflow supports three release types, chosen at launch time:

- **milestone** – pre‑release for early testing (e.g. `3.0.0-M1`)
- **rc** – release candidate (e.g. `3.0.0-RC1`)
- **release** – final stable release (e.g. `3.0.0`)

All releases start from a `-SNAPSHOT` version in the `master` branch.
The release commit is created in a detached HEAD and is never pushed to `master`;
the branch always remains on the current `-SNAPSHOT` version **except** after a successful **final release**, where the version is automatically bumped to the next patch SNAPSHOT (e.g., `3.0.0` → `3.0.1-SNAPSHOT`) and pushed to `master`.

#### Workflow summary

1. Go to **Actions → Release → Run workflow**.
2. Select the release type (`milestone`, `rc`, `release`).
3. The workflow automatically:
    - Computes the next release version and whether it is a pre‑release.
    - Creates a temporary commit with the release version (updating all POMs, Gradle properties, README, and Java source references).
    - Builds, tests, and deploys artifacts to Maven Central and the Gradle Plugin Portal.
    - Pushes a Git tag (e.g., `v3.0.0`) and **creates a draft GitHub Release** with automatically generated release notes (using `gh release create --generate-notes`).
    - Generates and publishes Javadocs to the `gh-pages` branch (versioned folder and `latest` redirect).
    - Publishes the draft release.
4. **For a final release only:** after all steps succeed, the workflow automatically bumps the version on `master` to the next SNAPSHOT and pushes that commit.

#### Release notes logic

- For **milestone** and **rc** releases, the notes contain pull requests merged since the previous release of **any** type.
- For a **final release**, the notes include all pull requests merged since the last **stable** release (i.e., skipping pre‑releases).

This is achieved by using `gh release create` with the `--generate-notes` flag and the appropriate `--notes-start-tag` (computed via `gh release list`).

#### Key scripts

| Script | Purpose |
|--------|---------|
| `CI/compute-release-version.sh` | Determines the release version, whether it is a pre‑release, and exports the last stable release tag. |
| `CI/prepare-release-commit.sh` | Creates the release commit: updates version references in POMs, Gradle properties, README, and Java source files. Commits the changes in a detached HEAD. |
| `CI/bump-snapshot.sh` | Bumps the version on `master` to the next patch SNAPSHOT after a final release and pushes the commit. |
| `CI/prepare-javadocs.sh` | Copies generated Javadocs from the Maven build to a temporary location for later publication. |
| `CI/publish-javadocs.sh` | Publishes Javadocs to `gh-pages` (versioned folder and a `latest` redirect). |

All release‑related GitHub API interactions use the pre‑installed `gh` CLI, which is automatically authenticated with `GITHUB_TOKEN`.

#### Post‑release version bump (for final releases)

After a **final release** (type `release`), the workflow runs `CI/bump-snapshot.sh`. This script:
- Checks out `master` (if currently in detached HEAD).
- Computes the next patch version (e.g., `3.0.0` → `3.0.1-SNAPSHOT`).
- Updates all POMs and the BOM to the new SNAPSHOT.
- Commits and pushes the change to `master`.

This ensures that `master` always reflects the next development version and prevents accidental overwrites of the released tag.

### Secrets

The following secrets must be configured in the repository or organization:

| Secret | Description |
|--------|-------------|
| `MAVEN_CENTRAL_USERNAME` | Sonatype username / token |
| `MAVEN_CENTRAL_PASSWORD` | Sonatype password / token |
| `OSSRH_GPG_PRIVATE_KEY` | GPG private key for artifact signing |
| `OSSRH_GPG_PRIVATE_PASSPHRASE` | Passphrase for the GPG key |
| `GRADLE_PUBLISH_KEY` | Gradle Plugin Portal publish key |
| `GRADLE_PUBLISH_SECRET` | Gradle Plugin Portal publish secret |

The workflow also uses the automatically provided `GITHUB_TOKEN` – no manual configuration is required.