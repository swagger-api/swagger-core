## Continuous integration

### Build, test and deploy
Swagger Core uses GitHub Actions to build, test and deploy snapshots on push and PR events.

Workflows in `.github/workflows`:

* `maven.yml` – Build, test and deploy `SNAPSHOT` artifacts from `master`.
* `maven-pulls.yml` – Build and test pull requests against `master`.

### Release

Releases are fully automated through a single manually triggered workflow.
The workflow supports three release types, chosen at launch time:

- **milestone** – pre‑release for early testing (e.g. `3.0.0-M1`)
- **rc** – release candidate (e.g. `3.0.0-RC1`)
- **release** – final stable release (e.g. `3.0.0`)

All releases start from a `-SNAPSHOT` version in the `master` branch.
The release commit is created in a detached HEAD and is never pushed to `master`;
the branch always remains on the current `-SNAPSHOT` version.

#### Workflow summary

1. Go to **Actions → Release → Run workflow**.
2. Select the release type (`milestone`, `rc`, `release`).
3. The workflow automatically:
    - Computes the next release version and whether it is a pre‑release.
    - Creates a temporary commit with the release version.
    - Builds, tests, and deploys artifacts to Maven Central and the Gradle Plugin Portal.
    - Pushes a Git tag and publishes a GitHub release with auto‑generated release notes.
    - Generates and publishes Javadocs to the `gh-pages` branch (versioned and `latest`).
4. After a **final release**, the `-SNAPSHOT` version in `master` must be bumped manually
   (e.g. `3.0.0` → `3.0.1-SNAPSHOT`).

#### Release notes logic

- For **milestone** and **rc** releases, the notes contain pull requests merged since the
  previous release of any type.
- For a **final release**, the notes contain all pull requests merged since the last stable
  release, providing a complete changelog.

#### Key scripts

| Script | Purpose |
|--------|---------|
| `CI/compute-release-version.sh` | Determines the release version, whether it is a pre‑release, and exports the last stable release tag. |
| `CI/prepare-release-commit.sh` | Creates the release commit: updates version references, generates release notes, commits. |
| `CI/releaseNotes.py` | Collects pull requests merged after a given release date and creates a draft GitHub release. |
| `CI/lastRelease.py` | Returns the latest release tag (excluding drafts). Use argument `stable` to ignore pre‑releases. |
| `CI/publishRelease.py` | Publishes the draft release. |
| `CI/prepare-javadocs.sh` | Copies generated Javadocs to a temporary location for later publication. |
| `CI/publish-javadocs.sh` | Publishes Javadocs to `gh-pages` (versioned folder and a `latest` redirect). |

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