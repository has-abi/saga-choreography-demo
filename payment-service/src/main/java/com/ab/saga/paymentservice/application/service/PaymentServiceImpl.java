package com.ab.saga.paymentservice.application.service;

import com.ab.saga.paymentservice.application.dto.OrderCreatedEventDto;
import com.ab.saga.paymentservice.application.dto.PaymentProcessedEventDto;
import com.ab.saga.paymentservice.application.dto.ShipmentFailedEventDto;
import com.ab.saga.paymentservice.application.enums.PaymentStatus;
import com.ab.saga.paymentservice.application.events.publisher.PaymentEventPublisher;
import com.ab.saga.paymentservice.domain.entity.Transaction;
import com.ab.saga.paymentservice.domain.repository.BalanceRepository;
import com.ab.saga.paymentservice.domain.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    @Override
    public void processPayment(OrderCreatedEventDto eventDto) {
        var paymentEventDto = new PaymentProcessedEventDto(eventDto.getUserId(), eventDto.getOrderId());

        var userBalance = balanceRepository.findByUserId(eventDto.getUserId());
        userBalance.ifPresent(balance -> {
            if (balance.getBalance() >= eventDto.getAmount()) {
                Transaction transaction = new Transaction();
                transaction.setUserId(eventDto.getUserId());
                transaction.setOrderId(eventDto.getOrderId());
                transaction.setAmount(eventDto.getAmount());

                transactionRepository.save(transaction);

                balance.setBalance(balance.getBalance() - eventDto.getAmount());

                paymentEventDto.setPaymentStatus(PaymentStatus.PAYMENT_COMPLETED);
            }
        });

        paymentEventPublisher.publishPaymentProcessedEvent(paymentEventDto);
    }

    @Transactional
    @Override
    public void rollbackPayment(ShipmentFailedEventDto eventDto) {
        var orderTransaction = transactionRepository.findByOrderId(eventDto.getOrderId());

        orderTransaction.ifPresent(transaction -> {
            var userBalance = balanceRepository.findByUserId(eventDto.getUserId());
            userBalance.ifPresent(balance -> balance.setBalance(balance.getBalance() + transaction.getAmount()));
            transactionRepository.delete(transaction);

            log.info("PaymentService#rollbackPayment: Successful rollback for orderId={}, userId={}, amount={}",
                    eventDto.getOrderId(), eventDto.getUserId(), transaction.getAmount());

            var paymentEventDto = new PaymentProcessedEventDto(eventDto.getUserId(), eventDto.getOrderId());
            paymentEventPublisher.publishPaymentProcessedEvent(paymentEventDto);
        });
    }
}
