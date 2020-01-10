package com.alphadevs.sales.service;

import com.alphadevs.sales.config.ApplicationProperties;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

/**
 * Service Implementation for Reporting
 */
@Service
@Transactional
public class FileServiceStorageService implements StorageService{

    private final Logger log = LoggerFactory.getLogger(FileServiceStorageService.class);

    private final Path rootPath;
    private final Path designPath;
    private final Path compiledPath;
    private final ApplicationProperties appProperties;


    public FileServiceStorageService(ApplicationProperties appProperties) throws IOException {

        this.appProperties = appProperties;
        this.rootPath = Paths.get(appProperties.getJasperReport().getRootPath().getURL().getPath());
        this.compiledPath = Paths.get(appProperties.getJasperReport().getDefaultCompiledPath().getURL().getPath());
        this.designPath = Paths.get(appProperties.getJasperReport().getDefaultDesignPath().getURL().getPath());
    }

    @Override
    public void initializeStorageService() {
        try {

            //Create Folders if not exists
            if(!Files.exists(rootPath)){
                Files.createDirectories(rootPath);
            }
            if(!Files.exists(designPath)){
                Files.createDirectories(designPath);
            }
            if(!Files.exists(compiledPath)){
                Files.createDirectories(compiledPath);
            }

        }catch (IOException e){
            throw new StorageException("initializeStorageService Error",e);
        }
    }

    @Override
    public void deleteFiles() {

    }

    @Override
    public boolean isDesignAvailable(String fileName) throws IOException {
        log.debug("isDesignAvailable");
        Path designFile = Paths.get(appProperties.getJasperReport().getDefaultDesignPath().getURI());
        log.debug("isDesignAvailable : " + appProperties.getJasperReport().getDefaultDesignPath());
        log.debug("isDesignAvailable : " + designFile);
        designFile = designFile.resolve(fileName + ".jrxml");
        log.debug("isDesignAvailable Actual : " + designFile);

        log.debug("isDesignAvailable Actual : " + Files.exists(designFile));
        return  Files.exists(designFile);
    }

    @Override
    public boolean isCompiledVersionAvailable(String fileName) throws IOException {
        Path compiledFile = Paths.get(appProperties.getJasperReport().getDefaultCompiledPath().getURI());
        compiledFile = compiledFile.resolve(fileName + ".jasper");
        return  Files.exists(compiledFile);
    }

    @Override
    public String loadDesign(String fileName) throws IOException {
        Path designFile = Paths.get(appProperties.getJasperReport().getDefaultDesignPath().getURI());
        designFile = designFile.resolve(fileName + ".jrxml");
        return  designFile.toString();
    }

    @Override
    public File loadCompiledReport(String fileName) throws IOException {
        Path compiledFile = Paths.get(appProperties.getJasperReport().getDefaultCompiledPath().getURI());
        compiledFile = compiledFile.resolve(fileName + ".jasper");
        return  compiledFile.toFile();
    }

    @Override
    public boolean isPatchAvailable(String fileName) {
        return false;
    }
}
