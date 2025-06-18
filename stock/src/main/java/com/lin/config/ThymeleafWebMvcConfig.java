package com.lin.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * @author CrunchDroid
 */
@Configuration
@EnableWebMvc
public class ThymeleafWebMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/stockIndex").setViewName("stockIndex");
        registry.addViewController("/hotSpotIndex").setViewName("hotSpotNews");
        registry.addViewController("/wallStreetNews").setViewName("wallStreetNews");
        registry.addViewController("/documentIndex").setViewName("documentIndex");
        registry.addViewController("/conceptAndIndex").setViewName("conceptAndIndex");
        registry.addViewController("/limitUpPool").setViewName("limitUpPool");
        registry.addViewController("/limitDownPool").setViewName("limitDownPool");
        registry.addViewController("/lianBanChi").setViewName("lianBanChi");
        registry.addViewController("/lianBan").setViewName("lianBan");

        registry.addViewController("/strongStock").setViewName("strongStock");
        registry.addViewController("/cloudPhoto").setViewName("cloudPhoto");
        registry.addViewController("/marketVolume").setViewName("marketVolume");
        registry.addViewController("/tigerAndDragon").setViewName("tigerAndDragon");
        registry.addViewController("/industryAnalyze").setViewName("industryAnalyze");
        registry.addViewController("/ratioRanking").setViewName("ratioRanking");
        registry.addViewController("/volumeTrend").setViewName("volumeTrend2");

        registry.addViewController("/allData").setViewName("downloadAllData");

        registry.addViewController("/stockConcept").setViewName("stockConcept");
        registry.addViewController("/stockBoard").setViewName("stockBoard");
        registry.addViewController("/stockMarketConceptBoard").setViewName("stockMarketConceptBoard");
        registry.addViewController("/popularStock").setViewName("popularStock");

        registry.addViewController("/yidong").setViewName("yidong");
        registry.addViewController("/zhaban").setViewName("zhaban");
        registry.addViewController("/stockBuyChange").setViewName("stockBuyChange");
        registry.addViewController("/stockSixtyDayChange").setViewName("stockSixtyDayChange");
        registry.addViewController("/rps").setViewName("rps");
        registry.addViewController("/rpsDetail").setViewName("rpsDetail");
        registry.addViewController("/stockStudy").setViewName("stockStudy");
        registry.addViewController("/stockStudyNew").setViewName("stockstudynew");
        registry.addViewController("/todayhot").setViewName("todayhot");
        registry.addViewController("/bigOrder").setViewName("bigorder");

        registry.addViewController("/fupanla").setViewName("fupanla");
        registry.addViewController("/increaseRank").setViewName("increaseRank");
        registry.addViewController("/decreaseRank").setViewName("decreaseRank");

        registry.addViewController("/zhangtingjiantu").setViewName("zhangtingjiantu");
        registry.addViewController("/trendRank").setViewName("trendRank");
        registry.addViewController("/top100Rank").setViewName("top100Rank");
        registry.addViewController("/boardandconcepts").setViewName("boardandconcepts");


    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public ITemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        try {
            resolver.setApplicationContext(applicationContext);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        return resolver;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // 解决静态资源无法访问的问题
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        // 解决升级swagger3.0.3后，swagger无法访问的问题
//        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(true);
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/resources/webjars/");
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/")
//                .resourceChain(true);
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
        super.addResourceHandlers(registry);

    }
}
