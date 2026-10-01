## Continuous integration

This file describes the CI of the `3.0.0` branch (3.x line). The `master` branch (2.x line) and the `1.5` branch (1.x line)
have their own copies of the workflows and scripts.

### Build, test and deploy
Swagger Core uses Github actions to run jobs/checks building, testing and deploying snapshots on push and PR events.

These github actions are configured in `.github/workflows`:

* maven.yml : Build Test Deploy 3.0.0 (push to `3.0.0`, JDK 17, 21, 25; snapshot deploy on JDK 17 only)
* maven-pulls.yml : Build Test PR (PRs to `3.0.0`, JDK 17, 21, 25)
* codeql-analysis.yml : Code scanning - action (push and PRs to `3.0.0`, JDK 17)
* dependency-review.yml : Dependency Review (all PRs)
* prepare-release.yml : Prepare Release (manual)
* release.yml : Release (manual)

The workflow files have the same names on `master` and `3.0.0`. Branch triggers are set per branch in each file.
The scheduled CodeQL run uses the file of the default branch (`master`) only.

These actions use available actions in combination with short bash scripts.

### Release

Releases are semi-automated and consist in 2 actions using available public actions in combination with bash and python scripts.
**TODO**: Python code is used for historical reasons to execute GitHub APIs calls, in general a more consistent environment would
be more maintainable e.g. implementing a custom JavaScript or Docker Container GitHub Action and/or a bash only script(s).

A manual run uses the workflow file of the branch selected in the `Run workflow` dropdown. Checkouts and PR bases use
`github.ref_name`, so they point to the selected branch.

Each release workflow starts with a `Verify release line` step. The step stops the run unless the selected branch is `3.0.0`
and the pom version starts with `3.`. This prevents a 3.x release from the wrong branch.

The `CI/*` scripts on `3.0.0` are the 3.x copies of the `master` scripts:

* `lastRelease.py` finds the last published `v3*` release. If no `v3*` release exists (first 3.x release), it uses the
last published `v2*` release and writes a warning to stderr.
* `releaseNotes.py` lists PRs merged to `3.0.0` and creates the draft release with target `3.0.0`.
* `publishRelease.py` publishes the draft release with target `3.0.0`.

A 3.x release does not update the Wiki (the Wiki documents 2.x) and does not push changes to the `1.5` branch.

#### Workflow summary

1. execute `prepare-release.yml` / `Prepare Release` for `3.0.0` branch
1. check and merge the Prepare Release PR pushed by previous step. Delete the branch
1. execute `release.yml` / `Release` for `3.0.0` branch
1. check and merge the next snaphot PR pushed by previous step. Delete the branch

#### Prepare Release

The first action to execute is `prepare-release.yml` / `Prepare Release`.

This is triggered by manually executing the action, selecting `Actions` in project GitHub UI, then `Prepare Release` workflow,
selecting `3.0.0` in the branch dropdown and clicking `Run Workflow`.

`Prepare Release` takes care of:

* create release notes out of merged PRs
* Draft a release with related tag
* bump versions to release, and update all affected files
* build and test maven
* build and test gradle plugin
* push a Pull Request to `3.0.0` with the changes for human check.

After the PR checks complete, the PR can me merged, and the second phase `Release` started.

#### Release

Once prepare release PR has been merged, the second phase is provided by `release.yml` / `Release`.

This is triggered by manually executing the action, selecting `Actions` in project GitHub UI, then `Release` workflow,
selecting `3.0.0` in the branch dropdown and clicking `Run Workflow`.

`Release` takes care of:

* build and test maven
* build and test gradle plugin
* deploy/publish to maven central
* publish javadocs to gh-pages (`swagger-core/v<version>/apidocs`)
* deploy/publish gradle plugin
* publish the previously prepared GitHub release / tag
* push PR to `3.0.0` for next snapshot



### Secrets

GitHub Actions make use of `Secrets` which can be configured either with Repo or Organization scope; the needed secrets are the following:

* `APP_ID` and APP_PRIVATE_KEY`: these are the values provided by an account configured GitHub App, allowing to obtain a GitHub token
different from the default used in GitHub Actions (which does not allow to "chain" actions).Actions

The GitHub App must be configured as detailed in [this doc](https://github.com/peter-evans/create-pull-request/blob/master/docs/concepts-guidelines.md#authenticating-with-github-app-generated-tokens).

See also [here](https://github.com/peter-evans/create-pull-request/blob/master/docs/concepts-guidelines.md#triggering-further-workflow-runs)

* `OSSRH_GPG_PRIVATE_KEY` and `OSSRH_GPG_PRIVATE_PASSPHRASE` : gpg key and passphrase to be used for sonatype releases
GPG private key and passphrase defined to be used for sonatype deployments, as detailed in
https://central.sonatype.org/pages/working-with-pgp-signatures.html (I'd say with email matching the one  of the sonatype account of point 1

* `MAVEN_CENTRAL_USERNAME` and `MAVEN_CENTRAL_PASSWORD`: sonatype user/token

* `GRADLE_PUBLISH_KEY` and `GRADLE_PUBLISH_SECRET`: credentials for https://plugins.gradle.org/







