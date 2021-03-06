package com.tenetmind.loans.operation.service.accounting;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currencyrate.converter.CurrencyRateConversionException;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.service.PaymentDto;
import com.tenetmind.loans.operation.service.processor.OperationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public interface AccountingUnit {

    Loan prepareSettlementOfMakingLoan(PaymentDto paymentDto) throws LoanNotFoundException;

    Loan prepareSettlementOfPayment(PaymentDto paymentDto) throws CurrencyRateConversionException,
            CurrencyNotFoundException, LoanNotFoundException;

}
