package com.pluralsight.conference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.Locale;

@Configuration
public class ConferenceConfig implements WebMvcConfigurer{

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("/WEB-INF/pdf/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    /*Configure viewResolver as a bean or from properties file
    we use the following properties :
    spring.mvc.view.prefix=/WEB-INF/jsp/
    spring.mvc.view.suffix=.jsp*/

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/jsp/");
        bean.setSuffix(".jsp");
        bean.setOrder(0);
        return bean;
    }

    /*a template Resolver is not the same as a view resolver when first looking at the temple it resolver it actually
    feels just like a view resolver that we've seen quite a few times in this course now. But this configuration and
    here is actually where to locate the template files and how they should be result. The view resolver which we will
    configure later, just sets the order and references this template resolver will use. Let's configure the template
    resolver in our project now and then we'll configure The view is over in a minute we need to configure a couple of
    beans for our application to resolve those template files. And I'm gonna do that inside of our conference config.
    So as I opened that up on a just scroll to the bottom and go below our view resolver But before our closing curly
    brace I'm gonna add in this template resolver to do so I want to go ahead and import all of these Resource is that
    I have you see we created an instance of a spring template Resolver just named the method template resolver and
    inside of there we have a spring resource template resolver we set the application context notice that still read.
    We're gonna fix that. And then we set the prefix of Web Dash I N F views and template resolver Suffolk's of dot html
     Now to fix that application context in a screw up to the top. Now, we haven't done this yet in our application, but
     it's really common to use an application context inside of our files. So I'm gonna import an annotation for at
     Auto Wired, and I'm going to create an instance of the application context for it to be auto wired into our
     application, and that's all I have to do. I am going to make that private just so that nothing else in the package
     utilizes it because we'll want Otto wire and inject that wherever we're going to use it. Now, if we scroll
     back down, that is purple instead of red, and the colors may not show up super good for you. But we've resolved
     that dependency. So we have our template resolver in here Now, now, in go configure a template engine that will
     utilize this resolver*/
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    /*We didn't have to create an engine of any sort when we were using jazz P pages. So this is a little bit unique to
     Time Leaf. We have to create a spring template engine that will process the pages and substitute in the model
     values from spring into our pages to be displayed. Noticed that this code makes a call to that template resolver
     method that we just created in the previous example. Let's go ahead and add this into our conference config file.
     Now the template engine is just another being and can actually go right below the template resolver that we just
     created. I'm gonna pace this coat in here and import it as well, and it stands alone on its own. It doesn't have
     a bunch of other references other than that call to the template resolver. So on Line 72 you can see where it's
     calling the temple that resolver that we just created online 61. We have this template engine that is configured
     just for spring, and it also has enabled the spring expression language compiler, the E l compiler, and that just
     makes it so we could use the short hand syntax of accessing spring variables and passing them in. That's it.
     That's all we have to add for the temperate resolver. Now we can add the actual view resolver foe, those templates
     that the controller can navigate things, too.*/
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /*the view Resolver is a little bit different because tthe e template resolver looked up the actual template
    The view resolver just takes whichever template was loaded and returns that based off the name So they kind of work
    in conjunction. I will tell you now, though, that the view resolver dot set order that*/
    @Bean
    public ViewResolver thymeleafResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        return viewResolver;
    }
}
