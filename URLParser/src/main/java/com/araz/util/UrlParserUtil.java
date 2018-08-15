package com.araz.util;

import com.araz.constant.Constant;
import com.araz.model.Authority;
import com.araz.model.Fragment;
import com.araz.model.Path;
import com.araz.model.Query;
import com.araz.model.UrlModel;

import java.net.MalformedURLException;

public class UrlParserUtil {

    private UrlParserUtil() {

    }

    public static UrlModel parseUrl(String urlToBeParsed) throws MalformedURLException {
        UrlModel urlModel = new UrlModel();

        String restToBeParsed = fillModelWithProtocol(urlModel, urlToBeParsed);

        if (containsAuthorityPart(restToBeParsed)) {
            restToBeParsed = fillModelWithAuthorityPartOfUrl(urlModel, restToBeParsed);
        }

        restToBeParsed = fillModelWithPath(urlModel, restToBeParsed);

        if (restToBeParsed.contains(Constant.QUESTION_MARK_SYMBOL)) {
            restToBeParsed = fillModelWithQuery(urlModel, restToBeParsed);
        }

        if (restToBeParsed.contains(Constant.HASH_SYMBOL)) {
            fillModelWithFragment(urlModel, restToBeParsed);
        }

        return urlModel;
    }

    private static boolean containsAuthorityPart(String restToBeParsed) {
        return restToBeParsed.charAt(0) == Constant.SLASH_CHAR_SYMBOL
                && restToBeParsed.charAt(1) == Constant.SLASH_CHAR_SYMBOL;
    }

    private static String fillModelWithProtocol(UrlModel urlModel, String restToBeParsed) throws MalformedURLException {
        if (!restToBeParsed.contains(Constant.COLON_SYMBOL)) {
            throw new MalformedURLException();
        }
        int index = restToBeParsed.indexOf(':');
        String protocolInURL = restToBeParsed.substring(0, index);
        if (!Constant.PROTOCOLS.contains(protocolInURL)) {
            throw new MalformedURLException();
        }
        urlModel.getProtocol().setName(protocolInURL);
        return restToBeParsed.substring(index + 1);
    }

    private static String fillModelWithAuthorityPartOfUrl(UrlModel urlModel, String restToBeParsed)
            throws MalformedURLException {
        Authority authority = urlModel.getAuthority();
        String startFromAuthorityPart = restToBeParsed.substring(2);
        String authorityPart;
        authorityPart = getAuthorityPartOfURL(startFromAuthorityPart);
        authority.setAuthority("//" + authorityPart);

        if (authorityPart.contains(Constant.AT_SYMBOL)) {
            fillAuthorityWithUserAndPasswordAndHostAndPort(authority, authorityPart);
        } else {
            fillAuthorityWithHostAndPort(authority, authorityPart);
        }
        urlModel.setAuthority(authority);
        return getNextPartOfURL(startFromAuthorityPart);
    }

    private static String getNextPartOfURL(String startFromAuthorityPart) {
        String nextPartOfURL;
        if (startFromAuthorityPart.contains(Constant.SLASH_SYMBOL)) {
            nextPartOfURL = startFromAuthorityPart
                    .substring(startFromAuthorityPart.indexOf(Constant.SLASH_CHAR_SYMBOL));
        } else if (startFromAuthorityPart.contains(Constant.QUESTION_MARK_SYMBOL)) {
            nextPartOfURL = startFromAuthorityPart.substring(startFromAuthorityPart.indexOf(
                    Constant.QUESTION_MARK_CHAR_SYMBOL));
        } else if (startFromAuthorityPart.contains(Constant.HASH_SYMBOL)) {
            nextPartOfURL = startFromAuthorityPart.substring(startFromAuthorityPart.indexOf(Constant.HASH_CHAR_SYMBOL));
        } else {
            nextPartOfURL = "";
        }
        return nextPartOfURL;
    }

    private static String getAuthorityPartOfURL(String startFromAuthorityPart) {
        String authorityPart;
        if (startFromAuthorityPart.contains(Constant.SLASH_SYMBOL)) {
            authorityPart = startFromAuthorityPart.substring(0, startFromAuthorityPart.indexOf(
                    Constant.SLASH_CHAR_SYMBOL));
        } else if (startFromAuthorityPart.contains(Constant.QUESTION_MARK_SYMBOL)) {
            authorityPart = startFromAuthorityPart.substring(0, startFromAuthorityPart.indexOf(
                    Constant.QUESTION_MARK_CHAR_SYMBOL));
        } else if (startFromAuthorityPart.contains(Constant.HASH_SYMBOL)) {
            authorityPart = startFromAuthorityPart.substring(0, startFromAuthorityPart.indexOf(
                    Constant.HASH_CHAR_SYMBOL));
        } else {
            authorityPart = startFromAuthorityPart;
        }
        return authorityPart;
    }

    private static void fillAuthorityWithUserAndPasswordAndHostAndPort(Authority authority, String authorityPart)
            throws MalformedURLException {
        String[] splitAuthorityPart = authorityPart.split(Constant.AT_SYMBOL);
        if (splitAuthorityPart[0].contains(Constant.COLON_SYMBOL)) {
            String[] splitUserAndPassword = splitAuthorityPart[0].split(Constant.COLON_SYMBOL);
            if (splitUserAndPassword.length == 1) {
                authority.setUsername(splitUserAndPassword[0]);
            } else if (splitUserAndPassword.length == 2) {
                authority.setUsername(splitUserAndPassword[0]);
                authority.setPassword(splitUserAndPassword[1]);
            }
        } else {
            authority.setUsername(splitAuthorityPart[0]);
        }
        String hostAndPort = splitAuthorityPart[1];
        fillAuthorityWithHostAndPort(authority, hostAndPort);
    }

