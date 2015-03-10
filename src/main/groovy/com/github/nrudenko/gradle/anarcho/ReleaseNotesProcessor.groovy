package com.github.nrudenko.gradle.anarcho


class ReleaseNotesProcessor {


    public static final String GIT_COMMIT = "\\{git_commit\\}"
    public static final String GIT_BRANCH = "\\{git_branch\\}"
    public static final String BUILD_TYPE = "\\{build_type\\}"

    final String buildType

    ReleaseNotesProcessor(String buildType) {
        this.buildType = buildType
    }

    public String process(String notesTemplate) {
        notesTemplate = processGit(notesTemplate)
        notesTemplate = processVariant(notesTemplate)
        return notesTemplate
    }

    public String processVariant(notes) {
        return notes.replaceAll(BUILD_TYPE, buildType);
    }

    /////////////////// GIT ////////////////////

    public String processGit(String notes) {
        notes = replaceShortLog(notes)
        notes = replaceBranch(notes)
        return notes;
    }

    private String replaceBranch(String notes) {
        List result = execute("rev-parse --abbrev-ref HEAD")
        if(result.isEmpty()){
            return notes.replaceAll(GIT_BRANCH, "no git branch");
        }else{
            return notes.replaceAll(GIT_BRANCH, result[0]);
        }
    }

    private String replaceShortLog(String notes) {
        List status = execute("status -s")
        if (status.isEmpty()) {
            List shortLog = execute("log --oneline -1")
            if (!shortLog.isEmpty()) {
                return notes.replaceAll(GIT_COMMIT, shortLog[0]);
            }
        }
        return notes.replaceAll(GIT_COMMIT, "UNCOMMITTED");
    }

    List<String> execute(String command) {
        List<String> output = new ArrayList<>()
        def process = "git ${command}".execute()
        output.addAll(process.in.readLines())
        return output
    }

}
