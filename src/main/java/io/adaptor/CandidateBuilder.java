package io.adaptor;

import org.jline.reader.Candidate;

public class CandidateBuilder {

    private final String value;
    private String displ;
    private String group;
    private String descr;
    private String suffix;
    private String key;
    private boolean complete;
    private int sort;

    public CandidateBuilder(String value) {
        this.value = value;
        this.displ = value;
    }

    public CandidateBuilder displ(String s) {
        this.displ = s;
        return this;
    }

    public CandidateBuilder group(String s) {
        this.group = s;
        return this;
    }

    public CandidateBuilder suffix(String s) {
        this.suffix = s;
        return this;
    }

    public CandidateBuilder descr(String s) {
        this.descr = s;
        return this;
    }

    public CandidateBuilder key(String s) {
        this.key = s;
        return this;
    }

    public CandidateBuilder complete(boolean flag) {
        this.complete = flag;
        return this;
    }

    public CandidateBuilder sort(int s) {
        this.sort = s;
        return this;
    }

    public Candidate build() {
        return new Candidate(value, displ, group, descr, suffix, key, complete, sort);
    }
}
