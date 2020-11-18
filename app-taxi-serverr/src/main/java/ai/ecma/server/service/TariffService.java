//package ai.ecma.server.service;
//
//import ai.ecma.server.entity.*;
//import ai.ecma.server.exceptions.ResourceNotFoundException;
//import ai.ecma.server.payload.Result;
//import ai.ecma.server.payload.TariffDto;
//import ai.ecma.server.repository.AttachmentRepository;
//import ai.ecma.server.repository.CarRepository;
//import ai.ecma.server.repository.ModelRepository;
//import ai.ecma.server.repository.TariffRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class TariffService {
//    @Autowired
//    TariffRepository tariffRepository;
//    @Autowired
//    ModelRepository modelRepository;
//    @Autowired
//    CarRepository carRepository;
//    @Autowired
//    AttachmentRepository attachmentRepository;
//
//    public List<TariffDto> getTariffListByDriverId(User user) {
//        List<TariffDto> tariffDtos = new ArrayList<>();
//        try {
//            List<Tariff> tariffList = tariffRepository.findAllByDriverId(user.getId());
//            for (int i = 0; i < tariffList.size(); i++) {
//                TariffDto tariffDto = new TariffDto();
//                tariffDto.setId(tariffList.get(i).getId());
//                tariffDto.setName(tariffList.get(i).getName());
//                tariffDto.setInitialPrice(tariffList.get(i).getInitialPrice());
//                tariffDto.setWaitingPricePreMinute(tariffList.get(i).getWaitingPricePreMinute());
//                tariffDto.setPricePerKm(tariffList.get(i).getPricePerKm());
//                tariffDto.setInitialFreeWaitingTime(tariffList.get(i).getInitialFreeWaitingTime());
//                tariffDto.setDescription(tariffList.get(i).getDescription());
//                tariffDtos.add(tariffDto);
//            }
//            return tariffDtos;
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
//
//    public List<TariffDto> getTariffListByCarId(UUID carId) {
//        Car car = carRepository.findByCarId(carId).orElseThrow(() -> new ResourceNotFoundException("car", "carId", carId));
//        String modelName = car.getModel().getName();
//        List<Tariff> tariffList = tariffRepository.findAllByCarId(carId);
//        List<TariffDto> tariffDtos = new ArrayList<>();
//        tariffList.forEach(tariff -> tariffDtos.add(getTariffDto(tariff, modelName)));
//        return tariffDtos;
//    }
//
//    public Result addTariff(TariffDto tariffDto) {
//        List<Model> modelList = modelRepository.findAllById(tariffDto.getModelId());
//        Attachment attachment = attachmentRepository.findById(tariffDto.getAttachmentId()).orElseThrow(() -> new ResourceNotFoundException("Attachment", "attachmentId", tariffDto.getId()));
//        Tariff tariff = new Tariff();
//        getTariff(tariff, tariffDto, attachment, modelList);
//        try {
//            tariffRepository.save(tariff);
//            return new Result("Created", true);
//        } catch (Exception e) {
//            return new Result("Xatolik", false);
//        }
//    }
//
//    public Result updateTariff(Integer id, TariffDto tariffDto) {
//        Tariff tariff = tariffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("tariff", "tariff_id", tariffDto.getId()));
//        Attachment attachment = attachmentRepository.findById(tariffDto.getAttachmentId()).orElseThrow(() -> new ResourceNotFoundException("attachment", "attachmentId", tariffDto.getAttachmentId()));
//        List<Model> modelList = modelRepository.findAllById(tariffDto.getModelId());
//        try {
//            tariffRepository.save(getTariff(tariff, tariffDto, attachment, modelList));
//            return new Result("Update", true);
//        } catch (Exception e) {
//            return new Result("UnUpdated", false);
//        }
//
//    }
//
//    public Result deleteTariffById(Integer id) {
//        try {
//            tariffRepository.deleteById(id);
//            return new Result("Deleted", true);
//        } catch (Exception e) {
//            return new Result("Tariff topilmadi", false);
//        }
//    }
//
//
//    public Tariff getTariff(Tariff tariff, TariffDto tariffDto, Attachment attachment, List<Model> modelList) {
//        tariff.setName(tariffDto.getName());
//        tariff.setInitialPrice(tariffDto.getInitialPrice());
//        tariff.setWaitingPricePreMinute(tariffDto.getWaitingPricePreMinute());
//        tariff.setPricePerKm(tariffDto.getPricePerKm());
//        tariff.setInitialFreeWaitingTime(tariffDto.getInitialFreeWaitingTime());
//        tariff.setDescription(tariffDto.getDescription());
//        tariff.setAttachment(attachment);
//        tariff.setModel(modelList);
//        return tariff;
//    }
//
//    public TariffDto getTariffDto(Tariff tariff, String modelName) {
//        TariffDto tariffDto = new TariffDto();
//        tariffDto.setId(tariff.getId());
//        tariffDto.setName(tariff.getName());
//        tariffDto.setInitialPrice(tariff.getInitialPrice());
//        tariffDto.setWaitingPricePreMinute(tariff.getWaitingPricePreMinute());
//        tariffDto.setPricePerKm(tariff.getPricePerKm());
//        tariffDto.setInitialFreeWaitingTime(tariff.getInitialFreeWaitingTime());
//        tariffDto.setAttachmentId(tariff.getAttachment().getId());
//        tariffDto.setAttachmentName(tariff.getAttachment().getName());
//        tariffDto.setModelName(modelName);
//        tariffDto.setDescription(tariff.getDescription());
//        return tariffDto;
//    }
//}
