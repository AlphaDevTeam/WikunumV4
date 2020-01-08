package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.Company;
import com.alphadevs.sales.repository.CompanyRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.jasperreports.JasperReportsUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    private final CompanyRepository companyRepository;
    private final Javers javers;

    public CompanyService(CompanyRepository companyRepository, Javers javers) {
        this.companyRepository = companyRepository;
        this.javers = javers;
    }

    /**
     * Save a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable);
    }


    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }

    public String getSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Company.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }

   public String getProductChanges(Long entityId) {
        Optional<Company> company = companyRepository.findById(entityId);
        QueryBuilder jqlQuery = QueryBuilder.byInstance(company.get());
        Changes changes = javers.findChanges(jqlQuery.build());
        return javers.getJsonConverter().toJson(changes);
    }

    public String getShadows(Long entityId) {
        Optional<Company> company = companyRepository.findById(entityId);
        JqlQuery jqlQuery = QueryBuilder.byInstance(company.get())
            .withChildValueObjects().build();
        List<Shadow<Company>> shadows = javers.findShadows(jqlQuery);
        return javers.getJsonConverter().toJson(shadows.get(0));
    }

    public byte[] exportPdfFileByte() throws SQLException, JRException, IOException {

        String path = resourceLoader.getResource("classpath:reports/Test/test.jrxml").getURI().getPath();

        JasperReport jasperReport = JasperCompileManager.compileReport(path);

        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();

        // Create an empty datasource.
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(companyRepository.findAll());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        // return the PDF in bytes
        OutputStream outputStream = new ByteArrayOutputStream();
        JasperReportsUtils.renderAsPdf(jasperReport,parameters,dataSource,outputStream);
        return JasperExportManager.exportReportToPdf(jasperPrint);

    }

    public byte[] exportPdfFileBytePrint() throws SQLException, JRException, IOException {

        String path = resourceLoader.getResource("classpath:reports/Test/test.jrxml").getURI().getPath();

        JasperReport jasperReport = JasperCompileManager.compileReport(path);

        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();

        // Create an empty datasource.
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(companyRepository.findAll());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        // return the PDF in bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperReportsUtils.renderAsPdf(jasperReport,parameters,dataSource,outputStream);
        return outputStream.toByteArray();

    }

    public byte[] exportPdfBarcode() throws SQLException, JRException, IOException {

        String path = resourceLoader.getResource("classpath:reports/Test/barcode.jrxml").getURI().getPath();

        JasperReport jasperReport = JasperCompileManager.compileReport(path);

        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();

        // Create an empty datasource.
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(companyRepository.findAll());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        // return the PDF in bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperReportsUtils.renderAsPdf(jasperReport,parameters,dataSource,outputStream);
        return outputStream.toByteArray();

    }

}
