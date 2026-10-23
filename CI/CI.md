## Continuous integration

This file describes the release CI of the `1.5` branch (swagger-core v1, `1.6.x`). The `master` branch (2.x line) and the
`3.0.0` branch (3.x line) have their own copies of the release workflows and scripts.

### Build, test and deploy
Github actions configured in `.github/workflows`:

* maven-v1.yml : Build Test Deploy 1.5 (push to `1.5`, snapshot deploy)
* maven-v1-pulls.yml : Build Test PR 1.5 (PRs to `1.5`)
* prepare-release.yml : Prepare Release (manual)
* release.yml : Release (manual)

### Release

The workflow files have the same names on `master`, `3.0.0` and `1.5`, so the manual workflows can be run from the
`Run workflow` dropdown by selecting the `1.5` branch. Both workflows stop unless the selected branch is `1.5` and the
pom version starts with `1.`.

Differences from the 2.x/3.x scripts:

* `lastRelease.py` finds the last published `v1.*` release (paging through all releases, v1 ones are far behind).
* `releaseNotes.py` lists PRs merged to `1.5` and creates the draft release with target `1.5`.
* `publishRelease.py` publishes the draft release with target `1.5`.
* no gradle plugin, BOM, changelog, javadocs or wiki steps. README: only the "currently X-SNAPSHOT" line is bumped to the next snapshot.

#### Workflow summary

1. execute `prepare-release.yml` / `Prepare Release` for `1.5` branch
1. check and merge the Prepare Release PR pushed by previous step. Delete the branch
1. execute `release.yml` / `Release` for `1.5` branch
1. check and merge the next snapshot PR pushed by previous step. Delete the branch

#### Prepare Release

`Prepare Release` takes care of:

* create release notes out of merged PRs
* draft a release with related tag (`v1.6.x`)
* bump versions to release
* build and test maven
* push a Pull Request to `1.5` with the changes for human check.

#### Release

`Release` takes care of:

* build and test maven
* deploy/publish to maven central
* publish the previously prepared GitHub release / tag
* push PR to `1.5` for next snapshot

### Secrets

* `APP_ID` and `APP_PRIVATE_KEY`: GitHub App credentials used to obtain a token for creating the PRs
* `OSSRH_GPG_PRIVATE_KEY` and `OSSRH_GPG_PRIVATE_PASSPHRASE`: gpg key and passphrase for signing artifacts
* `MAVEN_CENTRAL_USERNAME` and `MAVEN_CENTRAL_PASSWORD`: Maven Central user/token
