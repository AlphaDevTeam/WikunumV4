package com.alphadevs.sales.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Service Implementation for Reporting
 */
@Service
@Transactional
public class JasperReportService {

    private final Logger log = LoggerFactory.getLogger(JasperReportService.class);

    private final StorageService storageService;

    @Autowired
    private ResourceLoader resourceLoader;

    public JasperReportService(StorageService storageService) {
        this.storageService = storageService;
    }


    public byte[] generateReport(String fileName, Map<String, Object> reportParameters , Collection<?> beanCollection, boolean isForcedCompileRequest) throws SQLException, JRException, IOException {

        JasperReport jasperReport = null;

        if(isForcedCompileRequest){
            jasperReport = compileAndSave(fileName);
        }else {
            if(storageService.isCompiledVersionAvailable(fileName)){
                jasperReport = (JasperReport) JRLoader.loadObject(storageService.loadCompiledReport(fileName));
            }else{
                jasperReport = compileAndSave(fileName);
            }
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, new JRBeanCollectionDataSource(beanCollection));
        // return the PDF in bytes
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] generateReport(String fileName ,Map<String, Object> reportParameters , Collection<?> beanCollection) throws SQLException, JRException, IOException {

        return generateReport(fileName,reportParameters,beanCollection,false);
    }

    public byte[] generateReport(String fileName ,Map<String, Object> reportParameters ) throws SQLException, JRException, IOException {

        return generateReport(fileName,reportParameters,new ArrayList<>());
    }

    public byte[] generateReport(String fileName) throws SQLException, JRException, IOException {

        return generateReport(fileName,new HashMap<String, Object>());
    }

    public byte[] generateReport(String fileName ,Collection<?> beanCollection) throws SQLException, JRException, IOException {

        return generateReport(fileName,new HashMap<String, Object>(),beanCollection);
    }


    private JasperReport compileAndSave(String fileName) throws JRException, IOException {
        JasperReport jasperReportGenerated = JasperCompileManager.compileReport(storageService.isDesignAvailable(fileName) ? storageService.loadDesign(fileName) : "classpath:reports/Test/barcode.jrxml");
        JRSaver.saveObject(jasperReportGenerated,storageService.loadCompiledReport(fileName));
        return jasperReportGenerated;
    }

}
