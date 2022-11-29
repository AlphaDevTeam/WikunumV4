package com.alphadevs.sales.config;

import io.github.jhipster.config.JHipsterDefaults;
import io.github.jhipster.config.JHipsterProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Properties specific to Wikunum.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final JasperReport jasperReport = new JasperReport();

    public JasperReport getJasperReport() {
        return jasperReport;
    }

    public static class JasperReport {

        private boolean enabled = true;

        private Resource rootPath;

        private Resource defaultCompiledPath;

        private Resource defaultDesignPath;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Resource getDefaultCompiledPath() {
            return defaultCompiledPath;
        }

        public void setDefaultCompiledPath(Resource defaultCompiledPath) {
            this.defaultCompiledPath = defaultCompiledPath;
        }

        public Resource getDefaultDesignPath() {
            return defaultDesignPath;
        }

        public void setDefaultDesignPath(Resource defaultDesignPath) {
            this.defaultDesignPath = defaultDesignPath;
        }

        public Resource getRootPath() {
            return rootPath;
        }

        public void setRootPath(Resource rootPath) {
            this.rootPath = rootPath;
        }
    }
}
