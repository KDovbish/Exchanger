package org.example.service;

import org.example.entity.RequestEntity;
import org.example.enumtype.OperationStatus;
import org.example.enumtype.OperationType;
import org.example.repositary.FinalReport;
import org.example.repositary.RequestRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService{

    @Autowired
    RequestRepositary requestRepositary;
    @Autowired
    ExchangeRateServiceImpl exchangeRateService;

    public RequestEntity bankBuy(String ccy, BigDecimal ccySum, String firstName, String lastName, String tel){

            //  Получаем последний установленный курс покупки заданной валюты
            BigDecimal rateBuy = exchangeRateService.getLast(ccy).getBuy();

            //  Формируем и сохраняем в базе документ заявки с курсом покупки и вычисленным эквивалентом
            RequestEntity request = new RequestEntity();
            request.setType(OperationType.BUY);
            request.setCcy(ccy);
            request.setRate(rateBuy);
            request.setCcySum(ccySum);
            request.setUahSum( ccySum.multiply(rateBuy) );
            request.setStatus(OperationStatus.NEW);
            request.setFirstName(firstName);
            request.setLastName(lastName);
            request.setTel(tel);
            request.setOtp(generateOneTimePassword());
            request.setTs(new Timestamp(System.currentTimeMillis()));
            requestRepositary.save(request);

            return request;
    }


    @Override
    public RequestEntity bankSale(String ccy, BigDecimal ccySum, String firstName, String lastName, String tel) {

        //  Получаем последний установленный курс продажи для заданной валюты
        BigDecimal rateSale = exchangeRateService.getLast(ccy).getSale();

        //  Формируем и сохраняем в базе документ заявки с курсом продажи и вычисленным эквивалентом
        RequestEntity request = new RequestEntity();
        request.setType(OperationType.SALE);
        request.setCcy(ccy);
        request.setRate(rateSale);
        request.setCcySum(ccySum);
        request.setUahSum( ccySum.multiply(rateSale) );
        request.setStatus(OperationStatus.NEW);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setTel(tel);
        request.setOtp(generateOneTimePassword());
        request.setTs(new Timestamp(System.currentTimeMillis()));
        requestRepositary.save(request);

        return request;
    }




    public List<RequestEntity> confirm(String tel, String otp) {
        //  Получаем все заявки в статусе "Новая" с заданным телефоном
        List<RequestEntity> list = requestRepositary.findByTelAndStatus(tel, OperationStatus.NEW);
        //  Выбираем из них те, по которым совпадает пароль
        List<RequestEntity> listEqualsOtp = list.stream()
                .filter(e -> e.getOtp().equals(otp))
                .collect(Collectors.toList());

        if (listEqualsOtp.size() > 0) {
            //  Существует заявка(очень врядли, заявки) по которым совпадает пароль, сообщенный клиентом.
            //  Заявка(или заявки) переводиться в состояние "Выполнена"
            listEqualsOtp.forEach(e -> setStatus(e, OperationStatus.DONE));
            return listEqualsOtp;
        } else {
            //  Есть заявка(м.б. заявки) в статусе "Новый". Клиент сообщает пароль и он не подходит ни
            //  для одной заявки. Все заявки этого клиента аннулируются.
            list.forEach(e -> setStatus(e, OperationStatus.CANCELLED));
            return list;
        }
    }


    @Override
    public List<RequestEntity> getNewRequestByTel(String tel) {
        return requestRepositary.findByTelAndStatus(tel, OperationStatus.NEW);
    }

    @Override
    public Optional<RequestEntity> delete(Integer id) {
        Optional<RequestEntity> request = requestRepositary.findById(id);
        request.ifPresent(requestRepositary::delete);
        return request;
    }


    @Override
    public List<FinalReport> makeFinalReport() {
        return requestRepositary.makeFinalReport();
    }

    @Override
    public List<RequestEntity> makeDateRangeRequestReport(String ccy, Date beginDate, Date endDate) {
        return requestRepositary.findByCcyAndTsBetween(ccy, new Timestamp(beginDate.getTime()), new Timestamp(endDate.getTime() + 86399399l)); // добиваем в мс до 23:59:59.999
    }



    /**
     * Прописать статус для заявки
     * @param requestEntity Запись заявки в БД
     * @param status Статус заявки
     * @return Запись заявки с измененным стаутусом
     */
    RequestEntity setStatus(RequestEntity requestEntity, OperationStatus status) {
        if (requestEntity.getStatus() != status) {
            requestEntity.setStatus(status);
            return requestRepositary.save(requestEntity);
        } else {
            return requestEntity;
        }
    }

    /**
     * Генерация строки с otp-паролем из четырех цифр
     * @return 0000..9999
     */
    public String generateOneTimePassword() {
        return String.format("%04d", (int) ( Math.random() * 9999 ) );
    }


}
