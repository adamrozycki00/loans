package com.tenetmind.loans.operation.service.accounting;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateNotFoundException;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.operation.service.PaymentDto;
import org.springframework.stereotype.Service;

@Service
public interface AccountingUnit {

    Loan prepareSettlementOfMakingLoan(PaymentDto paymentDto) throws LoanNotFoundException;

    Loan prepareSettlementOfPayment(PaymentDto paymentDto) throws CurrencyNotFoundException,
            CurrencyRateNotFoundException, LoanNotFoundException;

}
