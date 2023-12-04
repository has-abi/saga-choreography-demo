package com.ab.saga.paymentservice.application.service;

import com.ab.saga.paymentservice.application.dto.OrderCreatedEventDto;
import com.ab.saga.paymentservice.application.dto.PaymentProcessedEventDto;
import com.ab.saga.paymentservice.application.dto.ShipmentFailedEventDto;
import com.ab.saga.paymentservice.application.enums.PaymentStatus;
import com.ab.saga.paymentservice.application.enums.ShipmentStatus;
import com.ab.saga.paymentservice.application.events.publisher.PaymentEventPublisher;
import com.ab.saga.paymentservice.domain.entity.Transaction;
import com.ab.saga.paymentservice.domain.repository.BalanceRepository;
import com.ab.saga.paymentservice.domain.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        userBalance.filter(balance -> balance.getBalance() >= eventDto.getAmount())
                .map(bl -> {
                    bl.setBalance(bl.getBalance() - eventDto.getAmount());

                    Transaction transaction = new Transaction();
                    transaction.setUserId(eventDto.getUserId());
                    transaction.setOrderId(eventDto.getOrderId());
                    transaction.setAmount(eventDto.getAmount());

                    transactionRepository.save(transaction);

                    paymentEventDto.setPaymentStatus(PaymentStatus.PAYMENT_COMPLETED);

                    log.info("PaymentEventPublisher#publishPaymentProcessedEvent: Payment completed for orderId={}, userId={}",
                            eventDto.getOrderId(), eventDto.getUserId());

                    return Optional.of(bl);
                }).or(() -> {
                    paymentEventDto.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);

                    log.info("PaymentEventPublisher#publishPaymentProcessedEvent: Payment Failed for orderId={}, userId={}",
                            eventDto.getOrderId(), eventDto.getUserId());

                    return Optional.empty();
                });

        paymentEventPublisher.publishPaymentEvent(paymentEventDto);
    }

    @Transactional
    @Override
    public void cancelPayment(ShipmentFailedEventDto eventDto) {
        if (eventDto.getShipmentStatus().equals(ShipmentStatus.SHIPMENT_FAILED)) {
            var orderTransaction = transactionRepository.findByOrderId(eventDto.getOrderId());

            orderTransaction.ifPresent(transaction -> {
                var userBalance = balanceRepository.findByUserId(eventDto.getUserId());
                userBalance.ifPresent(balance -> balance.setBalance(balance.getBalance() + transaction.getAmount()));
                transactionRepository.delete(transaction);

                log.info("PaymentService#rollbackPayment: Successful rollback for orderId={}, userId={}, amount={}",
                        eventDto.getOrderId(), eventDto.getUserId(), transaction.getAmount());

                var paymentEventDto = new PaymentProcessedEventDto(eventDto.getUserId(), eventDto.getOrderId());
                paymentEventDto.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
                paymentEventPublisher.publishPaymentEvent(paymentEventDto);
            });
        }
    }
}
