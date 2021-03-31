## Continuous integration

### Build, test and deploy
Swagger Core uses Github actions to run jobs/checks building, testing and deploying snapshots on push and PR events.

These github actions are configured in `.github/workflows`:

* maven.yml : Build Test Deploy master
* maven-pulls.yml Build Test PR
* maven-v1.yml : Build Test Deploy 1.5 (must exist in in `1.5` branch)
* maven-v1-pulls.yml Build Test PR 1.5 (must exist in in `1.5` branch)


These actions use available actions in combination with short bash scripts.

### Release

Releases are semi-automated and consist in 2 actions using available public actions in combination with bash and python scripts.
**TODO**: Python code is used for historical reasons to execute GitHub APIs calls, in general a more consistent environment would
be more maintainable e.g. implementing a custom JavaScript or Docker Container GitHub Action and/or a bash only script(s).

#### Workflow summary

1. execute `prepare-release.yml` / `Prepare Release` for `master` branch
1. check and merge the Prepare Release PR pushed by previous step. Delete the branch
1. execute `release.yml` / `Release` for `master` branch
1. check and merge the `1.5` branch Readme update PR pushed by previous step. Delete the branch
1. check and merge the next snaphot PR pushed by previous step. Delete the branch

#### Prepare Release

The first action to execute is `prepare-release.yml` / `Prepare Release` for master, and
`prepare-release-v1.yml` / `Prepare Release V1` for `1.5` branch.

This is triggered by manually executing the action, selecting `Actions` in project GitHub UI, then `Prepare Release` workflow
and clicking `Run Workflow` (or `Prepare Release V1` and selecting `1.5` in the dropdown)

`Prepare Release` takes care of:

* create release notes out of merged PRs
* Draft a release with related tag
* bump versions to release, and update all affected files
* build and test maven
* build and test gradle plugin
* push a Pull Request with the changes for human check.

After the PR checks complete, the PR can me merged, and the second phase `Release` started.

#### Release

Once prepare release PR has been merged, the second phase is provided by `release.yml` / `Release` actions for master, and
`release-v1.yml` / `Release V1` for `1.5` branch.

This is triggered by manually executing the action, selecting `Actions` in project GitHub UI, then `Release` workflow
and clicking `Run Workflow` (or `Release V1` and selecting `1.5` in the dropdown)

`Release` takes care of:

* build and test maven
* build and test gradle plugin
* deploy/publish to maven central
* publish javadocs to gh-pages
* deploy/publish gradle plugin
* publish the previously prepared GitHub release / tag
* push PR for next snapshot
* push PR for 1.5 Readme update with new v2 version
* update Wiki with javadocs links to new version



### Secrets

GitHub Actions make use of `Secrets` which can be configured either with Repo or Organization scope; the needed secrets are the following:

* `APP_ID` and APP_PRIVATE_KEY`: these are the values provided by an account configured GitHub App, allowing to obtain a GitHub token
different from the default used in GitHub Actions (which does not allow to "chain" actions).Actions

The GitHub App must be configured as detailed in [this doc](https://github.com/peter-evans/create-pull-request/blob/master/docs/concepts-guidelines.md#authenticating-with-github-app-generated-tokens).

See also [here](https://github.com/peter-evans/create-pull-request/blob/master/docs/concepts-guidelines.md#triggering-further-workflow-runs)

* `OSSRH_GPG_PRIVATE_KEY` and `OSSRH_GPG_PRIVATE_PASSPHRASE` : gpg key and passphrase to be used for sonatype releases
GPG private key and passphrase defined to be used for sonatype deployments, as detailed in
https://central.sonatype.org/pages/working-with-pgp-signatures.html (I'd say with email matching the one  of the sonatype account of point 1

* `OSSRH_USERNAME` and `OSSRH_TOKEN`: sonatype user/token

* `GRADLE_PUBLISH_KEY` and `GRADLE_PUBLISH_SECRET`: credentials for https://plugins.gradle.org/









