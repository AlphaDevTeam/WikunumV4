import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'products',
        loadChildren: () => import('./products/products.module').then(m => m.WikunumProductsModule)
      },
      {
        path: 'model',
        loadChildren: () => import('./model/model.module').then(m => m.WikunumModelModule)
      },
      {
        path: 'job',
        loadChildren: () => import('./job/job.module').then(m => m.WikunumJobModule)
      },
      {
        path: 'job-details',
        loadChildren: () => import('./job-details/job-details.module').then(m => m.WikunumJobDetailsModule)
      },
      {
        path: 'job-status',
        loadChildren: () => import('./job-status/job-status.module').then(m => m.WikunumJobStatusModule)
      },
      {
        path: 'items',
        loadChildren: () => import('./items/items.module').then(m => m.WikunumItemsModule)
      },
      {
        path: 'item-add-ons',
        loadChildren: () => import('./item-add-ons/item-add-ons.module').then(m => m.WikunumItemAddOnsModule)
      },
      {
        path: 'item-bin-card',
        loadChildren: () => import('./item-bin-card/item-bin-card.module').then(m => m.WikunumItemBinCardModule)
      },
      {
        path: 'purchase-order',
        loadChildren: () => import('./purchase-order/purchase-order.module').then(m => m.WikunumPurchaseOrderModule)
      },
      {
        path: 'purchase-order-details',
        loadChildren: () => import('./purchase-order-details/purchase-order-details.module').then(m => m.WikunumPurchaseOrderDetailsModule)
      },
      {
        path: 'goods-receipt',
        loadChildren: () => import('./goods-receipt/goods-receipt.module').then(m => m.WikunumGoodsReceiptModule)
      },
      {
        path: 'goods-receipt-details',
        loadChildren: () => import('./goods-receipt-details/goods-receipt-details.module').then(m => m.WikunumGoodsReceiptDetailsModule)
      },
      {
        path: 'invoice',
        loadChildren: () => import('./invoice/invoice.module').then(m => m.WikunumInvoiceModule)
      },
      {
        path: 'invoice-details',
        loadChildren: () => import('./invoice-details/invoice-details.module').then(m => m.WikunumInvoiceDetailsModule)
      },
      {
        path: 'storage-bin',
        loadChildren: () => import('./storage-bin/storage-bin.module').then(m => m.WikunumStorageBinModule)
      },
      {
        path: 'unit-of-measure',
        loadChildren: () => import('./unit-of-measure/unit-of-measure.module').then(m => m.WikunumUnitOfMeasureModule)
      },
      {
        path: 'currency',
        loadChildren: () => import('./currency/currency.module').then(m => m.WikunumCurrencyModule)
      },
      {
        path: 'document-number-config',
        loadChildren: () => import('./document-number-config/document-number-config.module').then(m => m.WikunumDocumentNumberConfigModule)
      },
      {
        path: 'user-permissions',
        loadChildren: () => import('./user-permissions/user-permissions.module').then(m => m.WikunumUserPermissionsModule)
      },
      {
        path: 'user-group',
        loadChildren: () => import('./user-group/user-group.module').then(m => m.WikunumUserGroupModule)
      },
      {
        path: 'menu-items',
        loadChildren: () => import('./menu-items/menu-items.module').then(m => m.WikunumMenuItemsModule)
      },
      {
        path: 'cash-payment-voucher-customer',
        loadChildren: () =>
          import('./cash-payment-voucher-customer/cash-payment-voucher-customer.module').then(
            m => m.WikunumCashPaymentVoucherCustomerModule
          )
      },
      {
        path: 'cash-receipt-voucher-expense',
        loadChildren: () =>
          import('./cash-receipt-voucher-expense/cash-receipt-voucher-expense.module').then(m => m.WikunumCashReceiptVoucherExpenseModule)
      },
      {
        path: 'cash-payment-voucher-expense',
        loadChildren: () =>
          import('./cash-payment-voucher-expense/cash-payment-voucher-expense.module').then(m => m.WikunumCashPaymentVoucherExpenseModule)
      },
      {
        path: 'cash-receipt-voucher-customer',
        loadChildren: () =>
          import('./cash-receipt-voucher-customer/cash-receipt-voucher-customer.module').then(
            m => m.WikunumCashReceiptVoucherCustomerModule
          )
      },
      {
        path: 'cash-payment-voucher-supplier',
        loadChildren: () =>
          import('./cash-payment-voucher-supplier/cash-payment-voucher-supplier.module').then(
            m => m.WikunumCashPaymentVoucherSupplierModule
          )
      },
      {
        path: 'cash-receipt-voucher-supplier',
        loadChildren: () =>
          import('./cash-receipt-voucher-supplier/cash-receipt-voucher-supplier.module').then(
            m => m.WikunumCashReceiptVoucherSupplierModule
          )
      },
      {
        path: 'cash-book',
        loadChildren: () => import('./cash-book/cash-book.module').then(m => m.WikunumCashBookModule)
      },
      {
        path: 'cash-book-balance',
        loadChildren: () => import('./cash-book-balance/cash-book-balance.module').then(m => m.WikunumCashBookBalanceModule)
      },
      {
        path: 'customer-account',
        loadChildren: () => import('./customer-account/customer-account.module').then(m => m.WikunumCustomerAccountModule)
      },
      {
        path: 'customer-account-balance',
        loadChildren: () =>
          import('./customer-account-balance/customer-account-balance.module').then(m => m.WikunumCustomerAccountBalanceModule)
      },
      {
        path: 'supplier-account',
        loadChildren: () => import('./supplier-account/supplier-account.module').then(m => m.WikunumSupplierAccountModule)
      },
      {
        path: 'supplier-account-balance',
        loadChildren: () =>
          import('./supplier-account-balance/supplier-account-balance.module').then(m => m.WikunumSupplierAccountBalanceModule)
      },
      {
        path: 'purchase-account',
        loadChildren: () => import('./purchase-account/purchase-account.module').then(m => m.WikunumPurchaseAccountModule)
      },
      {
        path: 'purchase-account-balance',
        loadChildren: () =>
          import('./purchase-account-balance/purchase-account-balance.module').then(m => m.WikunumPurchaseAccountBalanceModule)
      },
      {
        path: 'sales-account',
        loadChildren: () => import('./sales-account/sales-account.module').then(m => m.WikunumSalesAccountModule)
      },
      {
        path: 'sales-account-balance',
        loadChildren: () => import('./sales-account-balance/sales-account-balance.module').then(m => m.WikunumSalesAccountBalanceModule)
      },
      {
        path: 'expense-account',
        loadChildren: () => import('./expense-account/expense-account.module').then(m => m.WikunumExpenseAccountModule)
      },
      {
        path: 'expense-account-balance',
        loadChildren: () =>
          import('./expense-account-balance/expense-account-balance.module').then(m => m.WikunumExpenseAccountBalanceModule)
      },
      {
        path: 'document-type',
        loadChildren: () => import('./document-type/document-type.module').then(m => m.WikunumDocumentTypeModule)
      },
      {
        path: 'document-history',
        loadChildren: () => import('./document-history/document-history.module').then(m => m.WikunumDocumentHistoryModule)
      },
      {
        path: 'change-log',
        loadChildren: () => import('./change-log/change-log.module').then(m => m.WikunumChangeLogModule)
      },
      {
        path: 'transaction-type',
        loadChildren: () => import('./transaction-type/transaction-type.module').then(m => m.WikunumTransactionTypeModule)
      },
      {
        path: 'payment-types',
        loadChildren: () => import('./payment-types/payment-types.module').then(m => m.WikunumPaymentTypesModule)
      },
      {
        path: 'payment-type-account',
        loadChildren: () => import('./payment-type-account/payment-type-account.module').then(m => m.WikunumPaymentTypeAccountModule)
      },
      {
        path: 'payment-type-balance',
        loadChildren: () => import('./payment-type-balance/payment-type-balance.module').then(m => m.WikunumPaymentTypeBalanceModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.WikunumLocationModule)
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.WikunumCustomerModule)
      },
      {
        path: 'supplier',
        loadChildren: () => import('./supplier/supplier.module').then(m => m.WikunumSupplierModule)
      },
      {
        path: 'expense',
        loadChildren: () => import('./expense/expense.module').then(m => m.WikunumExpenseModule)
      },
      {
        path: 'worker',
        loadChildren: () => import('./worker/worker.module').then(m => m.WikunumWorkerModule)
      },
      {
        path: 'ex-user',
        loadChildren: () => import('./ex-user/ex-user.module').then(m => m.WikunumExUserModule)
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.WikunumStockModule)
      },
      {
        path: 'company',
        loadChildren: () => import('./company/company.module').then(m => m.WikunumCompanyModule)
      },
      {
        path: 'configuration-items',
        loadChildren: () => import('./configuration-items/configuration-items.module').then(m => m.WikunumConfigurationItemsModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class WikunumEntityModule {}
