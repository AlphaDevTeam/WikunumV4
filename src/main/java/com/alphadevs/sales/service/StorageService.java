package com.alphadevs.sales.service;

import com.alphadevs.sales.repository.CashBookRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Interface for Storage
 */

public interface StorageService {

    void initializeStorageService();
    void deleteFiles();
    boolean isDesignAvailable(String fileName) throws IOException;
    boolean isCompiledVersionAvailable(String fileName) throws IOException;
    String loadDesign(String fileName) throws IOException;
    File loadCompiledReport(String fileName) throws IOException;
    boolean isPatchAvailable(String fileName);

}
