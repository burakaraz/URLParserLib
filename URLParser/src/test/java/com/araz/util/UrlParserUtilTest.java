package com.araz.util;

import com.araz.model.UrlModel;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UrlParserUtilTest {

    @Test
    public void getSchemeFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertNotNull(urlModel.getProtocol());
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getUserAndPasswordFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertNotNull(urlModel.getAuthority().getUsername());
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getHostAndPortFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertNotNull(urlModel.getAuthority().getUsername());
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getHostAndPortIfUserAndPasswordAreAbsentInURL() throws MalformedURLException {
        //Given
        String url = "http://host:8090/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getHostIfUserAndPasswordAndPortAreAbsentInURL() throws MalformedURLException {
        //Given
        String url = "http://host/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityPartIfUserIsPresentPasswordIsAbsent() throws MalformedURLException {
        //Given
        String url = "http://user@host:8090/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityPartIfUserIsPresentWithColonPasswordIsAbsent() throws MalformedURLException {
        //Given
        String url = "http://user:@host:8090/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityPartIfHostIsPresentWithColonAndPortIsAbsent() throws MalformedURLException {
        //Given
        String url = "http://user:@host:/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityPartWhenPathIsAbsent() throws MalformedURLException {
        //Given
        String url = "http://user@host:8090?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("?query=123", urlModel.getQuery().getName());
        assertEquals("123", urlModel.getQuery().getQueryParameterMap().get("query"));
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityPartWhenQueryIsAbsent() throws MalformedURLException {
        //Given
        String url = "http://user@host:8090#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("#fragment", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityPartWhenTheRestIsAbsent() throws MalformedURLException {
        //Given
        String url = "http://user@host:8090";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getPathFromURL() throws MalformedURLException {
        //Given
        String url = "http://host/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("123", urlModel.getQuery().getQueryParameterMap().get("query"));
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getPathIfQueryIsAbsentInURL() throws MalformedURLException {
        //Given
        String url = "http://host/path#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getPathIfQueryAndFragmentAreAbsentInURL() throws MalformedURLException {
        //Given
        String url = "http://host/path";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getQueryFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?query=123#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?query=123", urlModel.getQuery().getName());
        assertEquals("123", urlModel.getQuery().getQueryParameterMap().get("query"));
        assertEquals("#fragment", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getQueryIfFragmentIsAbsentInURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?query=123";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?query=123", urlModel.getQuery().getName());
        assertEquals("123", urlModel.getQuery().getQueryParameterMap().get("query"));
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getQueryWhenValueIsAbsentFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?query#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?query", urlModel.getQuery().getName());
        assertEquals("", urlModel.getQuery().getQueryParameterMap().get("query"));
        assertEquals("#fragment", urlModel.getFragment().getName());
    }

    @Test
    public void getFragmentFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?query1=123&query2=1235#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?query1=123&query2=1235", urlModel.getQuery().getName());
        assertEquals("#fragment", urlModel.getFragment().getName());
        assertEquals("123", urlModel.getQuery().getQueryParameterMap().get("query1"));
        assertEquals("1235", urlModel.getQuery().getQueryParameterMap().get("query2"));
        assertEquals(url, urlModel.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void getExceptionWhenUndefinedProtocolIsPresent() throws MalformedURLException {
        //Given
        String url = "abcd:user.password";
        //When
        UrlParserUtil.parseUrl(url);
        //Then
    }

    @Test(expected = MalformedURLException.class)
    public void getExceptionWhenColonIsNotPresentInURL() throws MalformedURLException {
        //Given
        String url = "httpuser.password";
        //When
        UrlParserUtil.parseUrl(url);
        //Then
    }

    @Test
    public void getQueryWithNoParameterFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?#fragment";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("#fragment", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getFragmentWithNoFragmentValueAmdQueryValueFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?#";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("#", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getFragmentWithNoFragmentValueFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:password@host:8090/path?abc#";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("password", urlModel.getAuthority().getPassword());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?abc", urlModel.getQuery().getName());
        assertEquals("", urlModel.getQuery().getQueryParameterMap().get("abc"));
        assertEquals("#", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityWithUserAndColonButNoPasswordFromURL() throws MalformedURLException {
        //Given
        String url = "http://user:@host:8090/path?abc#";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("user", urlModel.getAuthority().getUsername());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?abc", urlModel.getQuery().getName());
        assertEquals("", urlModel.getQuery().getQueryParameterMap().get("abc"));
        assertEquals("#", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityWithAtSymbolButNoUserAndNoPasswordFromURL() throws MalformedURLException {
        //Given
        String url = "http://:@host:8090/path?abc#";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("host", urlModel.getAuthority().getHost());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?abc", urlModel.getQuery().getName());
        assertEquals("", urlModel.getQuery().getQueryParameterMap().get("abc"));
        assertEquals("#", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test
    public void getAuthorityWithNoHostDoubleColonFromURL() throws MalformedURLException {
        //Given
        String url = "http://:8090/path?abc#";
        //When
        UrlModel urlModel = UrlParserUtil.parseUrl(url);
        //Then
        assertEquals("http", urlModel.getProtocol().getName());
        assertEquals("8090", urlModel.getAuthority().getPort());
        assertEquals("/path", urlModel.getPath().getName());
        assertEquals("?abc", urlModel.getQuery().getName());
        assertEquals("", urlModel.getQuery().getQueryParameterMap().get("abc"));
        assertEquals("#", urlModel.getFragment().getName());
        assertEquals(url, urlModel.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void getExceptionWhenURLContainsNotNumericPortValue() throws MalformedURLException {
        //Given
        String url = "http://:port1/path?abc#";
        //When
        UrlParserUtil.parseUrl(url);
        //Then
    }
}
