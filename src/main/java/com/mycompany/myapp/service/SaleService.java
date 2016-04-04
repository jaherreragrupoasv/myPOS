package com.mycompany.myapp.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.ByteBuffer;
import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.domain.SaleLine;
import com.mycompany.myapp.repository.SaleLineRepository;
import com.mycompany.myapp.repository.SaleRepository;
import com.mycompany.myapp.service.patterns.ITotalCalculator;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.inject.Inject;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;


/**
 * Service class for managing sales.
 */
@Service
@Transactional
public class SaleService  {

    private final Logger log = LoggerFactory.getLogger(SaleService.class);

    @Inject
    private SaleRepository saleRepository;

    @Inject
    private SaleLineRepository saleLineRepository;

    @Autowired
    private ITotalCalculator salesTaxDecorator;

    @Inject
    private SpringTemplateEngine templateEngine;


    public Sale saveSale(Sale sale){

        List<SaleLine> saleLines = sale.getSaleLines();
        sale.setSaleLines(null);

        Sale result = saleRepository.save(sale);

//        Grabo líneas
        for (SaleLine c : saleLines)
        {
            c.setSale_id(result.getId());
            SaleLine resultLine = saleLineRepository.save(c);
            result.getSaleLines().add(resultLine);
        }

        salesTaxDecorator.calculateTotal(result);

        return result;
    }

    public BigDecimal updateSalePaied(Long id){

        BigDecimal totalPaied = saleRepository.totalSalePaied(id);

        if (totalPaied == null) {totalPaied = new BigDecimal(0);}

        Sale sale = saleRepository.findOne(id);

        sale.setTotalPaied(totalPaied);

        saleRepository.save(sale);

        return totalPaied;
    }

    public void printSale(Long id) throws DocumentException, IOException {

        Locale locale = Locale.forLanguageTag("EN");

        Context ctx = new Context(locale);

        Sale sale = saleRepository.findOne(id);

        ctx.setVariable("sale", sale);
        ctx.setVariable("saleLines", sale.getSaleLines());

        String htmlContent = templateEngine.process("invoice", ctx);

        ByteOutputStream os = new ByteOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();

        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(os);

        byte[] pdfAsBytes = os.getBytes();
        os.close();

        String fileName = "sale_" + id + ".pdf";

        FileOutputStream fos = new FileOutputStream(new File(fileName));
        fos.write(pdfAsBytes);
        fos.close();

        PDDocument doc = PDDocument.load(new File(fileName));
        PDFPrintable printable = new PDFPrintable(doc, Scaling.SHRINK_TO_FIT);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(printable);

        try {
            job.print();
        }
        catch (Exception e) {
            log.error(e.toString());
        }
    }
}
