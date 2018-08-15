package com.araz.model;

public class UrlModel {

    private Protocol protocol;
    private Authority authority;
    private Path path;
    private Query query;
    private Fragment fragment;

    public UrlModel() {
        protocol = new Protocol();
        authority = new Authority();
        path = new Path();
        query = new Query();
        fragment = new Fragment();
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public String toString() {
        return String.format("%s:%s%s%s%s", protocol, authority, path, query, fragment);
    }
}
