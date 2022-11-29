package com.alphadevs.sales.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.alphadevs.sales.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.alphadevs.sales.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.alphadevs.sales.domain.User.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Authority.class.getName());
            createCache(cm, com.alphadevs.sales.domain.User.class.getName() + ".authorities");
            createCache(cm, com.alphadevs.sales.domain.Products.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Model.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Job.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Job.class.getName() + ".details");
            createCache(cm, com.alphadevs.sales.domain.Job.class.getName() + ".assignedTos");
            createCache(cm, com.alphadevs.sales.domain.JobDetails.class.getName());
            createCache(cm, com.alphadevs.sales.domain.JobStatus.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Items.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Items.class.getName() + ".addons");
            createCache(cm, com.alphadevs.sales.domain.ItemAddOns.class.getName());
            createCache(cm, com.alphadevs.sales.domain.ItemAddOns.class.getName() + ".items");
            createCache(cm, com.alphadevs.sales.domain.ItemBinCard.class.getName());
            createCache(cm, com.alphadevs.sales.domain.PurchaseOrder.class.getName());
            createCache(cm, com.alphadevs.sales.domain.PurchaseOrder.class.getName() + ".details");
            createCache(cm, com.alphadevs.sales.domain.PurchaseOrderDetails.class.getName());
            createCache(cm, com.alphadevs.sales.domain.GoodsReceipt.class.getName());
            createCache(cm, com.alphadevs.sales.domain.GoodsReceipt.class.getName() + ".details");
            createCache(cm, com.alphadevs.sales.domain.GoodsReceipt.class.getName() + ".linkedPOs");
            createCache(cm, com.alphadevs.sales.domain.GoodsReceiptDetails.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Invoice.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Invoice.class.getName() + ".details");
            createCache(cm, com.alphadevs.sales.domain.InvoiceDetails.class.getName());
            createCache(cm, com.alphadevs.sales.domain.StorageBin.class.getName());
            createCache(cm, com.alphadevs.sales.domain.UnitOfMeasure.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Currency.class.getName());
            createCache(cm, com.alphadevs.sales.domain.DocumentNumberConfig.class.getName());
            createCache(cm, com.alphadevs.sales.domain.UserPermissions.class.getName());
            createCache(cm, com.alphadevs.sales.domain.UserPermissions.class.getName() + ".menuItems");
            createCache(cm, com.alphadevs.sales.domain.UserPermissions.class.getName() + ".users");
            createCache(cm, com.alphadevs.sales.domain.UserPermissions.class.getName() + ".userGroups");
            createCache(cm, com.alphadevs.sales.domain.UserGroup.class.getName());
            createCache(cm, com.alphadevs.sales.domain.UserGroup.class.getName() + ".userPermissions");
            createCache(cm, com.alphadevs.sales.domain.UserGroup.class.getName() + ".users");
            createCache(cm, com.alphadevs.sales.domain.MenuItems.class.getName());
            createCache(cm, com.alphadevs.sales.domain.MenuItems.class.getName() + ".userPermissions");
            createCache(cm, com.alphadevs.sales.domain.CashPaymentVoucherCustomer.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CashReceiptVoucherExpense.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CashPaymentVoucherExpense.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CashReceiptVoucherCustomer.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CashPaymentVoucherSupplier.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CashReceiptVoucherSupplier.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CashBook.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CashBookBalance.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CustomerAccount.class.getName());
            createCache(cm, com.alphadevs.sales.domain.CustomerAccountBalance.class.getName());
            createCache(cm, com.alphadevs.sales.domain.SupplierAccount.class.getName());
            createCache(cm, com.alphadevs.sales.domain.SupplierAccountBalance.class.getName());
            createCache(cm, com.alphadevs.sales.domain.PurchaseAccount.class.getName());
            createCache(cm, com.alphadevs.sales.domain.PurchaseAccountBalance.class.getName());
            createCache(cm, com.alphadevs.sales.domain.SalesAccount.class.getName());
            createCache(cm, com.alphadevs.sales.domain.SalesAccountBalance.class.getName());
            createCache(cm, com.alphadevs.sales.domain.ExpenseAccount.class.getName());
            createCache(cm, com.alphadevs.sales.domain.ExpenseAccountBalance.class.getName());
            createCache(cm, com.alphadevs.sales.domain.DocumentType.class.getName());
            createCache(cm, com.alphadevs.sales.domain.DocumentHistory.class.getName());
            createCache(cm, com.alphadevs.sales.domain.DocumentHistory.class.getName() + ".changeLogs");
            createCache(cm, com.alphadevs.sales.domain.ChangeLog.class.getName());
            createCache(cm, com.alphadevs.sales.domain.ChangeLog.class.getName() + ".documentHistories");
            createCache(cm, com.alphadevs.sales.domain.TransactionType.class.getName());
            createCache(cm, com.alphadevs.sales.domain.PaymentTypes.class.getName());
            createCache(cm, com.alphadevs.sales.domain.PaymentTypeAccount.class.getName());
            createCache(cm, com.alphadevs.sales.domain.PaymentTypeBalance.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Location.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Location.class.getName() + ".configitems");
            createCache(cm, com.alphadevs.sales.domain.Location.class.getName() + ".users");
            createCache(cm, com.alphadevs.sales.domain.Customer.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Supplier.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Expense.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Worker.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Worker.class.getName() + ".jobs");
            createCache(cm, com.alphadevs.sales.domain.ExUser.class.getName());
            createCache(cm, com.alphadevs.sales.domain.ExUser.class.getName() + ".locations");
            createCache(cm, com.alphadevs.sales.domain.ExUser.class.getName() + ".userGroups");
            createCache(cm, com.alphadevs.sales.domain.ExUser.class.getName() + ".userPermissions");
            createCache(cm, com.alphadevs.sales.domain.Stock.class.getName());
            createCache(cm, com.alphadevs.sales.domain.Company.class.getName());
            createCache(cm, com.alphadevs.sales.domain.ConfigurationItems.class.getName());
            createCache(cm, com.alphadevs.sales.domain.ConfigurationItems.class.getName() + ".locations");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
