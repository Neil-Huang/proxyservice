package com.meow.proxy.extract.impl;

import com.meow.proxy.check.ProxyCheck;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.enums.CountryType;
import com.meow.proxy.enums.ProxyAnonymousType;
import com.meow.proxy.enums.ProxySite;
import com.meow.proxy.extract.Extractor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
@Component("xicidailiExtractor")
public class XicidailiExtractor implements Extractor {
    private final static Logger LOG = LoggerFactory.getLogger(XicidailiExtractor.class);

    @Override
    public List<Proxy> extract(String htmlContent) {
        List<Proxy> proxies = new ArrayList<Proxy>(100);
        Document document = Jsoup.parse(htmlContent);
        ProxyCheck proxyCheck = ProxyCheck.getInstance();
        if (document != null) {
            Elements elements = document.select("tr.odd");
            if (CollectionUtils.isNotEmpty(elements)) {
                for (Element element : elements) {
                    long beginTime = System.currentTimeMillis();
                    Element ipEle = element.getElementsByClass("country").first().nextElementSibling();
                    if (ipEle != null) {
                        try {
                            Element portELe = ipEle.nextElementSibling();
                            String ip = ipEle.text();
                            int port = Integer.parseInt(portELe.text());
                            boolean valid = proxyCheck.checkProxyBySocket(new HttpHost(ip, port), true);
                            if (valid) {
                                long end = System.currentTimeMillis();
                                Element areaEle = portELe.nextElementSibling();
                                Element anonymousEle = areaEle.nextElementSibling();
                                Element protocolEle = anonymousEle.nextElementSibling();

                                Proxy proxy = new Proxy();
                                proxy.setCountry(CountryType.china.getCountryName());
                                proxy.setIp(ip);
                                proxy.setPort(port);
                                proxy.setArea(areaEle.text());
                                proxy.setCheckStatus(1);
                                proxy.setAnonymousType(getAnonymousType(anonymousEle));
                                proxy.setProtocolType(protocolEle.text());
                                proxy.setSourceSite(ProxySite.xicidaili.getProxySiteName());
                                proxy.setCheckTime(beginTime);
                                proxy.setCrawlTime(beginTime);
                                proxy.setValidTime(1);
                                proxy.setLastSurviveTime(-1L);
                                proxy.setInvalidTime(-1L);
                                proxy.setValid(true);
                                proxy.setResponseTime(end - beginTime);
                                LOG.info("Valid proxy:" + proxy.toString());
                                proxies.add(proxy);
                            }
                        }catch (Exception e){
                            LOG.error("XicidailiExtractor error",e);
                        }
                    } else {
                        LOG.error("XicidailiExtractor can not extract anything..., please check.");
                    }
                }
            }
        }
        return proxies;
    }

    @Override
    public List<Proxy> extract(List<String> htmlContentList) {
        List<Proxy> proxies = new ArrayList<Proxy>(200);
        if (CollectionUtils.isNotEmpty(htmlContentList)) {
            for (String htmlContent : htmlContentList) {
                try {
                    proxies.addAll(extract(htmlContent));
                }catch (Exception e){
                    LOG.error("解析XiciHtml错误",e);
                }
            }
        }
        return proxies;
    }

    /**
     * 代理匿名类型清洗
     *
     * @param element
     * @return
     */
    private String getAnonymousType(Element element) {
        String text = element.text();
        if (StringUtils.isNoneBlank(text)) {
            switch (text) {
                case "高匿":
                    return ProxyAnonymousType.elite.getAnonymousType();
                case "透明":
                    return ProxyAnonymousType.transparent.getAnonymousType();
                default:
                    LOG.error("Can not verify the anonymousType of proxy from XiciDaili>>>:" + text);
            }
        }
        return text;
    }
}