    private static void fillAuthorityWithHostAndPort(Authority authority, String hostAndPort)
            throws MalformedURLException {
        if (hostAndPort.contains(Constant.COLON_SYMBOL)) {
            String[] splitHostAndPort = hostAndPort.split(Constant.COLON_SYMBOL);
            if (splitHostAndPort.length > 1) {
                authority.setHost(splitHostAndPort[0]);
                setPortOrThrowMalFormedURLException(authority, splitHostAndPort[1]);
            } else {
                authority.setHost(splitHostAndPort[0]);
            }
        } else {
            authority.setHost(hostAndPort);
        }
    }

    private static void setPortOrThrowMalFormedURLException(Authority authority, String port)
            throws MalformedURLException {
        try {
            Integer.parseInt(port);
            authority.setPort(port);
        } catch (NumberFormatException ex) {
            throw new MalformedURLException(String.format("Port: \"%s\" should be numeric", port));
        }
    }

    private static String fillModelWithPath(UrlModel urlModel, String restToBeParsed) {
        Path path = urlModel.getPath();
        if (restToBeParsed.contains(Constant.QUESTION_MARK_SYMBOL)) {
            path.setName(restToBeParsed.substring(0, restToBeParsed.indexOf(Constant.QUESTION_MARK_CHAR_SYMBOL)));
            restToBeParsed = restToBeParsed.substring(restToBeParsed.indexOf(Constant.QUESTION_MARK_CHAR_SYMBOL));
        } else if (restToBeParsed.contains(Constant.HASH_SYMBOL)) {
            path.setName(restToBeParsed.substring(0, restToBeParsed.indexOf(Constant.HASH_CHAR_SYMBOL)));
            restToBeParsed = restToBeParsed.substring(restToBeParsed.indexOf(Constant.HASH_CHAR_SYMBOL));
        } else {
            path.setName(restToBeParsed);
            restToBeParsed = "";
        }
        urlModel.setPath(path);
        return restToBeParsed;
    }


    private static String fillModelWithQuery(UrlModel urlModel, String restToBeParsed) {
        Query query = urlModel.getQuery();
        String queryName = restToBeParsed.substring(1);
        if (queryName.contains(Constant.HASH_SYMBOL)) {
            String[] splitQueryAndFragment = queryName.split(Constant.HASH_SYMBOL);
            restToBeParsed = splitQueryAndFragmentThenFillModel(query, splitQueryAndFragment);
        } else {
            query.setName(Constant.QUESTION_MARK_SYMBOL + queryName);
            fillQueryMapParameter(query, queryName);
            restToBeParsed = "";
        }
        urlModel.setQuery(query);
        return restToBeParsed;
    }

    private static String splitQueryAndFragmentThenFillModel(Query query, String[] splitQueryAndFragment) {
        String restToBeParsed;
        query.setName(Constant.QUESTION_MARK_SYMBOL);
        if (splitQueryAndFragment.length == 0) {
            restToBeParsed = Constant.HASH_SYMBOL;
        } else if (splitQueryAndFragment.length == 1) {
            query.setName(Constant.QUESTION_MARK_SYMBOL + splitQueryAndFragment[0]);
            fillQueryMapParameter(query, splitQueryAndFragment[0]);
            restToBeParsed = Constant.HASH_SYMBOL;
        } else {
            query.setName(Constant.QUESTION_MARK_SYMBOL + splitQueryAndFragment[0]);
            if (splitQueryAndFragment[0].length() > 0) {
                fillQueryMapParameter(query, splitQueryAndFragment[0]);
            }
            restToBeParsed = Constant.HASH_SYMBOL + splitQueryAndFragment[1];
        }
        return restToBeParsed;
    }

    private static void fillQueryMapParameter(Query query, String queryName) {
        if (queryName.contains(Constant.AND_SYMBOL)) {
            String[] queries = queryName.split(Constant.AND_SYMBOL);
            for (String queryParam : queries) {
                fillQueryMapParameterWithKeyValue(query, queryParam);
            }
        } else {
            fillQueryMapParameterWithKeyValue(query, queryName);
        }
    }

    private static void fillQueryMapParameterWithKeyValue(Query query, String queryParam) {
        if (queryParam.contains(Constant.EQUAL_SYMBOL)) {
            String[] keyValueOfQuery = queryParam.split(Constant.EQUAL_SYMBOL);
            query.getQueryParameterMap().put(keyValueOfQuery[0], keyValueOfQuery[1]);
        } else {
            query.getQueryParameterMap().put(queryParam, "");
        }
    }

    private static void fillModelWithFragment(UrlModel urlModel, String restToBeParsed) {
        Fragment fragment = urlModel.getFragment();
        fragment.setName(Constant.HASH_SYMBOL);
        if (restToBeParsed.length() > 1) {
            fragment.setName(Constant.HASH_SYMBOL + restToBeParsed.substring(1));
            urlModel.setFragment(fragment);
        }
    }
}
