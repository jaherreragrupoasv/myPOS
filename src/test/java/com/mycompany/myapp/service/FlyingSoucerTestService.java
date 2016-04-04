package com.mycompany.myapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.repository.SaleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
//import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class FlyingSoucerTestService {

    @Inject
    private SpringTemplateEngine templateEngine;

//    @Inject
//    SaleRepository saleRepository;

    @Test
    public void test() throws DocumentException, IOException {

////        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
////
////        templateResolver.setPrefix("META-INF/pdfTemplates/");
////        templateResolver.setSuffix(".html");
////        templateResolver.setTemplateMode("XHTML");
////        templateResolver.setCharacterEncoding("UTF-8");
//
////        TemplateEngine templateEngine = new TemplateEngine();
//
////        templateEngine.setTemplateResolver(templateResolver);
//
//        Locale locale = Locale.forLanguageTag("EN");
//
//        Context ctx = new Context(locale);
//
////        Sale sale = saleRepository.findOne(new Long(111));
////
////        ctx.setVariable("sale", sale);
//
//        String htmlContent = templateEngine.process("invoice", ctx);
//
//        ByteOutputStream os = new ByteOutputStream();
//        ITextRenderer renderer = new ITextRenderer();
//        ITextFontResolver fontResolver = renderer.getFontResolver();
//
////        ClassPathResource regular = new ClassPathResource("/META-INF/fonts/LiberationSerif-Regular.ttf");
////        fontResolver.addFont(regular.getURL().toString(), BaseFont.IDENTITY_H, true);
//
//        renderer.setDocumentFromString(htmlContent);
//        renderer.layout();
//        renderer.createPDF(os);
//
//        byte[] pdfAsBytes = os.getBytes();
//        os.close();
//
//        FileOutputStream fos = new FileOutputStream(new File("message.pdf"));
//        fos.write(pdfAsBytes);
//        fos.close();
    }
}
